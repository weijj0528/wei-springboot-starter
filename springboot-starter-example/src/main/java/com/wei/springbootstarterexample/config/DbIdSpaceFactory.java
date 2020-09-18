package com.wei.springbootstarterexample.config;

import com.wei.starter.sequence.incr.Space;
import com.wei.starter.sequence.incr.SpaceFactory;
import com.wei.springbootstarterexample.model.SysId;
import com.wei.springbootstarterexample.service.SysIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @author William.Wei
 */
@Configuration
public class DbIdSpaceFactory extends SpaceFactory {

    @Autowired
    private SysIdService sysIdService;

    @Override
    public Space createSpace(String sysName, String bizKey) {
        SysId sysId = sysIdService.queryBySysNameAndBizType(sysName, bizKey);
        if (sysId == null) {
            sysId = sysIdService.addNewComId(sysName, bizKey);
        }
        Long start = sysId.getNextStart();
        Long end = sysId.getNextStart() + sysId.getStep();
        sysId.setNextStart(end);
        sysIdService.updateByPrimaryKeySelective(sysId);
        Space space = new Space(start, end);
        return space;
    }
}
