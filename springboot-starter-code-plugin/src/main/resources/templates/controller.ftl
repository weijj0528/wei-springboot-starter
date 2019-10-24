package ${package};

import com.wei.springboot.starter.bean.Page;
import com.wei.springboot.starter.bean.Result;
import com.wei.springboot.starter.valid.Add;
import com.wei.springboot.starter.valid.Update;
import ${package?replace(".controller","")}.dto.${tableClass.shortClassName}Dto;
import ${package?replace(".controller","")}.service.${tableClass.shortClassName}Service;
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
@Controller
@RequestMapping("/${tableClass.variableName}")
public class ${tableClass.shortClassName}${props['mapperSuffix']} {

    @Autowired
    private ${tableClass.shortClassName}Service ${tableClass.variableName}Service;

    @ResponseBody
    @PostMapping
    public Result save(@RequestBody @Validated(Add.class) ${tableClass.shortClassName}Dto saveDto) {
        ${tableClass.variableName}Service.save(saveDto);
        return Result.success();
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        ${tableClass.variableName}Service.delete(id);
        return Result.success();
    }

    @ResponseBody
    @PutMapping("/{id}")
    public Result update(@PathVariable Long id, @RequestBody @Validated(Update.class) ${tableClass.shortClassName}Dto updateDto) {
        updateDto.setId(id);
        ${tableClass.variableName}Service.update(updateDto);
        return Result.success();
    }

    @ResponseBody
    @GetMapping("/{id}")
    public Result details(@PathVariable Long id) {
        ${tableClass.shortClassName}Dto details = ${tableClass.variableName}Service.details(id);
        return Result.success(details);
    }

    @ResponseBody
    @GetMapping
    public Result list(@RequestParam ${tableClass.shortClassName}Dto ${tableClass.variableName}Dto, @RequestParam Page page) {
        ${tableClass.variableName}Service.list(${tableClass.variableName}Dto, page);
        return Result.success(page);
    }

}
