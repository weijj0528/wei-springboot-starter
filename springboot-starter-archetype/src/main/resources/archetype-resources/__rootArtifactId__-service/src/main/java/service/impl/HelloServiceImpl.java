#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ${package}.dto.HelloDTO;
import ${package}.mapper.HelloMapper;
import ${package}.entity.Hello;
import ${package}.service.HelloService;
import com.wei.starter.base.bean.Page;
import com.wei.starter.base.util.WeiBeanUtil;
import com.wei.starter.mybatis.service.AbstractService;
import com.wei.starter.mybatis.xmapper.XMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

/**
 * (Hello)表服务实现类
 *
 * @author EasyCode
 * @since 2024-06-28 21:48:25
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
    public Long save(HelloDTO dto) {
        Hello hello = WeiBeanUtil.toBean(dto, Hello.class);
        insertSelective(hello);
        return hello.getId();
    }

    @Override
    public HelloDTO saveAndGet(HelloDTO dto) {
        Long id = save(dto);
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
        Function<HelloDTO, Hello> mapper = o -> WeiBeanUtil.toBean(o, Hello.class);
        QueryWrapper<Hello> wrapper = new QueryWrapper<>(mapper.apply(queryDto));
        wrapper.checkSqlInjection();
        //  其他查询条件组装
        Page<Hello> convert = selectPageByExample(wrapper, page.convert(mapper));
        page = convert.convert(o -> WeiBeanUtil.toBean(o, HelloDTO.class));
        return page.getList();
    }
}
