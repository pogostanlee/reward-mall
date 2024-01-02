package com.rewardmall.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DepositVO {
    private String Receptionist;//揽存人
    private String depositAccount;//存款账户
    private String subDepositAccount;//子账户
    private String customerIdNumber;//客户身份证号
    private Integer monthDiff;//存款期限
    private Integer deposit;//存款金额
    private Date depositDate;//存款日期
    private Date maturityDate;//到期日期
    private Integer isNewDeposit;//是否是新存款
    private Integer newDeposit;//新增存款金额
}
