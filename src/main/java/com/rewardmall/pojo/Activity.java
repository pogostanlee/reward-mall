package com.rewardmall.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("activities")
public class Activity {
    @TableId(type = IdType.AUTO)
    private Integer id;//主键
    @TableField("name")
    private String name;//活动名称
    @TableField("pointsMultiplier")
    private Integer pointsMultiplier;//积分倍数
    @TableField("isActive")
    private Integer isActive;//是否激活
}
