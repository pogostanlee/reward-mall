package com.rewardmall.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerQueryVO {
    private  String name;//客户姓名
    private  String phone;//客户电话
    private  String idNumber;//身份证号
    private  String manager;//客户经理
    private  Integer startNumber;//查询存款起始
    private  Integer endNumber;//查询存款结束
    private  Long currentPage;//当前页
    private  Long pageSize;//每页显示条数
    private Integer branchId;//所属支行
}
