#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.service.impl;

import ${package}.dto.HelloDTO;
import ${package}.mapper.HelloMapper;
import ${package}.model.Hello;
import ${package}.service.HelloService;
import com.wei.starter.base.bean.Page;
import com.wei.starter.base.util.WeiBeanUtil;
import com.wei.starter.mybatis.service.AbstractService;
import com.wei.starter.mybatis.xmapper.XMapper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Hello)表服务实现类
 *
 * @author EasyCode
 * @since 2024-06-26 11:50:56
 */
@Service("helloService")
public class HelloServiceImpl extends AbstractService<Hello> implements HelloService {

    @Resource
    private HelloMapper helloMapper;

    @Override
    public XMapper<Hello> getMapper() {
        return helloMapper;
    }

    /**
     * Save int.
     *
     * @param dto
     * @return the int
     */
    @Override
    public int save(HelloDTO dto) {
        Hello hello = WeiBeanUtil.toBean(dto, Hello.class);
        return getMapper().insertSelective(hello);
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
    public int update(HelloDTO dto) {
        Hello hello = WeiBeanUtil.toBean(dto, Hello.class);
        return getMapper().updateByPrimaryKeySelective(hello);
    }

    /**
     * Details hello dto.
     *
     * @return the hello dto
     */
    @Override
    public HelloDTO details(Object id) {
        Hello hello = getMapper().selectByPrimaryKey(id);
        return WeiBeanUtil.toBean(hello, HelloDTO.class);
    }

    /**
     * List list.
     *
     * @param queryDto
     * @param page
     * @return the list
     */
    @Override
    public List<HelloDTO> list(HelloDTO queryDto, Page<HelloDTO> page) {
        Example example = new Example(Hello.class);
        Example.Criteria criteria = example.createCriteria();
        // TODO 查询条件组装
        Page<Hello> helloPage = new Page<>();
        helloPage.setPage(page.getPage());
        helloPage.setSize(page.getSize());
        selectPageByExample(example, helloPage);
        List<HelloDTO> helloDtoList = WeiBeanUtil.toList(helloPage.getList(), HelloDTO.class);
        page.setTotal(helloPage.getTotal());
        page.setList(helloDtoList);
        return helloDtoList;
    }
}
