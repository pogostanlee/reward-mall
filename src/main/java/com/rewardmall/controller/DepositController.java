package com.rewardmall.controller;

import com.rewardmall.pojo.Deposit;
import com.rewardmall.pojo.PageBean;
import com.rewardmall.pojo.Result;
import com.rewardmall.pojo.VO.DepositQueryVO;
import com.rewardmall.pojo.VO.DepositVO;
import com.rewardmall.service.DepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/deposit")
@RestController
public class DepositController {
    @Autowired
    private DepositService depositService;
    //获取存款列表
    @RequestMapping("/depositList")
    public PageBean<Deposit> list(DepositQueryVO depositQueryVO,
                                  @RequestParam("currentPage") Long currentPage,
                                  @RequestParam("pageSize") Long pageSize) {
        //获取前端传递数据进行查询
        PageBean<Deposit> pageBean=  depositService.list(depositQueryVO,currentPage,pageSize);

        return pageBean;
    }
    //新增存款
    @RequestMapping("/addDeposit")
    public Result<String> add(DepositVO depositVO) {
        //获取前端传递数据进行添加
        Result<String> result = depositService.add(depositVO);
        return result;
    }
    //删除存款记录
    @RequestMapping("/deleteId")
    public Result<String> deleteId(Deposit deposit) {
        Result<String> result = depositService.deleteId(deposit);
        return result;
    }
}
