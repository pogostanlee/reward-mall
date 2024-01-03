package com.rewardmall.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rewardmall.mapper.OutboundRecordMapper;
import com.rewardmall.pojo.*;
import com.rewardmall.pojo.VO.InboundQueryVO;
import com.rewardmall.pojo.VO.ProdcutQueryVO;
import com.rewardmall.pojo.VO.ProductVO;
import com.rewardmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/product")
@RestController
@CrossOrigin
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
        //
        Result<String> result = productService.exchange(products);
        return result;
    }

    //查询用户兑换记录
    @RequestMapping("/exchangeList")
    public PageBean<OutboundRecord> exchangeList(ProdcutQueryVO prodcutQueryVO, Long currentPage, Long pageSize) {

            //查询
            Page<OutboundRecord> pageInfo = productService.listByVO(prodcutQueryVO, currentPage, pageSize);
            //封装返回数据
            PageBean<OutboundRecord> pageBean = new PageBean<>();
            pageBean.setTotal(pageInfo.getTotal());
            pageBean.setItems(pageInfo.getRecords());
            return pageBean;

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
    //查询入库记录
    @RequestMapping("/inbound")
    public PageBean<InboundRecord> inbound(InboundQueryVO inboundQueryVO, Long currentPage, Long pageSize) {
            //根据商品id查询
            Page<InboundRecord> pageInfo = productService.getInboundList(inboundQueryVO, currentPage, pageSize);
            //封装返回数据
            PageBean<InboundRecord> pageBean = new PageBean<>();
            pageBean.setTotal(pageInfo.getTotal());
            pageBean.setItems(pageInfo.getRecords());
            return pageBean;
    }

}
