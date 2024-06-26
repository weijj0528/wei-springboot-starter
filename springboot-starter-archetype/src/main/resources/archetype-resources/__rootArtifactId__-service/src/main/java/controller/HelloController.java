#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.controller;

import ${package}.dto.HelloDTO;
import ${package}.service.HelloService;
import com.wei.starter.base.bean.Page;
import com.wei.starter.base.bean.Result;
import com.wei.starter.base.valid.Add;
import com.wei.starter.base.valid.Update;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (Hello)表控制层
 *
 * @author EasyCode
 * @since 2024-06-26 11:54:31
 */
@RestController
@RequestMapping("/hello")
public class HelloController {

    @Resource
    private HelloService helloService;

    /**
     * 新增保存
     *
     * @param saveDto the save dto
     * @return the result
     */
    @PostMapping
    public Result<Void> save(@RequestBody @Validated(Add.class) HelloDTO saveDto) {
        helloService.save(saveDto);
        return Result.success();
    }

    /**
     * 删除
     *
     * @param id the id
     * @return the result
     */
    @PostMapping("/del/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        helloService.delete(id);
        return Result.success();
    }

    /**
     * 更新修改
     *
     * @param id        the id
     * @param updateDto the update dto
     * @return the result
     */
    @PostMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody @Validated(Update.class) HelloDTO updateDto) {
        updateDto.setId(id);
        helloService.update(updateDto);
        return Result.success();
    }

    /**
     * 详情查询
     *
     * @param id the id
     * @return the result
     */
    @GetMapping("/{id}")
    public Result<HelloDTO> details(@PathVariable Long id) {
        HelloDTO details = helloService.details(id);
        return Result.success(details);
    }

    /**
     * 分页查询
     *
     * @param queryDto the query dto
     * @param page     the page
     * @return the result
     */
    @GetMapping
    public Result<Page<HelloDTO>> page(HelloDTO queryDto, Page<HelloDTO> page) {
        helloService.list(queryDto, page);
        return Result.success(page);
    }

}
