package com.rewardmall.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rewardmall.mapper.ActivityMapper;
import com.rewardmall.mapper.CustomerMapper;
import com.rewardmall.mapper.DepositMapper;
import com.rewardmall.pojo.Activity;
import com.rewardmall.pojo.Customer;
import com.rewardmall.pojo.Deposit;
import com.rewardmall.pojo.Result;
import com.rewardmall.pojo.VO.DepositQueryVO;
import com.rewardmall.service.DepositService;
import com.rewardmall.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;

@Service
public class DepositServiceImpl implements DepositService {
    @Autowired
    private DepositMapper depositMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private ActivityMapper activityMapper;
    //活动基本倍率
    private static final Integer BASE_RATE = 1;
    //活动倍率
    private Integer ACTIVITY_RATE;

    //根据身份证号查询存款
    @Override
    public Page<Deposit> listByIdNumber(String idNumber, Long currentPage, Long pageSize) {
        //封装page对象
        Page<Customer> page = new Page<>(currentPage, pageSize);
        //获取branchId
        HashMap<String, Object> claims = ThreadLocalUtil.get();
        Integer branchId = (Integer) claims.get("number");
        //根据身份证号查询
        Page<Deposit> pageInfo = depositMapper.listByIdNumber(page, idNumber, branchId);

        return pageInfo;
    }

    //添加存款
    @Override
    @Transactional
    public Result<String> add(DepositQueryVO depositQueryVO) {
        //获取客户身份证号
        String idNumber = depositQueryVO.getCustomerIdNumber();
        //根据身份证号查询客户信息
        Customer customer = customerMapper.selectByIdNumber(idNumber);
        //判断customer是否为空
        if (customer == null) {
            //为空，抛出异常
            throw new RuntimeException("客户不存在");
        }
        //查询激活的活动
        Activity activitied = activityMapper.selectByIsActive(1);
        //判断活动是否为空
        if (activitied == null) {
            ACTIVITY_RATE = BASE_RATE;
        } else { //获取活动倍率
            Integer rate = activitied.getPointsMultiplier();
            //计算活动倍率
            ACTIVITY_RATE = rate * BASE_RATE;
        }
        //计算获得积分，存款金额/100，然后取整，再乘以活动倍率
        Integer points = (int) (depositQueryVO.getDeposit() / 100 * ACTIVITY_RATE);
        //判断存款日期是否为生日
        //获取存款日期
        Date depositDate = depositQueryVO.getDepositDate();
        //获取存款日期的月份与日子并设置格式为两位数
        String depositMonth = String.format("%02d", depositDate.getMonth() + 1);
        String depositDay = String.format("%02d", depositDate.getDate());
        //获取客户生日
        String month = idNumber.substring(10, 12);
        String day = idNumber.substring(12, 14);
        //判断存款日期是否为生日
        if (depositMonth.equals(month) && depositDay.equals(day)) {
            //是生日，积分翻倍
            points = points * 2;
        }
        //获取支行branchId
        HashMap<String, Object> claims = ThreadLocalUtil.get();
        Integer branchId = (Integer) claims.get("number");
        //封装存款对象
        Deposit deposit = new Deposit();
        deposit.setBranchId(branchId);
        deposit.setCustomerIdNumber(idNumber);
        deposit.setDeposit(depositQueryVO.getDeposit());
        deposit.setDepositDate(depositQueryVO.getDepositDate());
        deposit.setMaturityDate(depositQueryVO.getMaturityDate());
        deposit.setGetPoints(points);
        deposit.setActivity(activitied.getName());
        deposit.setName(customer.getName());
        //添加存款记录
        int count = depositMapper.insert(deposit);
        //判断是否添加成功
        if (count > 0) {
            //添加成功，更新客户积分
            customer.setPoints(customer.getPoints() + points);
            //更新客户存款
            customer.setTotalDeposit(customer.getTotalDeposit() + depositQueryVO.getDeposit());
            //更新客户信息
            customerMapper.updateById(customer);
            //添加成功，返回成功信息
            return Result.success("添加成功");
        }

        return Result.error("添加失败");
    }

    //删除存款记录
    @Override
    @Transactional
    public Result<String> deleteId(Deposit deposit) {
        //获取存款的客户id
        String idNumber = deposit.getCustomerIdNumber();
        //根据客户id查询客户信息
        Customer customer = customerMapper.selectByIdNumber(idNumber);
        //判断客户是否存在
        if (customer == null) {
            //客户不存在，抛出异常
            throw new RuntimeException("客户不存在");
        }
        //获取存款金额
        Integer depositMoney = deposit.getDeposit();
        //获取存款积分
        Integer points = deposit.getGetPoints();
        //判断存款积分是否大于客户积分
        if (points > customer.getPoints()) {
            //存款积分大于客户积分，抛出异常
            throw new RuntimeException("客户积分不足");
        }
        //更新客户积分
        customer.setPoints(customer.getPoints() - points);
        //更新客户存款
        customer.setTotalDeposit(customer.getTotalDeposit() - depositMoney);
        //更新客户信息
        customerMapper.updateById(customer);
        //删除存款记录
        int count = depositMapper.deleteById(deposit.getId());
        //判断是否删除成功
        if (count > 0) {
            //删除成功，返回成功信息
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
}
