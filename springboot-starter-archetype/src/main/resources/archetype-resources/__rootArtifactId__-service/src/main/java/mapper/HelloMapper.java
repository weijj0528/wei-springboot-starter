#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.mapper;


import ${package}.entity.Hello;
import com.wei.starter.mybatis.xmapper.XMapper;

/**
 * (Hello)表数据库访问层
 *
 * @author EasyCode
 * @since 2024-06-26 11:50:54
 */
public interface HelloMapper extends XMapper<Hello> {

}

