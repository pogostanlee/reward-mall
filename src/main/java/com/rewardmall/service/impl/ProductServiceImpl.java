package com.rewardmall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rewardmall.mapper.CustomerMapper;
import com.rewardmall.mapper.InventoryMapper;
import com.rewardmall.mapper.OutboundRecordMapper;
import com.rewardmall.mapper.ProductMapper;
import com.rewardmall.pojo.*;
import com.rewardmall.pojo.VO.ProductVO;
import com.rewardmall.service.ProductService;
import com.rewardmall.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private InventoryMapper inventoryMapper;
    @Autowired
    private OutboundRecordMapper outboundRecordMapper;

    //获取商品列表
    @Override
    public Result<List<Product>> list() {

        List<Product> products = productMapper.selectList(null);

        return products == null ? Result.error("获取商品列表失败") : Result.success(products);
    }

    @Override
    @Transactional
    public Result<String> exchange(List<ProductVO> products) {
        //根据身份证查询用户信息
        Customer customer = customerMapper.selectByIdNumber(products.get(0).getCustomerId());
        //判断用户是否存在
        if (customer == null) {
            return Result.error("用户不存在");
        }
        //根据id查询商品信息并计算总价
        Integer totalPrice = 0;
        for (ProductVO product : products) {
            Product product1 = productMapper.selectById(product.getId());
            if (product1 == null) {
                return Result.error("商品不存在");
            }
            totalPrice += product1.getPrice() * product.getNum();
        }
        //判断用户积分是否足够
        if (customer.getPoints() < totalPrice) {
            return Result.error("用户积分不足");
        }
        //扣除用户积分
        customer.setPoints(customer.getPoints() - totalPrice);
        customerMapper.updateById(customer);
        //查询商品库存并判断是否足够
        for (ProductVO product : products) {
            Integer number = inventoryMapper.getNumberByProductIdAndBranchId(product.getId(), customer.getBranchId());
            if (number < product.getNum()) {
                return Result.error("商品库存不足");
            }
            //扣除商品库存
            inventoryMapper.updateNumberByProductIdAndBranchId(product.getId(), customer.getBranchId(), number - product.getNum());
            //添加出库记录
            outboundRecordMapper.insert(customer.getBranchId(), customer.getIdNumber(), product.getId(), product.getNum(), product.getPrice() * product.getNum(), customer.getName());
        }
        return Result.success("兑换成功");

    }

    //根据身份证号查询兑换记录
    @Override
    public Page<OutboundRecord> listByIdNumber(String idNumber, Long currentPage, Long pageSize) {
        //封装page对象
        Page<OutboundRecord> page = new Page<>(currentPage, pageSize);
        //获取branchId
        HashMap<String, Object> claims = ThreadLocalUtil.get();
        Integer branchId = (Integer) claims.get("number");
        //根据身份证号查询兑换记录
        QueryWrapper<OutboundRecord> outboundRecordQueryWrapper = new QueryWrapper<>();
        outboundRecordQueryWrapper.eq("branchId", branchId);
        outboundRecordQueryWrapper.eq("customerIdNumber", idNumber);
        Page<OutboundRecord> pageInfo = outboundRecordMapper.selectPage(page, outboundRecordQueryWrapper);
        return pageInfo;
    }

    //删除兑换记录
    @Override
    @Transactional
    public Result<String> deleteId(OutboundRecord outboundRecord) {
        //获取用户身份证号
        String idNumber = outboundRecord.getCustomerIdNumber();
        //根据身份证号查询用户信息
        Customer customer = customerMapper.selectByIdNumber(idNumber);
        //判断用户是否存在
        if (customer == null) {
            //用户不存在，抛出异常
            throw new RuntimeException("用户不存在");
        }
        //获取商品id
        Integer productId = outboundRecord.getProductId();
        //根据商品id查询商品信息
        Product product = productMapper.selectById(productId);
        //判断商品是否存在
        if (product == null) {
            //商品不存在，抛出异常
            throw new RuntimeException("商品不存在");
        }
        //获取商品数量
        Integer quantity = outboundRecord.getQuantity();
        //查询库存表商品数
        Integer number = inventoryMapper.getNumberByProductIdAndBranchId(productId, customer.getBranchId());
        //将退还商品数增加
        quantity += number;
        //增加库存表中的商品数量
        inventoryMapper.updateNumberByProductIdAndBranchId(productId, customer.getBranchId(), quantity);
        //退还积分
        customer.setPoints(customer.getPoints() + outboundRecord.getPoints());
        //更新用户信息
        customerMapper.updateById(customer);
        //删除兑换记录
        int count = outboundRecordMapper.deleteById(outboundRecord.getId());
        //判断是否删除成功
        if (count > 0) {
            //删除成功，返回成功信息
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }

    @Override
    public Page<Inventory> productInventory(String id, Long currentPage, Long pageSize) {
        //封装page对象
        Page<Inventory> page = new Page<>(currentPage, pageSize);
        //获取branchId
        HashMap<String, Object> claims = ThreadLocalUtil.get();
        Integer branchId = (Integer) claims.get("number");
        //封装查询条件
            //判断id非空
        if (id == null||id.equals("")) {
            //id为空，查询所有
            QueryWrapper<Inventory> inventoryQueryWrapper = new QueryWrapper<>();
            inventoryQueryWrapper.eq("branchId", branchId);
            Page<Inventory> pageInfo = inventoryMapper.selectPage(page, inventoryQueryWrapper);
            return pageInfo;
        }
        //根据id和支行id查询
        QueryWrapper<Inventory> inventoryQueryWrapper = new QueryWrapper<>();
        inventoryQueryWrapper.eq("productId", id);
        inventoryQueryWrapper.eq("branchId", branchId);
        Page<Inventory> pageInfo = inventoryMapper.selectPage(page, inventoryQueryWrapper);
        return pageInfo;

    }
}

