package ${package};

import com.wei.springboot.starter.dto.BaseDto;
import ${tableClass.fullClassName};
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
<#assign dateTime = .now>
/**
 * @author
 * @createTime ${dateTime?string["yyyy-MM-dd HH:mm:ss"]}
 * @description
 */
@Data
public class ${tableClass.shortClassName}${props['mapperSuffix']} extends BaseDto<${tableClass.shortClassName}> implements Serializable {


<#list tableClass.allFields as field>
    /**
     * ${field.remarks}
     */
    private ${field.shortTypeName} ${field.fieldName};

</#list>
}
