package com.wei.springboot.starter.code.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.*;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.mybatis.generator.internal.util.StringUtility;

import java.io.File;
import java.util.*;

/**
 * @author William
 * @Date 2019/3/22
 * @Description 代码生成插件
 */
@Mojo(name = "generator", defaultPhase = LifecyclePhase.GENERATE_SOURCES, requiresDependencyResolution = ResolutionScope.TEST)
public class CodeGeneratorMojo extends AbstractMojo {

    private static final String targetProject = "src\\main\\java";
    private static final String targetResources = "src\\main\\resources";

    @Parameter(property = "project", required = true, readonly = true)
    private MavenProject project;

    /**
     * MBG配置文件，未配置则使用Java方式配置
     */
    @Parameter(defaultValue = "${project.basedir}/src/main/resources/generatorConfig.xml")
    private File configurationFile;

    @Parameter(defaultValue = "true")
    private boolean overwrite;

    @Parameter(property = "jdbcDriver")
    private String jdbcDriver;

    @Parameter(property = "jdbcURL")
    private String jdbcURL;

    @Parameter(property = "jdbcUserId")
    private String jdbcUserId;

    @Parameter(property = "jdbcPassword")
    private String jdbcPassword;

    /**
     * 要生成的表名，全部可以指定为all，部分使用英文逗号分隔
     */
    @Parameter(property = "tableNames", defaultValue = "all")
    private String tableNames;

    /**
     * Mapper继承类，通用Mapper中的设置
     */
    @Parameter(property = "xMapper")
    private String xMapper;

    /**
     * 基础包名
     */
    @Parameter(property = "basePackage")
    private String basePackage;

    /**
     * Swagger开启
     */
    @Parameter(defaultValue = "true")
    private boolean swagger;

    /**
     * 需要生成的类，Base-xml与model生成,Dto-Dto生成,Service-Service接口与实现生成,Controller-控制层生成
     */
    @Parameter(property = "generatorClass", defaultValue = "Model")
    private List<String> generatorClass;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            List<String> warnings = new ArrayList<>();
            Configuration config = new Configuration();
            if (configurationFile != null && configurationFile.exists()) {
                ConfigurationParser cp = new ConfigurationParser(warnings);
                config = cp.parseConfiguration(configurationFile);
            } else {
                // 构建配置
                // 该模型为每一张表只生成一个实体类。这个实体类包含表中的所有字段。
                Context context = new Context(ModelType.FLAT);
                context.setId("code-generator");
                context.setTargetRuntime("MyBatis3");
                addPluginConfigurationToContext(context);
                // Dto Service Controller是否生成
                if (generatorClass.contains("Dto")) {
                    addDtoPluginConfigurationToContext(context);
                }
                if (generatorClass.contains("Service")) {
                    addServicePluginConfigurationToContext(context);
                }
                if (generatorClass.contains("Controller")) {
                    addControllerPluginConfigurationToContext(context);
                }
                context.setCommentGeneratorConfiguration(getCommentGeneratorConfiguration());
                context.setJdbcConnectionConfiguration(getJdbcConnectionConfiguration());
                context.setJavaTypeResolverConfiguration(getJavaTypeResolverConfiguration());
                context.setJavaModelGeneratorConfiguration(getJavaModelGeneratorConfiguration());
                context.setJavaClientGeneratorConfiguration(getJavaClientGeneratorConfiguration());
                if (generatorClass.contains("Xml")) {
                    context.setSqlMapGeneratorConfiguration(getSqlMapGeneratorConfiguration());
                }
                addTableConfiguration(context);
                config.addContext(context);
            }
            DefaultShellCallback callback = new DefaultShellCallback(overwrite);
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
            myBatisGenerator.generate(null);
            for (String warning : warnings) {
                getLog().warn(warning);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void addTableConfiguration(Context context) {
        if (Objects.equals(tableNames, "all")) {
            context.addTableConfiguration(getTableConfiguration(context, "%", null));
        } else {
            String[] split = tableNames.trim().split(",");
            for (String table : split) {
                if (StringUtility.stringHasValue(table)) {
                    context.addTableConfiguration(getTableConfiguration(context, table, null));
                }
            }
        }
    }

    private TableConfiguration getTableConfiguration(Context context, String tableName, String domainName) {
        TableConfiguration configuration = new TableConfiguration(context);
        configuration.setTableName(tableName);
        if (StringUtility.stringHasValue(domainName)) {
            configuration.setDomainObjectName(domainName);
        }
        configuration.setDeleteByExampleStatementEnabled(false);
        configuration.setUpdateByExampleStatementEnabled(false);
        configuration.setSelectByExampleStatementEnabled(false);
        configuration.setCountByExampleStatementEnabled(false);
        GeneratedKey generatedKey = new GeneratedKey("id", "Mysql", true, "post");
        configuration.setGeneratedKey(generatedKey);
        ColumnOverride is_del = new ColumnOverride("is_del");
        is_del.setJavaProperty("del");
        configuration.addColumnOverride(is_del);
        return configuration;
    }

    private JavaClientGeneratorConfiguration getJavaClientGeneratorConfiguration() {
        JavaClientGeneratorConfiguration configuration = new JavaClientGeneratorConfiguration();
        configuration.setTargetPackage(basePackage + ".mapper");
        configuration.setTargetProject(targetProject);
        if (!generatorClass.contains("Xml")) {
            configuration.setConfigurationType("ANNOTATEDMAPPER");
        } else {
            configuration.setConfigurationType("XMLMAPPER");
        }
        return configuration;
    }

    private SqlMapGeneratorConfiguration getSqlMapGeneratorConfiguration() {
        SqlMapGeneratorConfiguration configuration = new SqlMapGeneratorConfiguration();
        configuration.setTargetPackage("mapper");
        configuration.setTargetProject(targetResources);
        return configuration;
    }

    private JavaModelGeneratorConfiguration getJavaModelGeneratorConfiguration() {
        JavaModelGeneratorConfiguration configuration = new JavaModelGeneratorConfiguration();
        configuration.setTargetPackage(basePackage + ".model");
        configuration.setTargetProject(targetProject);
        return configuration;
    }

    private JavaTypeResolverConfiguration getJavaTypeResolverConfiguration() {
        JavaTypeResolverConfiguration configuration = new JavaTypeResolverConfiguration();
        configuration.addProperty("forceBigDecimals", "false");
        return configuration;
    }

    private CommentGeneratorConfiguration getCommentGeneratorConfiguration() {
        CommentGeneratorConfiguration configuration = new CommentGeneratorConfiguration();
        configuration.addProperty("suppressDate", "true");
        return configuration;
    }


    private void addDtoPluginConfigurationToContext(Context context) {
        // Dto代码生成插件
        String filePlugin = "tk.mybatis.mapper.generator.TemplateFilePlugin";
        // Dto
        Map<String, String> dtoProperties = new HashMap<>();
        dtoProperties.put("targetProject", targetProject);
        dtoProperties.put("targetPackage", basePackage + ".dto");
        dtoProperties.put("templatePath", "templates/dto.ftl");
        dtoProperties.put("mapperSuffix", "Dto");
        dtoProperties.put("swagger", String.valueOf(swagger));
        dtoProperties.put("fileName", "${tableClass.shortClassName}${mapperSuffix}.java");
        context.addPluginConfiguration(getPluginConfiguration(filePlugin, dtoProperties));
    }

    private void addServicePluginConfigurationToContext(Context context) {
        // Service Dto代码生成插件
        String filePlugin = "tk.mybatis.mapper.generator.TemplateFilePlugin";
        // Service
        Map<String, String> serviceProperties = new HashMap<>();
        serviceProperties.put("targetProject", targetProject);
        serviceProperties.put("targetPackage", basePackage + ".service");
        serviceProperties.put("templatePath", "templates/service.ftl");
        serviceProperties.put("mapperSuffix", "Service");
        serviceProperties.put("fileName", "${tableClass.shortClassName}${mapperSuffix}.java");
        context.addPluginConfiguration(getPluginConfiguration(filePlugin, serviceProperties));
        // ServiceImpl
        Map<String, String> serviceImplProperties = new HashMap<>();
        serviceImplProperties.put("targetProject", targetProject);
        serviceImplProperties.put("targetPackage", basePackage + ".service.impl");
        serviceImplProperties.put("templatePath", "templates/serviceImpl.ftl");
        serviceImplProperties.put("mapperSuffix", "ServiceImpl");
        serviceImplProperties.put("fileName", "${tableClass.shortClassName}${mapperSuffix}.java");
        context.addPluginConfiguration(getPluginConfiguration(filePlugin, serviceImplProperties));
    }

    private void addControllerPluginConfigurationToContext(Context context) {
        // Service Dto代码生成插件
        String filePlugin = "tk.mybatis.mapper.generator.TemplateFilePlugin";
        // Controller
        Map<String, String> controllerProperties = new HashMap<>();
        controllerProperties.put("targetProject", targetProject);
        controllerProperties.put("targetPackage", basePackage + ".controller");
        controllerProperties.put("templatePath", "templates/controller.ftl");
        controllerProperties.put("mapperSuffix", "Controller");
        controllerProperties.put("swagger", String.valueOf(swagger));
        controllerProperties.put("fileName", "${tableClass.shortClassName}${mapperSuffix}.java");
        context.addPluginConfiguration(getPluginConfiguration(filePlugin, controllerProperties));

    }

    private void addPluginConfigurationToContext(Context context) {
        // SerializablePlugin
        String serializablePlugin = "org.mybatis.generator.plugins.SerializablePlugin";
        context.addPluginConfiguration(getPluginConfiguration(serializablePlugin, null));
        // tk.mybatis.mapper.generator.MapperPlugin
        String mapperPlugin = "tk.mybatis.mapper.generator.MapperPlugin";
        Map<String, String> properties = new HashMap<>();
        properties.put("mappers", xMapper);
        properties.put("caseSensitive", "true");
        properties.put("lombok", "Data");
        properties.put("generateColumnConsts", "true");
        context.addPluginConfiguration(getPluginConfiguration(mapperPlugin, properties));
    }

    private PluginConfiguration getPluginConfiguration(String type, Map<String, String> properties) {
        PluginConfiguration pluginConfiguration = new PluginConfiguration();
        pluginConfiguration.setConfigurationType(type);
        if (properties != null && !properties.isEmpty()) {
            Set<String> keySet = properties.keySet();
            for (String key : keySet) {
                pluginConfiguration.addProperty(key, properties.get(key));
            }
        }
        return pluginConfiguration;
    }

    private JDBCConnectionConfiguration getJdbcConnectionConfiguration() {
        JDBCConnectionConfiguration connectionConfiguration = new JDBCConnectionConfiguration();
        connectionConfiguration.setDriverClass(jdbcDriver);
        connectionConfiguration.setConnectionURL(jdbcURL);
        connectionConfiguration.setUserId(jdbcUserId);
        connectionConfiguration.setPassword(jdbcPassword);
        return connectionConfiguration;
    }

}
