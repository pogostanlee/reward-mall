package com.rewardmall.pojo;

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
    private Integer id;//主键
    @NotEmpty
    @TableField("name")
    private  String name;//客户姓名
    @NotEmpty
    @TableField("phone")
    private  String phone;//客户电话
    @NotEmpty
    @TableField("idNumber")
    private  String idNumber;//身份证号
    @NotEmpty
    @TableField("manager")
    private  String manager;//客户经理
    @TableField("totalDeposit")
    private Integer totalDeposit;//总存款
    @TableField("points")
    private Integer points;//积分
    @TableField("branchId")
    private Integer branchId;//所属支行
}
