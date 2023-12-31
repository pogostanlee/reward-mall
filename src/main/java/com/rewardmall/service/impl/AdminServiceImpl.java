package com.rewardmall.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rewardmall.mapper.CustomerMapper;
import com.rewardmall.mapper.DepositMapper;
import com.rewardmall.mapper.InboundRecordMapper;
import com.rewardmall.mapper.InventoryMapper;
import com.rewardmall.pojo.*;
import com.rewardmall.pojo.VO.DepositQueryVO;
import com.rewardmall.service.AdminService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
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
    @Autowired
    private DepositMapper depositMapper;

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

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("导出", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            //获取所有用户信息
            List<Customer> customers = customerMapper.selectAll();
            //创建excel
            EasyExcel.write(response.getOutputStream(), Customer.class).sheet("用户信息").doWrite(customers);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
    //查询所有存款信息
    @Override
    public PageBean<Deposit> getAllDeposit(DepositQueryVO depositQueryVO, Long currentPage, Long pageSize) {
        //封装page对象
        Page<Deposit> page = new Page<>(currentPage, pageSize);
        //判断depositQueryVO属性是否为空
        QueryWrapper<Deposit> depositQueryWrapper = new QueryWrapper<>();
        if (depositQueryVO.getBranchId() != null) {
            depositQueryWrapper.eq("branchId", depositQueryVO.getBranchId());
        }
        if (depositQueryVO.getIdNumber() != null&&!depositQueryVO.getIdNumber().equals("")) {
            depositQueryWrapper.eq("customerIdNumber", depositQueryVO.getIdNumber());
        }
        if (depositQueryVO.getStartNumber() != null&&depositQueryVO.getEndNumber() != null) {
            depositQueryWrapper.between("deposit", depositQueryVO.getStartNumber(), depositQueryVO.getEndNumber());
        }
        if (depositQueryVO.getIsNewDeposit() != null) {
            depositQueryWrapper.eq("isNewDeposit", depositQueryVO.getIsNewDeposit());
        }
        if (depositQueryVO.getReceptionist() != null&&!depositQueryVO.getReceptionist().equals("")) {
            depositQueryWrapper.eq("receptionist", depositQueryVO.getReceptionist());
        }
        if (depositQueryVO.getMonthDiff() != null) {
            depositQueryWrapper.eq("monthDiff", depositQueryVO.getMonthDiff());
        }
        if (depositQueryVO.getDate() != null && depositQueryVO.getDate().length == 2) {
            depositQueryWrapper.between("depositDate", depositQueryVO.getDate()[0], depositQueryVO.getDate()[1]);
        }
        //查询存款
        Page<Deposit> pageInfo = depositMapper.selectPage(page, depositQueryWrapper);
        //封装返回数据
        PageBean<Deposit> pageBean = new PageBean<>();
        pageBean.setTotal(pageInfo.getTotal());
        pageBean.setItems(pageInfo.getRecords());
        return pageBean;
    }
    //导出所有存款excel
    @Override
    public void exportDeposit(HttpServletResponse response, DepositQueryVO depositQueryVO) {
        try {

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("存款信息", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            //获取所有存款信息
            QueryWrapper<Deposit> depositQueryWrapper = new QueryWrapper<>();
            if (depositQueryVO.getBranchId() != null) {
                depositQueryWrapper.eq("branchId", depositQueryVO.getBranchId());
            }
            if (depositQueryVO.getIdNumber() != null&&!depositQueryVO.getIdNumber().equals("")) {
                depositQueryWrapper.eq("customerIdNumber", depositQueryVO.getIdNumber());
            }
            if (depositQueryVO.getStartNumber() != null&&depositQueryVO.getEndNumber() != null) {
                depositQueryWrapper.between("deposit", depositQueryVO.getStartNumber(), depositQueryVO.getEndNumber());
            }
            if (depositQueryVO.getIsNewDeposit() != null) {
                depositQueryWrapper.eq("isNewDeposit", depositQueryVO.getIsNewDeposit());
            }
            if (depositQueryVO.getReceptionist() != null&&!depositQueryVO.getReceptionist().equals("")) {
                depositQueryWrapper.eq("receptionist", depositQueryVO.getReceptionist());
            }
            if (depositQueryVO.getMonthDiff() != null) {
                depositQueryWrapper.eq("monthDiff", depositQueryVO.getMonthDiff());
            }
            if (depositQueryVO.getDate() != null && depositQueryVO.getDate().length == 2) {
                depositQueryWrapper.between("depositDate", depositQueryVO.getDate()[0], depositQueryVO.getDate()[1]);
            }
            //查询存款
            List<Deposit> deposits = depositMapper.selectList(depositQueryWrapper);
            //创建excel
            EasyExcel.write(response.getOutputStream(), Deposit.class).sheet("存款信息").doWrite(deposits);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
