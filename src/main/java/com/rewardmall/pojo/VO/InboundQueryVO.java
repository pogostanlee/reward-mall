package com.rewardmall.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InboundQueryVO {
    private Integer productId;//商品id
    private Date[] date;//兑换日期
}
