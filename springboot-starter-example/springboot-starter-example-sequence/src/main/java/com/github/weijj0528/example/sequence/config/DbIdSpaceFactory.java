package com.github.weijj0528.example.sequence.config;

import com.github.weijj0528.example.sequence.model.SysId;
import com.github.weijj0528.example.sequence.service.SysIdService;
import com.wei.starter.sequence.incr.Space;
import com.wei.starter.sequence.incr.SpaceFactory;
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
