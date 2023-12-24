package com.rewardmall.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rewardmall.pojo.Deposit;
import com.rewardmall.pojo.PageBean;
import com.rewardmall.pojo.Result;
import com.rewardmall.pojo.VO.DepositQueryVO;
import com.rewardmall.service.DepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/deposit")
@RestController
public class DepositController {
    @Autowired
    private DepositService depositService;
    //获取存款列表
    @RequestMapping("/depositList")
    public PageBean<Deposit> list(String idNumber,Long currentPage,Long pageSize) {
        //获取前端传递数据进行查询
        if (StringUtils.hasText(idNumber)) {
            //根据身份证号查询
            Page<Deposit> pageInfo=  depositService.listByIdNumber(idNumber,currentPage,pageSize);
            //封装返回数据
            PageBean<Deposit> pageBean = new PageBean<>();
            pageBean.setTotal(pageInfo.getTotal());
            pageBean.setItems(pageInfo.getRecords());
            return pageBean;
        }
        return null;
    }
    //新增存款
    @RequestMapping("/addDeposit")
    public Result<String> add(DepositQueryVO depositQueryVO) {
        //获取前端传递数据进行添加
        Result<String> result = depositService.add(depositQueryVO);
        return result;
    }
    //删除存款记录
    @RequestMapping("/deleteId")
    public Result<String> deleteId(Deposit deposit) {
        Result<String> result = depositService.deleteId(deposit);
        return result;
    }
}
