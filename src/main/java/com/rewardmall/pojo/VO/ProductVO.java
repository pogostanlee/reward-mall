package com.rewardmall.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductVO {
    //商品id
    private Integer id;
    //商品名称
    private String name;
    //商品积分
    private Integer price;
    //商品数量
    private Integer num;
    //客户身份证
    private String customerId;
}
