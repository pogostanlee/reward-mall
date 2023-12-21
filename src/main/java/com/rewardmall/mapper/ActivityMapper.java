package com.rewardmall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rewardmall.pojo.Activity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ActivityMapper extends BaseMapper<Activity> {
    //根据是否激活选择活动
    Activity selectByIsActive(@Param("isActive") Integer isActive);
}
