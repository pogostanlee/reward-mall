package com.rewardmall.service;

import com.rewardmall.pojo.Result;

public interface AdminService {
    //添加库存
    Result<String> addInventory(Integer productId, Integer branchId, Integer quantity);
}
