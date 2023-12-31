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
@TableName("outboundRecords")
public class OutboundRecord {
    @TableId(type = IdType.AUTO)
    private Integer id;//主键
    @TableField("productId")
    private Integer productId;//商品id
    @TableField("quantity")
    private Integer quantity;//出库数量
    @TableField("branchId")
    private Integer branchId;//所属支行
    @TableField("customerIdNumber")
    private String customerIdNumber;//客户id
    @TableField("points")
    private Integer points;//消耗积分
    @TableField("name")
    private String name;//客户姓名
    @TableField("date")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date date;//出库日期
}
