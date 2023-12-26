package com.rewardmall.pojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName("customers")
public class Customer {
    @TableId(type = IdType.AUTO)
    @ExcelIgnore
    private Integer id;//主键
    @NotEmpty
    @TableField("name")
    @ExcelProperty("客户姓名")
    private  String name;//客户姓名
    @NotEmpty
    @TableField("phone")
    @ExcelProperty("客户电话")
    private  String phone;//客户电话
    @NotEmpty
    @TableField("idNumber")
    @ExcelProperty("身份证号")
    private  String idNumber;//身份证号
    @NotEmpty
    @TableField("manager")
    @ExcelProperty("客户经理")
    private  String manager;//客户经理
    @TableField("totalDeposit")
    @ExcelProperty("总存款")
    private Integer totalDeposit;//总存款
    @TableField("points")
    @ExcelProperty("积分")
    private Integer points;//积分
    @TableField("branchId")
    @ExcelProperty("所属支行")
    private Integer branchId;//所属支行
}
