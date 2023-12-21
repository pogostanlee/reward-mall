package com.rewardmall.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rewardmall.mapper.CustomerMapper;
import com.rewardmall.pojo.Customer;
import com.rewardmall.pojo.VO.CustomerQueryVO;
import com.rewardmall.service.CuntomerService;
import com.rewardmall.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class CustomerServiceImpl implements CuntomerService {
    @Autowired
    private CustomerMapper customerMapper;
    //获取所有用户信息
    @Override
    public Page<Customer> list(CustomerQueryVO customerQueryVO) {
        //Page<Customer> page = new Page<>(customerQueryVO.getCurrentPage(), customerQueryVO.getPageSize());
        Page<Customer> page = new Page<>(customerQueryVO.getCurrentPage(), customerQueryVO.getPageSize());

        //增加查询条件（支行查询时增加支行id）
        HashMap<String,Object> claims = ThreadLocalUtil.get();
        customerQueryVO.setBranchId((Integer) claims.get("number"));
        //获取前端传递数据进行查询
        Page<Customer> pageInfo =customerMapper.selectList(page,customerQueryVO);
        return pageInfo;
    }
    //添加新用户
    @Override
    public void add(Customer customer) {
        //封装支行id
        HashMap<String,Object> claims = ThreadLocalUtil.get();

        customer.setBranchId((Integer) claims.get("number"));

        customerMapper.add(customer);
    }
}
