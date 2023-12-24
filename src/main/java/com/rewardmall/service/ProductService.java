package com.rewardmall.service;

import com.rewardmall.pojo.Product;
import com.rewardmall.pojo.Result;
import com.rewardmall.pojo.VO.ProductVO;

import java.util.List;

public interface ProductService {
    //获取商品列表
    Result<List<Product>> list();

    Result<String> exchange(List<ProductVO> products);
    //兑换礼品
  // Result<String> exchange(ProductExVO productExVO);
}
