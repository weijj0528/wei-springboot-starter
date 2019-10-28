package ${package};

import com.wei.springboot.starter.bean.Page;
import com.wei.springboot.starter.service.AbstractService;
import ${package?replace(".service.impl","")}.dto.${tableClass.shortClassName}Dto;
import ${tableClass.fullClassName};
import ${package?replace("impl",tableClass.shortClassName+"Service")};
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

<#assign dateTime = .now>
/**
 * @author
 * @createTime ${dateTime?string["yyyy-MM-dd HH:mm:ss"]}
 * @description
 */
@Service
public class ${tableClass.shortClassName}${props['mapperSuffix']} extends AbstractService<${tableClass.shortClassName}> implements ${tableClass.shortClassName}Service {

    /**
     * Save int.
     *
     * @param dto
     * @return the int
     */
    @Override
    public int save(${tableClass.shortClassName}Dto dto) {
        ${tableClass.shortClassName} ${tableClass.variableName} = dto.toModel();
        return mapper.insertSelective(${tableClass.variableName});
    }

    /**
     * Delete int.
     *
     * @param id
     * @return the int
     */
    @Override
    public int delete(Object id) {
        return mapper.deleteByPrimaryKey(id);
    }

    /**
     * Update int.
     *
     * @param dto
     * @return the int
     */
    @Override
    public int update(${tableClass.shortClassName}Dto dto) {
        ${tableClass.shortClassName} ${tableClass.variableName} = dto.toModel();
        return mapper.updateByPrimaryKeySelective(${tableClass.variableName});
    }

    /**
     * Details ${tableClass.variableName} dto.
     *
     * @return the ${tableClass.variableName} dto
     */
    @Override
    public ${tableClass.shortClassName}Dto details(Object id) {
        ${tableClass.shortClassName} ${tableClass.variableName} = mapper.selectByPrimaryKey(id);
        ${tableClass.shortClassName}Dto ${tableClass.variableName}Dto = new ${tableClass.shortClassName}Dto();
        ${tableClass.variableName}Dto.copyModel(${tableClass.variableName});
        return ${tableClass.variableName}Dto;
    }

    /**
     * List list.
     *
     * @param queryDto
     * @param page
     * @return the list
     */
    @Override
    public List<${tableClass.shortClassName}Dto> list(${tableClass.shortClassName}Dto queryDto, Page page) {
        Example example = new Example(${tableClass.shortClassName}.class);
        Example.Criteria criteria = example.createCriteria();
        selectPageByExample(example, page);
        return page.getList();
    }
}
