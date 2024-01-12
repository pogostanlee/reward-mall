package com.rewardmall.pojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
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
    @ExcelIgnore
    private Integer id;//主键
    @TableField("productId")
    @ExcelIgnore
    private Integer productId;//商品id
    @TableField(exist = false)
    @ExcelProperty("商品名称")
    private String productName;
    @TableField("quantity")
    @ExcelProperty("库存数量")
    private Integer quantity;//库存数量
    @TableField("total")
    @ExcelProperty("商品总量")
    private Integer total;//商品总量
    @TableField("branchId")
    @ExcelProperty("所属部门")
    private Integer branchId;//所属支行
    @TableField("reback")
    @ExcelProperty("退货数量")
    private Integer reback;//退货数量


}
