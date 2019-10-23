package com.wei.springboot.starter.mybatis;

import com.alibaba.druid.sql.PagerUtils;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLQueryExpr;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLJoinTableSource;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.util.StringUtils;
import com.wei.springboot.starter.bean.Page;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.jdbc.ConnectionLogger;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 自动分页插件
 *
 * @author lee
 */
@Intercepts({@Signature(
        method = "query",
        type = Executor.class,
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
)})
@SuppressWarnings("unchecked")
public class PageInterceptor implements Interceptor {

    /**
     * 每页最大返回记录数
     */
    private int maxSize = 500;
    /**
     * 第页最小记录数, 当传入参数小于等于0时, 重置
     */
    private int minSize = 20;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Executor executor = (Executor) invocation.getTarget();
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameter = args[1];

        if (SqlCommandType.SELECT != ms.getSqlCommandType()
                || StatementType.CALLABLE == ms.getStatementType()) {
            return invocation.proceed();
        }
        // 检查是否有分页对象, 如果没有则不进行分页查询
        Page<?> page = getPage(parameter);
        if (page == null || !page.isCount()) {
            return invocation.proceed();
        }

        // 检查最大最小记录数
        if (page.getSize() <= 0) {
            page.setSize(minSize);
        }
        if (maxSize > 0 && page.getSize() > maxSize) {
            page.setSize(maxSize);
        }

        // count总数
        BoundSql boundSql = ms.getBoundSql(parameter);
        String countSql = null;
        try {
            countSql = PagerUtils.count(boundSql.getSql(), "mysql");
        } catch (Exception e) {
            ms.getStatementLog().error(boundSql.getSql(), e);
            throw e;
        }
        BoundSql countBoundSql = new BoundSql(ms.getConfiguration(), countSql, boundSql.getParameterMappings(), boundSql.getParameterObject());
        Cache cache = ms.getCache();
        if (cache != null && ms.isUseCache() && ms.getConfiguration().isCacheEnabled()) {
            CacheKey cacheKey = executor.createCacheKey(ms, parameter, RowBounds.DEFAULT, countBoundSql);
            Long count = (Long) cache.getObject(cacheKey);
            if (count == null) {
                count = this.getTotalCount(executor, ms, countBoundSql);
                cache.putObject(cacheKey, count);
                page.setTotal(count);
            }
        } else {
            page.setTotal(this.getTotalCount(executor, ms, countBoundSql));
        }

        // 为0时, 直接返回空集
        if (page.getTotal() == 0) {
            return new ArrayList<>(0);
        }

        // 重写分页sql
        String sql = orderBy(page, boundSql.getSql());
        sql += " LIMIT ?, ?";
        // 分页参数设置
        List<ParameterMapping> parameterMappings = new ArrayList<>(boundSql.getParameterMappings());
        ParameterMapping offsetParameterMapping = new ParameterMapping.Builder(ms.getConfiguration(), "_offset", Long.class).build();
        ParameterMapping limitParameterMapping = new ParameterMapping.Builder(ms.getConfiguration(), "_limit", Long.class).build();
        parameterMappings.add(offsetParameterMapping);
        parameterMappings.add(limitParameterMapping);
        ((Map) parameter).put("_offset", page.offset());
        ((Map) parameter).put("_limit", page.getSize());

        // 覆盖重写的sql
        SqlSource sqlSource = new BoundSqlSqlSource(new BoundSql(ms.getConfiguration(), sql, parameterMappings, boundSql.getParameterObject()));
        MappedStatement rewriteMs = copyFromMappedStatement(ms, sqlSource);
        args[0] = rewriteMs;
        args[1] = parameter;

        // 查询结果
        List list = (List) invocation.proceed();
        // 设置返回结果
        page.setList(list);
        return list;
    }

    private Page<?> getPage(Object parameter) {
        if (parameter instanceof Map) {
            for (Object p : ((Map) parameter).values()) {
                if (p instanceof Page) {
                    return ((Page) p);
                }
            }
        }
        return null;
    }

    /**
     * 排序条件
     *
     * @param page
     * @param sql
     * @return
     */
    private String orderBy(Page page, String sql) {
        // 是否需要排序
        if (page.getAscs() != null || page.getDescs() != null) {
            SQLExpr expr = SQLUtils.toMySqlExpr(sql);
            MySqlSelectQueryBlock select = (MySqlSelectQueryBlock) (((SQLQueryExpr) expr).getSubQuery().getQuery());
            StringBuilder selectBuilder = new StringBuilder("SELECT ");
            select.getSelectList().forEach(i -> selectBuilder.append(i.toString()).append(","));
            StringBuilder sqlBuilder = new StringBuilder(selectBuilder.subSequence(0, selectBuilder.length() - 1));
            sqlBuilder.append(" FROM ");
            getFrom(sqlBuilder, select.getFrom());

            if (select.getWhere() != null) {
                sqlBuilder.append(" WHERE ").append(select.getWhere().toString()).append(" ");
            }
            if (select.getGroupBy() != null) {
                sqlBuilder.append(" GROUP BY ").append(select.getGroupBy().getItems().stream().map(Object::toString)
                        .collect(Collectors.joining(","))).append(" ");
                if (select.getGroupBy().getHaving() != null) {
                    sqlBuilder.append(select.getGroupBy().getHaving().toString()).append(" ");
                }
            }
            sqlBuilder.append(" ORDER BY ");
            List<String> orderBy = new ArrayList<>(1);
            if (select.getOrderBy() != null) {
                orderBy = select.getOrderBy().getItems().stream()
                        .map(i -> i.getExpr().toString() + " " + i.getType().name).collect(Collectors.toList());
            }
            if (page.getAscs() != null && page.getAscs().length > 0) {
                orderBy.addAll(Stream.of(page.getAscs()).filter(a -> !"".equals(a)).collect(Collectors.toList()));
            }
            if (page.getDescs() != null) {
                orderBy.addAll(Stream.of(page.getDescs()).filter(d -> !"".equals(d)).map(d -> d.toLowerCase().trim().endsWith("desc") ? d : d.concat(" desc")).collect(Collectors.toList()));
            }
            sqlBuilder.append(String.join(",", orderBy));
            return sqlBuilder.toString();
        }
        return sql;
    }

    /**
     * 重写sql时, 查询表及别名设置
     *
     * @param sqlBuilder
     * @param from
     * @return
     */
    private void getFrom(StringBuilder sqlBuilder, SQLTableSource from) {
        if (from instanceof SQLExprTableSource) {
            sqlBuilder.append(from.toString()).append(" ");
            if (!StringUtils.isEmpty(from.getAlias())) {
                sqlBuilder.append(from.getAlias()).append(" ");
            }
        }
        if (from instanceof SQLJoinTableSource) {
            getFrom(sqlBuilder, ((SQLJoinTableSource) from).getLeft());
            sqlBuilder.append(((SQLJoinTableSource) from).getJoinType().name).append(" ");
            getFrom(sqlBuilder, ((SQLJoinTableSource) from).getRight());
            sqlBuilder.append(" ON ").append(((SQLJoinTableSource) from).getCondition());
        }
    }

    /**
     * 查询总记录数
     *
     * @param executor
     * @param ms
     * @param countSql
     * @return
     * @throws SQLException
     */
    private Long getTotalCount(Executor executor, MappedStatement ms, BoundSql countSql) throws SQLException {
        Log log = ms.getStatementLog();
        Connection conn = executor.getTransaction().getConnection();
        if (log.isDebugEnabled()) {
            conn = ConnectionLogger.newInstance(conn, log, 1);
        }

        PreparedStatement countStmt = conn.prepareStatement(countSql.getSql());
        DefaultParameterHandler handler = new DefaultParameterHandler(ms, countSql.getParameterObject(), countSql);
        handler.setParameters(countStmt);
        ResultSet rs = null;

        try {
            rs = countStmt.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ignore) {
                }
            }
            if (countStmt != null) {
                try {
                    countStmt.close();
                } catch (SQLException ignore) {
                }
            }

        }

        return 0L;
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public int getMinSize() {
        return minSize;
    }

    public void setMinSize(int minSize) {
        this.minSize = minSize;
    }

    private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length > 0) {
            builder.keyProperty(String.join(",", ms.getKeyProperties()));
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }


    public static class BoundSqlSqlSource implements SqlSource {
        private BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        @Override
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }
}
