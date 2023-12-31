package com.rewardmall.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProdcutQueryVO {
    private String idNumber;//客户身份证号
    private Integer productId;//商品id
    private Date[] date;//兑换日期
}
