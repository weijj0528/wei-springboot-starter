#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.service;

import ${package}.dto.HelloDTO;
import ${package}.model.Hello;
import com.wei.starter.base.bean.Page;
import com.wei.starter.mybatis.service.BaseService;

import java.io.Serializable;
import java.util.List;

/**
 * (Hello)表服务接口
 *
 * @author EasyCode
 * @since 2024-06-26 11:50:55
 */
public interface HelloService extends BaseService<Hello> {

    /**
     * Save int.
     *
     * @param dto the Hello dto
     * @return the int
     */
    int save(HelloDTO dto);

    /**
     * 新增保存并返回新增记录
     *
     * @param dto the dto
     * @return the Hello dto
     */
    HelloDTO saveAndGet(HelloDTO dto);

    /**
     * Delete int.
     *
     * @param id the id
     * @return the int
     */
    int delete(Serializable id);

    /**
     * 按主键删除并返回删除的记录
     *
     * @param id the id
     * @return the Hello dto
     */
    HelloDTO deleteAndGet(Serializable id);

    /**
     * Update int.
     *
     * @param dto the Hello dto
     * @return the int
     */
    int update(HelloDTO dto);

    /**
     * 按主键更新并返回更新记录
     *
     * @param dto the dto
     * @return the Hello dto
     */
    HelloDTO updateAndGet(HelloDTO dto);

    /**
     * Details
     *
     * @param id the id
     * @return the
     */
    HelloDTO details(Serializable id);

    /**
     * List.
     *
     * @param queryDto query
     * @param page     page
     * @return the list
     */
    List<HelloDTO> list(HelloDTO queryDto, Page<HelloDTO> page);

}
