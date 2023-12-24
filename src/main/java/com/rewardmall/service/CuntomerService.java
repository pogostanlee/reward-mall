package com.rewardmall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rewardmall.pojo.Customer;
import com.rewardmall.pojo.VO.CustomerQueryVO;

public interface CuntomerService {
    //获取所有用户信息
    Page<Customer> list(CustomerQueryVO customerQueryVO);

    //添加新用户
    void add(Customer customer);

    //根据身份证查询用户信息
    Customer selectByIdNumber(String idNumber);
}
