package com.rewardmall.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName("branches")
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;//主键
    @TableField("number")
    private Integer number;//支行编号
    @JsonIgnore
    @TableField("password")
    private String password;//密码
    @TableField("name")
    private String name;//支行名称
    @TableField("isAdmin")
    private Integer isAdmin;//是否是管理员

}
