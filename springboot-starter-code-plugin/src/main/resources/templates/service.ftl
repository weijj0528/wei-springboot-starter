package ${package};

import com.wei.springboot.starter.bean.Page;
import com.wei.springboot.starter.service.BaseService;
import ${package?replace(".service","")}.dto.${tableClass.shortClassName}Dto;
import ${tableClass.fullClassName};

import java.util.List;

<#assign dateTime = .now>
/**
 * @author
 * @createTime ${dateTime?string["yyyy-MM-dd HH:mm:ss"]}
 * @description
 */
public interface ${tableClass.shortClassName}${props['mapperSuffix']} extends BaseService<${tableClass.shortClassName}> {

    /**
     * Save int.
     *
     * @param dto the ${tableClass.variableName} dto
     * @return the int
     */
    int save(${tableClass.shortClassName}Dto dto);

    /**
     * Delete int.
     *
     * @param id the id
     * @return the int
     */
    int delete(Object id);

    /**
     * Update int.
     *
     * @param dto the ${tableClass.variableName} dto
     * @return the int
     */
    int update(${tableClass.shortClassName}Dto dto);

    /**
     * Details
     *
     * @param id the id
     * @return the
     */
    ${tableClass.shortClassName}Dto details(Object id);

    /**
     * List list.
     *
     * @return the list
     * @param queryDto
     * @param page
     */
    List<${tableClass.shortClassName}Dto> list(${tableClass.shortClassName}Dto queryDto, Page page);

}