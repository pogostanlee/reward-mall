package com.rewardmall.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName("inventory")
public class Inventory {
    @TableId(type = IdType.AUTO)
    private Integer id;//主键
    @TableField("productId")
    private Integer productId;//商品id
    @TableField("quantity")
    private Integer quantity;//库存数量
    @TableField("total")
    private Integer total;//商品总量
    @TableField("branchId")
    private Integer branchId;//所属支行
    @TableField("reback")
    private Integer reback;//退货数量
}
