package com.rewardmall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rewardmall.mapper.*;
import com.rewardmall.pojo.*;
import com.rewardmall.pojo.VO.InboundQueryVO;
import com.rewardmall.pojo.VO.ProdcutQueryVO;
import com.rewardmall.pojo.VO.ProductVO;
import com.rewardmall.service.ProductService;
import com.rewardmall.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
    @Autowired
    private InboundRecordMapper inboundRecordMapper;

    //获取商品列表
    @Override
    public Result<List<Product>> list() {
        QueryWrapper<Product> productQueryWrapper = new QueryWrapper<>();
        productQueryWrapper.eq("isActive", 1);
        List<Product> products = productMapper.selectList(productQueryWrapper);

        return products == null ? Result.error("获取商品列表失败") : Result.success(products);
    }

    @Override
    @Transactional
    //兑换礼品
    public Result<String> exchange(List<ProductVO> products) {
        //根据身份证和branchId查询用户信息
        HashMap<String, Object> claims = ThreadLocalUtil.get();
        Integer branchId = (Integer) claims.get("number");
        String idNumber = (String) products.get(0).getCustomerId();
        Customer customer = customerMapper.selectcustomer(idNumber, branchId);
        //判断用户是否存在
        if (customer == null) {
            throw new RuntimeException("用户不存在");
        }
        //根据id查询商品信息并计算总价
        Integer totalPrice = 0;
        for (ProductVO product : products) {
            Product product1 = productMapper.selectById(product.getId());
            if (product1 == null) {
                throw new RuntimeException("商品不存在");
            }
            totalPrice += product1.getPrice() * product.getNum();
        }
        //判断用户积分是否足够
        if (customer.getPoints() < totalPrice) {
            throw new RuntimeException("用户积分不足");

        }
        //扣除用户积分
        customer.setPoints(customer.getPoints() - totalPrice);
        customerMapper.updateById(customer);
        //查询商品库存并判断是否足够
        for (ProductVO product : products) {
            Integer number = inventoryMapper.getNumberByProductIdAndBranchId(product.getId(), customer.getBranchId());
            //判断number是否为空
            if (number == null) {
                throw new RuntimeException("商品未入库，请先入库商品");

            }
            if (number < product.getNum()) {
                throw new RuntimeException("商品库存不足");
            }
            //扣除商品库存
            inventoryMapper.updateNumberByProductIdAndBranchId(product.getId(), customer.getBranchId(), number - product.getNum());
            //添加出库记录
            outboundRecordMapper.insert(customer.getBranchId(), customer.getIdNumber(), product.getId(), product.getNum(), product.getPrice() * product.getNum(), customer.getName(), new Date());
        }
        return Result.success("兑换成功");

    }

    //查询兑换记录
    @Override
    public Page<OutboundRecord> listByVO(ProdcutQueryVO prodcutQueryVO, Long currentPage, Long pageSize) {
        //封装page对象
        Page<OutboundRecord> page = new Page<>(currentPage, pageSize);
        //获取branchId
        HashMap<String, Object> claims = ThreadLocalUtil.get();
        Integer branchId = (Integer) claims.get("number");
        QueryWrapper<OutboundRecord> outboundRecordQueryWrapper = new QueryWrapper<>();
        //不为空，根据条件查询兑换记录
        outboundRecordQueryWrapper.eq("branchId", branchId);
        if (prodcutQueryVO.getIdNumber() != null && !prodcutQueryVO.getIdNumber().equals("")) {
            outboundRecordQueryWrapper.eq("customerIdNumber", prodcutQueryVO.getIdNumber());
        }
        if (prodcutQueryVO.getProductId() != null) {
            outboundRecordQueryWrapper.eq("productId", prodcutQueryVO.getProductId());
        }
        //判断时间数组是否为空
        if (prodcutQueryVO.getDate() != null && prodcutQueryVO.getDate().length == 2) {
            outboundRecordQueryWrapper.between("date", prodcutQueryVO.getDate()[0], prodcutQueryVO.getDate()[1]);
        }
        Page<OutboundRecord> pageInfo = outboundRecordMapper.selectPage(page, outboundRecordQueryWrapper);
        return pageInfo;


    }

    //删除兑换记录
    @Override
    @Transactional
    public Result<String> deleteId(OutboundRecord outboundRecord) {
        //获取用户身份证号
        String idNumber = outboundRecord.getCustomerIdNumber();
        //获取支行信息
        Integer branchId = outboundRecord.getBranchId();
        //根据身份证号查询用户信息
        Customer customer = customerMapper.selectByIdNumberAndBranchId(idNumber, branchId);
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
        if (id == null || id.equals("")) {
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
    //查询入库记录
    @Override
    public Page<InboundRecord> getInboundList(InboundQueryVO inboundQueryVO, Long currentPage, Long pageSize) {
        //封装page对象
        Page<InboundRecord> page = new Page<>(currentPage, pageSize);
        //获取branchId
        HashMap<String, Object> claims = ThreadLocalUtil.get();
        Integer branchId = (Integer) claims.get("number");
        QueryWrapper<InboundRecord> inboundRecordQueryWrapper = new QueryWrapper<>();
        //不为空，根据条件查询入库记录
        inboundRecordQueryWrapper.eq("branchId", branchId);
        if (inboundQueryVO.getProductId() != null) {
            inboundRecordQueryWrapper.eq("productId", inboundQueryVO.getProductId());
        }
        //判断时间数组是否为空
        if (inboundQueryVO.getDate() != null && inboundQueryVO.getDate().length == 2) {
            inboundRecordQueryWrapper.between("date", inboundQueryVO.getDate()[0], inboundQueryVO.getDate()[1]);
        }
        Page<InboundRecord> pageInfo = inboundRecordMapper.selectPage(page, inboundRecordQueryWrapper);
        return pageInfo;
    }
}

