#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.service;

import ${package}.dto.HelloDTO;
import ${package}.entity.Hello;
import com.wei.starter.base.bean.Page;
import com.wei.starter.mybatis.service.BaseService;

import java.io.Serializable;
import java.util.List;

/**
 * (Hello)表服务接口
 *
 * @author EasyCode
 * @since 2024-06-28 21:48:24
 */
public interface HelloService extends BaseService<Hello> {

    /**
     * 新增保存，返回保存后主键
     *
     * @param dto the Hello dto
     * @return the int
     */
    Long save(HelloDTO dto);

    /**
     * 新增保存并返回新增记录
     *
     * @param dto the dto
     * @return the Hello dto
     */
    HelloDTO saveAndGet(HelloDTO dto);

    /**
     * 按主键删除
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
     * 按主键更新
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
     * 按主键查询单条记录详情
     *
     * @param id the id
     * @return the
     */
    HelloDTO details(Serializable id);

    /**
     * 分页查询
     *
     * @param queryDto query
     * @param page     page
     * @return the list
     */
    List<HelloDTO> list(HelloDTO queryDto, Page<HelloDTO> page);

}
