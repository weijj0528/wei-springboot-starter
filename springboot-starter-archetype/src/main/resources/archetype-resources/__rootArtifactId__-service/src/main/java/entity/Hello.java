#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wei.starter.mybatis.entity.Entity;

/**
 * (Hello)实体类
 *
 * @author EasyCode
 * @since 2024-06-28 21:45:19
 */
@TableName("HELLO")
public class Hello extends Entity<Long> {
    private static final long serialVersionUID = -63420559195978268L;

}

