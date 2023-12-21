package com.rewardmall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rewardmall.pojo.Customer;
import com.rewardmall.pojo.VO.CustomerQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {
    //查询用户信息
    Page<Customer> selectList(Page<Customer> page, @Param("customerQueryVO") CustomerQueryVO customerQueryVO);

    //添加用户
    void add(@Param("customer") Customer customer);
    //根据身份证查询用户
    Customer selectByIdNumber(@Param("idNumber") String idNumber);
}
