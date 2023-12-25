package com.rewardmall.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rewardmall.mapper.OutboundRecordMapper;
import com.rewardmall.pojo.*;
import com.rewardmall.pojo.VO.ProductVO;
import com.rewardmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/product")
@RestController
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private OutboundRecordMapper outboundRecordMapper;

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

    //查询用户兑换记录
    @RequestMapping("/exchangeList")
    public PageBean<OutboundRecord> exchangeList(String idNumber, Long currentPage, Long pageSize) {
        //获取前端传递数据进行查询
        if (StringUtils.hasText(idNumber)) {
            //根据身份证号查询
            Page<OutboundRecord> pageInfo = productService.listByIdNumber(idNumber, currentPage, pageSize);
            //封装返回数据
            PageBean<OutboundRecord> pageBean = new PageBean<>();
            pageBean.setTotal(pageInfo.getTotal());
            pageBean.setItems(pageInfo.getRecords());
            return pageBean;
        }
        return null;
    }

    //删除兑换记录
    @RequestMapping("/deleteId")
    public Result<String> deleteId(OutboundRecord outboundRecord) {
        Result<String> result = productService.deleteId(outboundRecord);
        return result;
    }

    //查询商品库存详情
    @RequestMapping("/productInventory")
    public PageBean<Inventory> productInventory(String id, Long currentPage, Long pageSize) {
            //根据商品id查询
            Page<Inventory> pageInfo = productService.productInventory(id, currentPage, pageSize);
            //封装返回数据
            PageBean<Inventory> pageBean = new PageBean<>();
            pageBean.setTotal(pageInfo.getTotal());
            pageBean.setItems(pageInfo.getRecords());
            return pageBean;
    }
}
