#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.controller.admin;

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
 * @since 2024-06-28 21:49:59
 */
@RestController
@RequestMapping("/admin/hello")
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
    public Result<HelloDTO> save(@RequestBody @Validated(Add.class) HelloDTO saveDto) {
        HelloDTO dto = helloService.saveAndGet(saveDto);
        return Result.success(dto);
    }

    /**
     * 按主键更新
     * PUT请求会报告安全问题，这里使用POST请求替代
     *
     * @param id        the id
     * @param updateDto the update dto
     * @return the result
     */
    @PostMapping("/{id}")
    public Result<HelloDTO> update(@PathVariable Long id, @RequestBody @Validated(Update.class) HelloDTO updateDto) {
        updateDto.setId(id);
        HelloDTO dto = helloService.updateAndGet(updateDto);
        return Result.success(dto);
    }

    /**
     * 按主键删除
     * DELETE请求会报告安全问题，这里使用POST请求替代
     *
     * @param id the id
     * @return the result
     */
    @PostMapping("/del/{id}")
    public Result<HelloDTO> delete(@PathVariable Long id) {
        HelloDTO dto = helloService.deleteAndGet(id);
        return Result.success(dto);
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
