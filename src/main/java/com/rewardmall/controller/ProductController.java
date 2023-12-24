package com.rewardmall.controller;

import com.rewardmall.pojo.Product;
import com.rewardmall.pojo.Result;
import com.rewardmall.pojo.VO.ProductVO;
import com.rewardmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/product")
@RestController
public class ProductController {
        @Autowired
        private ProductService productService;
    //获取商品列表
    @RequestMapping("/list")
    public Result<List<Product>> list() {
        Result<List<Product>> result = productService.list();
        return result;
    }
    //兑换礼品
    @RequestMapping("/exchange")
       public Result<String> exchange(@RequestBody List<ProductVO> products) {
        System.out.println(products);
           //
        Result<String> result = productService.exchange(products);
            return result;
    }
}
