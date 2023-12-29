package com.rewardmall.pojo;

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
    @TableId(type = IdType.AUTO)
    private Integer id;//主键
    @TableField("branchId")
    @NotEmpty
    private Integer branchId;//支行id
    @NotEmpty
    @TableField("customerIdNumber")
    private String customerIdNumber;//客户id
    @NotEmpty
    @TableField("deposit")
    private Integer deposit;//存款金额
    @NotEmpty
    @TableField("depositDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date depositDate;//存款日期
    @NotEmpty
    @TableField("maturityDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date maturityDate;//到期日期
    @TableField("getPoints")
    private Integer getPoints;//获得积分
    @TableField("activity")
    private String activity;//参与的活动
    @TableField("name")
    private String name;//客户姓名
    @TableField("isNewDeposit")
    private Integer isNewDeposit;//是否是新存款
    @TableField("receptionist")
    private String receptionist;//揽存人
    @TableField("depositAccount")
    private String depositAccount;//存款账户
    @TableField("subDepositAccount")
    private String subDepositAccount;//子账户
    @TableField("monthDiff")
    private Integer monthDiff;//存款期限
}
