package com.rewardmall.pojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName("deposits")
public class Deposit {
    @ExcelIgnore
    @TableId(type = IdType.AUTO)
    private Integer id;//主键
    @TableField("branchId")
    @NotEmpty
    @ExcelProperty("支行部门号")
    private Integer branchId;//支行id
    @NotEmpty
    @TableField("customerIdNumber")
    @ExcelProperty("客户身份证号")
    private String customerIdNumber;//客户id
    @NotEmpty
    @TableField("deposit")
    @ExcelProperty("存款金额")
    private Integer deposit;//存款金额
    @NotEmpty
    @TableField("depositDate")
    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty("存款日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date depositDate;//存款日期
    @NotEmpty
    @ExcelProperty("到期日期")
    @DateTimeFormat("yyyy-MM-dd")
    @TableField("maturityDate")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date maturityDate;//到期日期
    @TableField("getPoints")
    @ExcelProperty("获得积分")
    private Integer getPoints;//获得积分
    @TableField("activity")
    @ExcelProperty("参与的活动")
    private String activity;//参与的活动
    @TableField("name")
    @ExcelProperty("客户姓名")
    private String name;//客户姓名
    @TableField("isNewDeposit")
    @ExcelProperty("是否是新存款")
    private Integer isNewDeposit;//是否是新存款
    @TableField("receptionist")
    @ExcelProperty("揽存人")
    private String receptionist;//揽存人
    @ExcelProperty("存款账户")
    @TableField("depositAccount")
    private String depositAccount;//存款账户
    @ExcelProperty("子账户")
    @TableField("subDepositAccount")
    private String subDepositAccount;//子账户
    @ExcelProperty("存款期限(月)")
    @TableField("monthDiff")
    private Integer monthDiff;//存款期限
}
