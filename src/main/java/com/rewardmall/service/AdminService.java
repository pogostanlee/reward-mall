package com.rewardmall.service;

import com.rewardmall.pojo.Result;
import jakarta.servlet.http.HttpServletResponse;

public interface AdminService {
    //添加库存
    Result<String> addInventory(Integer productId, Integer branchId, Integer quantity);
    //全量导出
    void exportCustomer(HttpServletResponse response);
}
