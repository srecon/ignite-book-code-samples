package com.blu.imdg.dao;

import com.blu.imdg.dto.Employee;
import com.blu.imdg.mapper.UserMapper;
import org.springframework.cache.annotation.Cacheable;

/**
 * Created by shamim on 16/02/16.
 */
public class UserServices {
    private UserMapper userMapper;

    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public Employee getEmploee (String ename){return userMapper.getEmploee(ename);}
}
