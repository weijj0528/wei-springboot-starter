#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wei.starter.base.bean.Page;
import com.wei.starter.base.util.WeiBeanUtil;
import com.wei.starter.mybatis.service.AbstractService;
import com.wei.starter.mybatis.xmapper.XMapper;
import ${package}.dto.HelloDTO;
import ${package}.mapper.HelloMapper;
import ${package}.model.Hello;
import ${package}.service.HelloService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
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

    @Override
    public int save(HelloDTO dto) {
        Hello hello = WeiBeanUtil.toBean(dto, Hello.class);
        return insertSelective(hello);
    }

    @Override
    public HelloDTO saveAndGet(HelloDTO dto) {
        int id = save(dto);
        return details(id);
    }

    @Override
    public int delete(Serializable id) {
        return deleteByPrimaryKey(id);
    }

    @Override
    public HelloDTO deleteAndGet(Serializable id) {
        HelloDTO details = details(id);
        delete(id);
        return details;
    }

    @Override
    public int update(HelloDTO dto) {
        Hello hello = WeiBeanUtil.toBean(dto, Hello.class);
        return updateByPrimaryKeySelective(hello);
    }

    @Override
    public HelloDTO updateAndGet(HelloDTO dto) {
        update(dto);
        return details(dto.getId());
    }

    @Override
    public HelloDTO details(Serializable id) {
        Hello hello = selectByPrimaryKey(id);
        return WeiBeanUtil.toBean(hello, HelloDTO.class);
    }

    @Override
    public List<HelloDTO> list(HelloDTO queryDto, Page<HelloDTO> page) {
        QueryWrapper<Hello> example = new QueryWrapper(Hello.class);
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
