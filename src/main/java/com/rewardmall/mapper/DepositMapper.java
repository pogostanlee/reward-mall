package com.rewardmall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rewardmall.pojo.Customer;
import com.rewardmall.pojo.Deposit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DepositMapper extends BaseMapper<Deposit> {
    //根据身份证号查询存款
    Page<Deposit> listByIdNumber(Page<Deposit> page, @Param("idNumber") String idNumber, @Param("branchId") Integer branchId);
}
