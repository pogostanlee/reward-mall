package com.rewardmall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rewardmall.pojo.Deposit;
import com.rewardmall.pojo.Result;
import com.rewardmall.pojo.VO.DepositQueryVO;

public interface DepositService {
    //根据身份证号查询存款
    Page<Deposit> listByIdNumber(String idNumber, Long currentPage, Long pageSize);
    //添加存款
    Result<String> add(DepositQueryVO depositQueryVO);
}
