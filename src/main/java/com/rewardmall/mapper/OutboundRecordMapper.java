package com.rewardmall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rewardmall.pojo.OutboundRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

@Mapper
public interface OutboundRecordMapper extends BaseMapper<OutboundRecord> {
    //插入出库记录
    void insert(@Param("branchId") Integer branchId, @Param("customerIdNumber") String customerId, @Param("productId") Integer id, @Param("quantity") Integer number, @Param("points") Integer totalPrice, @Param("name") String name, @Param("date") Date date);

}
