package com.rewardmall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rewardmall.mapper.CustomerMapper;
import com.rewardmall.pojo.Customer;
import com.rewardmall.pojo.PageBean;
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
    //根据身份证查询用户信息
    @Override
    public Customer selectByIdNumber(String idNumber) {
        //获取前端传递数据进行查询
        Customer customer = customerMapper.selectByIdNumber(idNumber);
        return customer;
    }
    //获取所有用户信息(admin)
    @Override
    public PageBean<Customer> adminList(CustomerQueryVO customerQueryVO) {
        Page<Customer> page = new Page<>(customerQueryVO.getCurrentPage(), customerQueryVO.getPageSize());
        //获取前端传递数据进行查询
        QueryWrapper<Customer> customerQueryWrapper = new QueryWrapper<>();
        //增加查询条件（支行查询时增加支行id）
        if (customerQueryVO.getBranchId()!=null){
            customerQueryWrapper.eq("branchId",customerQueryVO.getBranchId());
        }
        if (customerQueryVO.getName()!=null){
            customerQueryWrapper.like("name",customerQueryVO.getName());
        }
        if (customerQueryVO.getIdNumber()!=null){
            customerQueryWrapper.like("idNumber",customerQueryVO.getIdNumber());
        }
        if (customerQueryVO.getManager()!=null){
            customerQueryWrapper.like("manager",customerQueryVO.getManager());
        }
        if(customerQueryVO.getStartNumber()!=null&&customerQueryVO.getEndNumber()!=null){
            customerQueryWrapper.between("totalDeposit",customerQueryVO.getStartNumber(),customerQueryVO.getEndNumber());
        }
       Page<Customer> pageInfo =customerMapper.selectPage(page,customerQueryWrapper);
        //封装返回数据
        PageBean<Customer> pageBean = new PageBean<>();
        pageBean.setTotal(pageInfo.getTotal());
        pageBean.setItems(pageInfo.getRecords());
        return pageBean;
    }
}
