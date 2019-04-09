package ${package};

import com.wei.springboot.starter.service.AbstractService;
import ${tableClass.fullClassName};
import ${package?replace("impl",tableClass.shortClassName+"Service")};
import org.springframework.stereotype.Service;

<#assign dateTime = .now>
/**
 * @author
 * @createTime ${dateTime?string["yyyy-MM-dd HH:mm:ss"]}
 * @description
 */
@Service
public class ${tableClass.shortClassName}${props['mapperSuffix']} extends AbstractService<${tableClass.shortClassName}> implements ${tableClass.shortClassName}Service {

}
