package com.rewardmall.convert;

import org.springframework.core.convert.converter.Converter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DateConvert implements Converter<String, Date> {
    static List<SimpleDateFormat> list = new ArrayList<SimpleDateFormat>();
    static {
    list.add(new SimpleDateFormat("yyyy-MM-dd"));
    list.add(new SimpleDateFormat("yyyy/MM/dd"));
    list.add(new SimpleDateFormat("yyyy年MM月dd日"));

    }
    //日期格式转换
    @Override
    public Date convert(String source) {
        for (SimpleDateFormat simpleDateFormat : list) {
            try {
                return simpleDateFormat.parse(source);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
