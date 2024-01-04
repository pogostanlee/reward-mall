package com.rewardmall.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rewardmall.mapper.*;
import com.rewardmall.pojo.*;
import com.rewardmall.pojo.VO.DepositQueryVO;
import com.rewardmall.pojo.VO.InboundQueryVO;
import com.rewardmall.service.AdminService;
import com.rewardmall.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
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
    @Autowired
    private ReboundRecordMapper reboundRecordMapper;


    @Override
    @Transactional
    public Result<String> addInventory(Integer productId, Integer branchId, Integer quantity) {
        //获取部门号
        HashMap<String, Object> cliams = ThreadLocalUtil.get();
        Integer departmentId = (Integer) cliams.get("number");

        //判断是否为总行添加库存
        if (!departmentId.equals(branchId)) {
            //根据部门号与商品id查询剩余商品总量
            QueryWrapper<Inventory> inventoryQueryWrapperAdmin = new QueryWrapper<>();
            inventoryQueryWrapperAdmin.eq("productid", productId);
            inventoryQueryWrapperAdmin.eq("branchid", departmentId);
            Inventory inventoryAdmin = inventoryMapper.selectOne(inventoryQueryWrapperAdmin);
            if (inventoryAdmin == null) {
                throw new RuntimeException("库存不存在");
            }
            //判断库存是否足够
            if (inventoryAdmin.getQuantity() < quantity) {
                throw new RuntimeException("库存不足");
            }
            //库存足够则减去库存
            inventoryAdmin.setQuantity(inventoryAdmin.getQuantity() - quantity);
            inventoryMapper.updateById(inventoryAdmin);
        }


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
        if (depositQueryVO.getIdNumber() != null && !depositQueryVO.getIdNumber().equals("")) {
            depositQueryWrapper.eq("customerIdNumber", depositQueryVO.getIdNumber());
        }
        if (depositQueryVO.getStartNumber() != null && depositQueryVO.getEndNumber() != null) {
            depositQueryWrapper.between("deposit", depositQueryVO.getStartNumber(), depositQueryVO.getEndNumber());
        }
        if (depositQueryVO.getIsNewDeposit() != null) {
            depositQueryWrapper.eq("isNewDeposit", depositQueryVO.getIsNewDeposit());
        }
        if (depositQueryVO.getReceptionist() != null && !depositQueryVO.getReceptionist().equals("")) {
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
            if (depositQueryVO.getIdNumber() != null && !depositQueryVO.getIdNumber().equals("")) {
                depositQueryWrapper.eq("customerIdNumber", depositQueryVO.getIdNumber());
            }
            if (depositQueryVO.getStartNumber() != null && depositQueryVO.getEndNumber() != null) {
                depositQueryWrapper.between("deposit", depositQueryVO.getStartNumber(), depositQueryVO.getEndNumber());
            }
            if (depositQueryVO.getIsNewDeposit() != null) {
                depositQueryWrapper.eq("isNewDeposit", depositQueryVO.getIsNewDeposit());
            }
            if (depositQueryVO.getReceptionist() != null && !depositQueryVO.getReceptionist().equals("")) {
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

    //获取所有入库记录
    @Override
    public PageBean<InboundRecord> getInboundList(InboundQueryVO inboundQueryVO, Long currentPage, Long pageSize) {
        //封装page对象
        Page<InboundRecord> page = new Page<>(currentPage, pageSize);
        QueryWrapper<InboundRecord> inboundRecordQueryWrapper = new QueryWrapper<>();
        //不为空，根据条件查询入库记录
        if (inboundQueryVO.getProductId() != null) {
            inboundRecordQueryWrapper.eq("productId", inboundQueryVO.getProductId());
        }
        //判断时间数组是否为空
        if (inboundQueryVO.getDate() != null && inboundQueryVO.getDate().length == 2) {
            inboundRecordQueryWrapper.between("date", inboundQueryVO.getDate()[0], inboundQueryVO.getDate()[1]);
        }
        //判断支行是否为空
        if (inboundQueryVO.getBranchId() != null) {
            inboundRecordQueryWrapper.eq("branchId", inboundQueryVO.getBranchId());
        }
        //查询入库记录
        Page<InboundRecord> pageInfo = inboundRecordMapper.selectPage(page, inboundRecordQueryWrapper);
        //封装返回数据
        PageBean<InboundRecord> pageBean = new PageBean<>();
        pageBean.setTotal(pageInfo.getTotal());
        pageBean.setItems(pageInfo.getRecords());
        return pageBean;
    }

    //获取上交产品记录
    @Override
    public PageBean<ReboundRecords> getReboundList(InboundQueryVO inboundQueryVO, Long currentPage, Long pageSize) {
        //封装page对象
        Page<ReboundRecords> page = new Page<>(currentPage, pageSize);
        QueryWrapper<ReboundRecords> reboundRecordsQueryWrapper = new QueryWrapper<>();
        //不为空，根据条件查询入库记录
        if (inboundQueryVO.getProductId() != null) {
            reboundRecordsQueryWrapper.eq("productId", inboundQueryVO.getProductId());
        }
        //判断时间数组是否为空
        if (inboundQueryVO.getDate() != null && inboundQueryVO.getDate().length == 2) {
            reboundRecordsQueryWrapper.between("date", inboundQueryVO.getDate()[0], inboundQueryVO.getDate()[1]);
        }
        //判断支行是否为空
        if (inboundQueryVO.getBranchId() != null) {
            reboundRecordsQueryWrapper.eq("branchId", inboundQueryVO.getBranchId());
        }
        //查询入库记录
        Page<ReboundRecords> pageInfo = reboundRecordMapper.selectPage(page, reboundRecordsQueryWrapper);
        //封装返回数据
        PageBean<ReboundRecords> pageBean = new PageBean<>();
        pageBean.setTotal(pageInfo.getTotal());
        pageBean.setItems(pageInfo.getRecords());
        return pageBean;
    }

    //添加上交产品记录
    @Override
    public Result<String> addRebound(Integer productId, Integer branchId, Integer quantity) {
        //根据部门号与商品id查询剩余商品总量
        QueryWrapper<Inventory> inventoryQueryWrapper = new QueryWrapper<>();
        inventoryQueryWrapper.eq("productid", productId);
        inventoryQueryWrapper.eq("branchid", branchId);
        Inventory inventory = inventoryMapper.selectOne(inventoryQueryWrapper);
        //判断库存是否足够
        if (inventory.getQuantity() < quantity) {
            throw new RuntimeException("库存不足");
        }
        //库存足够则减去库存
        inventory.setQuantity(inventory.getQuantity() - quantity);
        //将减去的库存添加到上交数量
        inventory.setReback(inventory.getReback() + quantity);
        //更新库存
        inventoryMapper.updateById(inventory);
        //添加上交记录
        ReboundRecords reboundRecords = new ReboundRecords();
        reboundRecords.setProductId(productId);
        reboundRecords.setBranchId(branchId);
        reboundRecords.setProductNum(quantity);
        reboundRecords.setDate(new Date());
        reboundRecordMapper.insert(reboundRecords);
        return Result.success("上交成功");

    }

    //删除库存记录
    @Override
    @Transactional
    public Result<String> deleteInboundById(InboundRecord inboundRecord) {
        //根据productId与branchId查询库存
        QueryWrapper<Inventory> inventoryQueryWrapper = new QueryWrapper<>();
        inventoryQueryWrapper.eq("productid", inboundRecord.getProductId());
        inventoryQueryWrapper.eq("branchid", inboundRecord.getBranchId());
        Inventory inventory = inventoryMapper.selectOne(inventoryQueryWrapper);
        //判断库存是否存在
        if (inventory == null) {
            throw new RuntimeException("库存不存在");
        }
        //判断库存是否足够
        if (inventory.getQuantity() < inboundRecord.getQuantity()) {
            throw new RuntimeException("库存不足无法删除");
        }
        //库存足够则减去库存
        inventory.setQuantity(inventory.getQuantity() - inboundRecord.getQuantity());
        //同时减去total
        inventory.setTotal(inventory.getTotal() - inboundRecord.getQuantity());
        //更新库存
        inventoryMapper.updateById(inventory);
        //将减去的库存添加给总行
        //判断是否是总行物品
        HashMap<String, Object> claims = ThreadLocalUtil.get();
        Integer branchId = (Integer) claims.get("number");
        if (!inboundRecord.getBranchId().equals(branchId)) {
            //支行物品删除则将物品退还总行账户
            QueryWrapper<Inventory> inventoryQueryWrapperAdmin = new QueryWrapper<>();
            inventoryQueryWrapperAdmin.eq("productId", inboundRecord.getProductId());
            inventoryQueryWrapperAdmin.eq("branchId", branchId);
            Inventory inventoryAdmin = inventoryMapper.selectOne(inventoryQueryWrapperAdmin);
            //判断总行是否存在该物品
            if (inventoryAdmin == null) {
                throw new RuntimeException("总行库存不存在");
            }
            //总行库存存在则增加库存
            inventoryAdmin.setQuantity(inventoryAdmin.getQuantity() + inboundRecord.getQuantity());
            inventoryMapper.updateById(inventoryAdmin);
        }
        //删除入库记录
        inboundRecordMapper.deleteById(inboundRecord.getId());
        return Result.success("删除成功");

    }
}
