package com.rewardmall.service.impl;

import com.rewardmall.mapper.UserMapper;
import com.rewardmall.pojo.User;
import com.rewardmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    //登录
    @Override
    public User fingByUserName(String number) {
        User user = userMapper.fingByUserName(number);
        return user;
    }
    //更改密码
    @Override
    public void updatePwd(Integer id, String pass) {

        userMapper.updatePwd(id,pass);
    }
}
