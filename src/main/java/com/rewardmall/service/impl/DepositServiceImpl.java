package com.rewardmall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rewardmall.mapper.ActivityMapper;
import com.rewardmall.mapper.CustomerMapper;
import com.rewardmall.mapper.DepositMapper;
import com.rewardmall.pojo.*;
import com.rewardmall.pojo.VO.DepositQueryVO;
import com.rewardmall.pojo.VO.DepositVO;
import com.rewardmall.service.DepositService;
import com.rewardmall.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public PageBean<Deposit> list(DepositQueryVO depositQueryVO, Long currentPage, Long pageSize) {
        //封装page对象
        Page<Deposit> page = new Page<>(currentPage, pageSize);
        //获取branchId
        HashMap<String, Object> claims = ThreadLocalUtil.get();
        Integer branchId = (Integer) claims.get("number");
        //判断depositQueryVO属性是否为空
        if (depositQueryVO != null) {
            //不为空，根据条件查询存款
            QueryWrapper<Deposit> depositQueryWrapper = new QueryWrapper<>();
            depositQueryWrapper.eq("branchId", branchId);
            if (depositQueryVO.getIdNumber() != null&&!depositQueryVO.getIdNumber().equals("")) {
                depositQueryWrapper.eq("customerIdNumber", depositQueryVO.getIdNumber());
            }
            if (depositQueryVO.getStartNumber() != null&&depositQueryVO.getEndNumber() != null) {
                depositQueryWrapper.between("deposit", depositQueryVO.getStartNumber(), depositQueryVO.getEndNumber());
            }
            if (depositQueryVO.getIsNewDeposit() != null) {
                depositQueryWrapper.eq("isNewDeposit", depositQueryVO.getIsNewDeposit());
            }
            if (depositQueryVO.getReceptionist() != null&&!depositQueryVO.getReceptionist().equals("")) {
                depositQueryWrapper.eq("receptionist", depositQueryVO.getReceptionist());
            }
            if (depositQueryVO.getMonthDiff() != null) {
                depositQueryWrapper.eq("monthDiff", depositQueryVO.getMonthDiff());
            }
            if (depositQueryVO.getDate() != null && depositQueryVO.getDate().length == 2) {
                depositQueryWrapper.between("depositDate", depositQueryVO.getDate()[0], depositQueryVO.getDate()[1]);
            }
            Page<Deposit> pageInfo = depositMapper.selectPage(page, depositQueryWrapper);
            //封装返回数据
            PageBean<Deposit> pageBean = new PageBean<>();
            pageBean.setTotal(pageInfo.getTotal());
            pageBean.setItems(pageInfo.getRecords());
            return pageBean;
        }

        //为空，查询全部存款
        QueryWrapper<Deposit> depositQueryWrapper = new QueryWrapper<>();
        depositQueryWrapper.eq("branchId", branchId);
        Page<Deposit> pageInfo = depositMapper.selectPage(page, depositQueryWrapper);
        //封装返回数据
        PageBean<Deposit> pageBean = new PageBean<>();
        pageBean.setTotal(pageInfo.getTotal());
        pageBean.setItems(pageInfo.getRecords());
        return pageBean;
    }
    //添加存款
    @Override
    @Transactional
    public Result<String> add(DepositVO depositVO) {
        //获取客户身份证号
        String idNumber = depositVO.getCustomerIdNumber();
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
        Integer points = (int) (depositVO.getDeposit() / 100 * ACTIVITY_RATE);
        //判断存款日期是否为生日

        //获取支行branchId
        HashMap<String, Object> claims = ThreadLocalUtil.get();
        Integer branchId = (Integer) claims.get("number");
        //封装存款对象
        Deposit deposit = new Deposit();
        deposit.setBranchId(branchId);
        deposit.setCustomerIdNumber(idNumber);
        deposit.setDeposit(depositVO.getDeposit());
        deposit.setDepositDate(depositVO.getDepositDate());
        deposit.setMaturityDate(depositVO.getMaturityDate());
        deposit.setGetPoints(points);
        deposit.setActivity(activitied != null ? activitied.getName() : "无");
        deposit.setName(customer.getName());
        deposit.setIsNewDeposit(depositVO.getIsNewDeposit());
        deposit.setReceptionist(depositVO.getReceptionist());
        deposit.setDepositAccount(depositVO.getDepositAccount());
        deposit.setSubDepositAccount(depositVO.getSubDepositAccount());
        deposit.setMonthDiff(depositVO.getMonthDiff());
        //添加存款记录
        int count = depositMapper.insert(deposit);
        //判断是否添加成功
        if (count > 0) {
            //添加成功，更新客户积分
            customer.setPoints(customer.getPoints() + points);
            //更新客户存款
            customer.setTotalDeposit(customer.getTotalDeposit() + depositVO.getDeposit());
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
