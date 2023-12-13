package com.rewardmall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rewardmall.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
//    根据账号查询用户信息
    User fingByUserName(String number);

    void updatePwd(Integer id, String pass);
}
