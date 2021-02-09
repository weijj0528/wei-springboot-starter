package ${package};

import com.wei.starter.base.valid.Add;
<#if swagger?? && swagger == "true">
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
</#if>
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
<#assign dateTime = .now>
/**
 * @author
 * @createTime ${dateTime?string["yyyy-MM-dd HH:mm:ss"]}
 * @description
 */
@Data
<#if swagger?? && swagger == "true">
@ApiModel(description = "${tableClass.introspectedTable.remarks}")
</#if>
public class ${tableClass.shortClassName}${props['mapperSuffix']} implements Serializable {

    private static final long serialVersionUID = 1L;

<#list tableClass.allFields as field>
    /**
     * ${field.remarks}
     */
    @NotNull(groups = Add.class, message = "${field.remarks}不能为空")
    <#if swagger?? && swagger == "true">
    @ApiModelProperty("${field.remarks}")
    </#if>
    private ${field.shortTypeName} ${field.fieldName};

</#list>
}
