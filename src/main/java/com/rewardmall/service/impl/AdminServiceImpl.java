package com.rewardmall.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rewardmall.mapper.CustomerMapper;
import com.rewardmall.mapper.InboundRecordMapper;
import com.rewardmall.mapper.InventoryMapper;
import com.rewardmall.pojo.Customer;
import com.rewardmall.pojo.InboundRecord;
import com.rewardmall.pojo.Inventory;
import com.rewardmall.pojo.Result;
import com.rewardmall.service.AdminService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private InventoryMapper inventoryMapper;
    @Autowired
    private InboundRecordMapper inboundRecordMapper;
    @Autowired
    private CustomerMapper customerMapper;

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

    //导出excel
    @Override
    public void exportCustomer(HttpServletResponse response) {
        try {
            //设置excel下载响应头
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename=customer.xlsx");

            //获取所有用户信息
            List<Customer> customers = customerMapper.selectAll();
            //创建excel
            EasyExcel.write(response.getOutputStream(), Customer.class).sheet("用户信息").doWrite(customers);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
