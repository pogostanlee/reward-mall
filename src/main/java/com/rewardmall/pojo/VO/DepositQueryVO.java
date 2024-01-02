package com.rewardmall.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DepositQueryVO {
    private String receptionist;//揽存人
    private String idNumber;//客户身份证号
    private Integer startNumber;//存款起始金额
    private Integer endNumber;//存款终止金额
    private Integer isNewDeposit;//是否是新存款
    private Date[] date;//存款日期
    private Integer monthDiff;//存款期限（月）
    private Integer branchId;//支行id
}
