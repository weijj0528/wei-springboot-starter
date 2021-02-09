package ${package};

import com.wei.starter.base.bean.Page;
import com.wei.starter.base.util.WeiBeanUtil;
import com.wei.starter.mybatis.xmapper.XMapper;
import com.wei.starter.mybatis.service.AbstractService;
import ${package?replace(".service.impl","")}.dto.${tableClass.shortClassName}Dto;
import ${package?replace(".service.impl","")}.mapper.${tableClass.shortClassName}Mapper;
import ${tableClass.fullClassName};
import ${package?replace("impl",tableClass.shortClassName+"Service")};
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

<#assign dateTime = .now>
/**
 * @author
 * @createTime ${dateTime?string["yyyy-MM-dd HH:mm:ss"]}
 * @description
 */
@Service
public class ${tableClass.shortClassName}${props['mapperSuffix']} extends AbstractService<${tableClass.shortClassName}> implements ${tableClass.shortClassName}Service {

    @Resource
    private ${tableClass.shortClassName}Mapper ${tableClass.variableName}Mapper;

    @Override
    public XMapper<${tableClass.shortClassName}> getMapper() {
        return ${tableClass.variableName}Mapper;
    }

    /**
     * Save int.
     *
     * @param dto
     * @return the int
     */
    @Override
    public int save(${tableClass.shortClassName}Dto dto) {
        ${tableClass.shortClassName} ${tableClass.variableName} = WeiBeanUtil.toBean(dto, ${tableClass.shortClassName}.class);
        return getMapper().insertSelective(${tableClass.variableName});
    }

    /**
     * Delete int.
     *
     * @param id
     * @return the int
     */
    @Override
    public int delete(Object id) {
        return getMapper().deleteByPrimaryKey(id);
    }

    /**
     * Update int.
     *
     * @param dto
     * @return the int
     */
    @Override
    public int update(${tableClass.shortClassName}Dto dto) {
        ${tableClass.shortClassName} ${tableClass.variableName} = WeiBeanUtil.toBean(dto, ${tableClass.shortClassName}.class);
        return getMapper().updateByPrimaryKeySelective(${tableClass.variableName});
    }

    /**
     * Details ${tableClass.variableName} dto.
     *
     * @return the ${tableClass.variableName} dto
     */
    @Override
    public ${tableClass.shortClassName}Dto details(Object id) {
        ${tableClass.shortClassName} ${tableClass.variableName} = getMapper().selectByPrimaryKey(id);
        return WeiBeanUtil.toBean(${tableClass.variableName}, ${tableClass.shortClassName}Dto.class);
    }

    /**
     * List list.
     *
     * @param queryDto
     * @param page
     * @return the list
     */
    @Override
    public List<${tableClass.shortClassName}Dto> list(${tableClass.shortClassName}Dto queryDto, Page<${tableClass.shortClassName}Dto> page) {
        Example example = new Example(${tableClass.shortClassName}.class);
        Example.Criteria criteria = example.createCriteria();
        // TODO 查询条件组装
        Page<${tableClass.shortClassName}> ${tableClass.variableName}Page = new Page<>();
        ${tableClass.variableName}Page.setPage(page.getPage());
        ${tableClass.variableName}Page.setSize(page.getSize());
        selectPageByExample(example, ${tableClass.variableName}Page);
        List<${tableClass.shortClassName}Dto> ${tableClass.variableName}DtoList = WeiBeanUtil.toList(${tableClass.variableName}Page.getList(), ${tableClass.shortClassName}Dto.class);
        page.setTotal(${tableClass.variableName}Page.getTotal());
        page.setList(${tableClass.variableName}DtoList);
        return ${tableClass.variableName}DtoList;
    }
}
