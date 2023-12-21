package com.rewardmall.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rewardmall.pojo.Customer;
import com.rewardmall.pojo.PageBean;
import com.rewardmall.pojo.Result;
import com.rewardmall.pojo.VO.CustomerQueryVO;
import com.rewardmall.service.CuntomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/customer")
@RestController
public class CustomerController {
    @Autowired
    private CuntomerService customerService;
    //获取所有用户信息
    @RequestMapping("/list")
    public PageBean<Customer> list( CustomerQueryVO customerQueryVO) {
    //获取前端传递数据进行查询
        Page<Customer> pageInfo= customerService.list(customerQueryVO);
        //封装返回数据
        PageBean<Customer> pageBean = new PageBean<>();
        pageBean.setTotal(pageInfo.getTotal());
        pageBean.setItems(pageInfo.getRecords());
        return pageBean;
    }
    //添加新储户
    @RequestMapping("/add")
    public Result<String> add(@Validated Customer customer) {

        //获取前端传递数据进行添加
        customerService.add(customer);
        return Result.success("添加成功");
    }
    //查询储户存款信息 /deposit/depositList
}
