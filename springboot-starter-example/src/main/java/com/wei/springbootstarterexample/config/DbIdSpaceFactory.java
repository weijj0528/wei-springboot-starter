package com.wei.springbootstarterexample.config;

import com.wei.springboot.starter.sequence.incr.Space;
import com.wei.springboot.starter.sequence.incr.SpaceFactory;
import com.wei.springbootstarterexample.model.ComId;
import com.wei.springbootstarterexample.service.ComIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @author William.Wei
 */
@Configuration
public class DbIdSpaceFactory extends SpaceFactory {

    @Autowired
    ComIdService comIdService;

    @Override
    public Space createSpace(String sysName, String bizKey) {
        ComId comId = comIdService.queryBySysNameAndBizType(sysName, bizKey);
        if (comId == null) {
            comId = comIdService.addNewComId(sysName, bizKey);
        }
        Long start = comId.getNextStart();
        Long end = comId.getNextStart() + comId.getStep();
        comId.setNextStart(end);
        comIdService.updateByPrimaryKeySelective(comId);
        Space space = new Space(start, end);
        return space;
    }
}
