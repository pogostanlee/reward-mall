package com.rewardmall.service;

import com.rewardmall.pojo.User;


public interface UserService {
    User fingByUserName(String number);

    void updatePwd(Integer id, String pass);
}
