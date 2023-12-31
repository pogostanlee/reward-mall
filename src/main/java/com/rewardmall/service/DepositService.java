package com.rewardmall.service;

import com.rewardmall.pojo.Deposit;
import com.rewardmall.pojo.PageBean;
import com.rewardmall.pojo.Result;
import com.rewardmall.pojo.VO.DepositQueryVO;
import com.rewardmall.pojo.VO.DepositVO;

public interface DepositService {
//    //根据身份证号查询存款
//    Page<Deposit> listByIdNumber(String idNumber, Long currentPage, Long pageSize);
    //添加存款
    Result<String> add(DepositVO depositVO);
    //删除存款记录
    Result<String> deleteId(Deposit deposit);
    //根据身份证号查询存款
    PageBean<Deposit> list(DepositQueryVO depositQueryVO, Long currentPage, Long pageSize);
}
