package ${package};

import com.wei.starter.base.bean.Page;
import com.wei.starter.base.bean.Result;
import com.wei.starter.base.valid.Add;
import com.wei.starter.base.valid.Update;
import ${package?replace(".controller","")}.dto.${tableClass.shortClassName}Dto;
import ${package?replace(".controller","")}.service.${tableClass.shortClassName}Service;
<#if swagger?? && swagger == "true">
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
</#if>
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

<#assign dateTime = .now>
/**
 * @author
 * @createTime ${dateTime?string["yyyy-MM-dd HH:mm:ss"]}
 * @description
 */
<#if swagger?? && swagger == "true">
@Api(tags = "${tableClass.introspectedTable.remarks}(${tableClass.shortClassName})前端控制器")
</#if>
@Controller
@RequestMapping("/${tableClass.variableName}")
public class ${tableClass.shortClassName}${props['mapperSuffix']} {

    @Autowired
    private ${tableClass.shortClassName}Service ${tableClass.variableName}Service;

<#if swagger?? && swagger == "true">
    @ApiOperation(value = "Save", notes = "${tableClass.introspectedTable.remarks}新增")
</#if>
    @ResponseBody
    @PostMapping
    public Result<Void> save(@RequestBody @Validated(Add.class) ${tableClass.shortClassName}Dto saveDto) {
        ${tableClass.variableName}Service.save(saveDto);
        return Result.success();
    }

<#if swagger?? && swagger == "true">
    @ApiOperation(value = "Delete", notes = "${tableClass.introspectedTable.remarks}删除")
</#if>
    @ResponseBody
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        ${tableClass.variableName}Service.delete(id);
        return Result.success();
    }

<#if swagger?? && swagger == "true">
    @ApiOperation(value = "Update", notes = "${tableClass.introspectedTable.remarks}更新")
</#if>
    @ResponseBody
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody @Validated(Update.class) ${tableClass.shortClassName}Dto updateDto) {
        updateDto.setId(id);
        ${tableClass.variableName}Service.update(updateDto);
        return Result.success();
    }

<#if swagger?? && swagger == "true">
    @ApiOperation(value = "Details", notes = "${tableClass.introspectedTable.remarks}详情查询")
</#if>
    @ResponseBody
    @GetMapping("/{id}")
    public Result<${tableClass.shortClassName}Dto> details(@PathVariable Long id) {
        ${tableClass.shortClassName}Dto details = ${tableClass.variableName}Service.details(id);
        return Result.success(details);
    }

<#if swagger?? && swagger == "true">
    @ApiOperation(value = "Page", notes = "${tableClass.introspectedTable.remarks}分页查询")
</#if>
    @ResponseBody
    @GetMapping
    public Result<Page<${tableClass.shortClassName}Dto>> page(${tableClass.shortClassName}Dto queryDto, Page<${tableClass.shortClassName}Dto> page) {
        ${tableClass.variableName}Service.list(queryDto, page);
        return Result.success(page);
    }

}
