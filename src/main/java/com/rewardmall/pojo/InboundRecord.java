package com.rewardmall.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName("inboundrecords")
public class InboundRecord {
    @TableId(type = IdType.AUTO)
    private Integer id;//主键
    @TableField("branchId")
    private Integer branchId;//所属支行
    @TableField("productId")
    private Integer productId;//商品id
    @TableField("quantity")
    private Integer quantity;//入库数量
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date date;//入库日期
}
