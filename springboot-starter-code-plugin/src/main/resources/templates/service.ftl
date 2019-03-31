package ${package};

import com.wei.springboot.starter.service.BaseService;
import ${tableClass.fullClassName};


<#assign dateTime = .now>
/**
 * @author
 * @createTime ${dateTime?string["yyyy-MM-dd HH:mm:ss"]}
 * @description
 */
public interface ${tableClass.shortClassName}${props['mapperSuffix']} extends BaseService<${tableClass.shortClassName}> {

}
