package com.rewardmall.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DepositQueryVO {
    private String name;//客户姓名
    private String customerIdNumber;//客户身份证号
    private Integer deposit;//存款金额
    private Date depositDate;//存款日期
    private Date maturityDate;//到期日期
}
