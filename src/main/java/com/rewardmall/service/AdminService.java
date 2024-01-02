package com.rewardmall.service;

import com.rewardmall.pojo.Deposit;
import com.rewardmall.pojo.InboundRecord;
import com.rewardmall.pojo.PageBean;
import com.rewardmall.pojo.Result;
import com.rewardmall.pojo.VO.DepositQueryVO;
import com.rewardmall.pojo.VO.InboundQueryVO;
import jakarta.servlet.http.HttpServletResponse;

public interface AdminService {
    //添加库存
    Result<String> addInventory(Integer productId, Integer branchId, Integer quantity);
    //全量导出
    void exportCustomer(HttpServletResponse response);
    //查询所有存款信息
    PageBean<Deposit> getAllDeposit(DepositQueryVO depositQueryVO, Long currentPage, Long pageSize);
    //导出所有存款excel
    void exportDeposit(HttpServletResponse response, DepositQueryVO depositQueryVO);
    //获取所有入库记录
    PageBean<InboundRecord> getInboundList(InboundQueryVO inboundQueryVO, Long currentPage, Long pageSize);
}
