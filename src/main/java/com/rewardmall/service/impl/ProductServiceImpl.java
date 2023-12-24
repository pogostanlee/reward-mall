package com.rewardmall.service.impl;

import com.rewardmall.mapper.CustomerMapper;
import com.rewardmall.mapper.InventoryMapper;
import com.rewardmall.mapper.OutboundRecordMapper;
import com.rewardmall.mapper.ProductMapper;
import com.rewardmall.pojo.Customer;
import com.rewardmall.pojo.Product;
import com.rewardmall.pojo.Result;
import com.rewardmall.pojo.VO.ProductVO;
import com.rewardmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            outboundRecordMapper.insert(customer.getBranchId(),customer.getIdNumber(),product.getId(),product.getNum(),totalPrice,customer.getName());
        }
        return Result.success("兑换成功");

    }
}

