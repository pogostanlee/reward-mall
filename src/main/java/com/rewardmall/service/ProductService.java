package com.rewardmall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rewardmall.pojo.*;
import com.rewardmall.pojo.VO.InboundQueryVO;
import com.rewardmall.pojo.VO.ProdcutQueryVO;
import com.rewardmall.pojo.VO.ProductVO;

import java.util.List;

public interface ProductService {
    //获取商品列表
    Result<List<Product>> list();
    //兑换礼品
    Result<String> exchange(List<ProductVO> products);

    //根据身份证号查询兑换记录
    Page<OutboundRecord> listByVO(ProdcutQueryVO prodcutQueryVO, Long currentPage, Long pageSize);
    //删除兑换记录
    Result<String> deleteId(OutboundRecord outboundRecord);
    //查询库存
    Page<Inventory> productInventory(String id, Long currentPage, Long pageSize);
    //查询入库记录
    Page<InboundRecord> getInboundList(InboundQueryVO inboundQueryVO, Long currentPage, Long pageSize);
}
