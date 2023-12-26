package com.rewardmall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rewardmall.mapper.InboundRecordMapper;
import com.rewardmall.mapper.InventoryMapper;
import com.rewardmall.pojo.InboundRecord;
import com.rewardmall.pojo.Inventory;
import com.rewardmall.pojo.Result;
import com.rewardmall.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class AdminServiceImpl  implements AdminService {
    @Autowired
    private InventoryMapper inventoryMapper;
    @Autowired
    private InboundRecordMapper inboundRecordMapper;

    @Override
    @Transactional
    public Result<String> addInventory(Integer productId, Integer branchId, Integer quantity) {
        //判断库存是否存在
        QueryWrapper<Inventory> inventoryQueryWrapper = new QueryWrapper<>();
        inventoryQueryWrapper.eq("productid", productId);
        inventoryQueryWrapper.eq("branchid", branchId);
        Inventory inventory = inventoryMapper.selectOne(inventoryQueryWrapper);
        if (inventory != null) {
            //存在则更新库存
            inventory.setQuantity(inventory.getQuantity() + quantity);
            inventory.setTotal(inventory.getTotal() + quantity);
            inventoryMapper.updateById(inventory);
            //添加入库记录
            InboundRecord inboundRecord = new InboundRecord();
            inboundRecord.setProductId(productId);
            inboundRecord.setBranchId(branchId);
            inboundRecord.setQuantity(quantity);
            inboundRecord.setDate(new Date());
            inboundRecordMapper.insert(inboundRecord);
            return Result.success("更新库存成功");
        } else {
            //不存在则添加库存
            Inventory inventory1 = new Inventory();
            inventory1.setProductId(productId);
            inventory1.setBranchId(branchId);
            inventory1.setQuantity(quantity);
            inventory1.setTotal(quantity);
            inventoryMapper.insert(inventory1);
            //添加入库记录
            InboundRecord inboundRecord = new InboundRecord();
            inboundRecord.setProductId(productId);
            inboundRecord.setBranchId(branchId);
            inboundRecord.setQuantity(quantity);
            inboundRecord.setDate(new Date());
            inboundRecordMapper.insert(inboundRecord);
            return Result.success("添加库存成功");
        }
    }
}
