package com.rewardmall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rewardmall.pojo.Inventory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface InventoryMapper extends BaseMapper<Inventory> {
    //根据商品id与所属支行查询库存数量
    Integer getNumberByProductIdAndBranchId(@Param("productId") Integer productId, @Param("branchId") Integer branchId);

    //根据商品id与所属支行更新库存数量
    void updateNumberByProductIdAndBranchId(@Param("id") Integer id, @Param("branchId") Integer branchId, @Param("quantity") int quantity);
}
