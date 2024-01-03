package com.rewardmall.controller;

import com.rewardmall.pojo.Result;
import com.rewardmall.pojo.User;
import com.rewardmall.service.UserService;
import com.rewardmall.utils.JwtUtil;
import com.rewardmall.utils.Md5Util;
import com.rewardmall.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
@CrossOrigin
@RequestMapping("/user")
@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //登录账号
    @RequestMapping("/login")
    public Result<String> login(String number, String password) {
        //根据用户名查询用户信息比对密码
        User user = userService.fingByUserName(number);
        //非空判断
        if (user == null) {
            return Result.error("用户名不存在");
        }
        //比对密码
        if (Md5Util.getMD5String(password).equals(user.getPassword())) {
            //登录成功
            Map<String, Object> claims = new HashMap<>();
            //封装参数
            claims.put("id", user.getId());
            claims.put("number", user.getNumber());
            claims.put("name", user.getName());
            claims.put("isAdmin", user.getIsAdmin());
            String token = JwtUtil.genToken(claims);
            //把token存储到redis中
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.set(token, token, 24, TimeUnit.HOURS);
            return Result.success(token);
        }
        return Result.error("密码错误");
    }

    //获取用户详细信息
    @RequestMapping("/info")
    public Result<User> info() throws Exception {
        //获取用户id
        //在线程中取出客户信息
        Map<String, Object> map = ThreadLocalUtil.get();
        if (map == null) {
            throw new RuntimeException("用户未登录");
        }
        //封装参数
        User user = new User();
        user.setId((Integer) map.get("id"));
        user.setNumber((Integer) map.get("number"));
        user.setName((String) map.get("name"));
        user.setIsAdmin((Integer) map.get("isAdmin"));
        //回传参数
        return Result.success(user);
    }

    //更改密码
    @RequestMapping("/updatePwd")
    public Result<String> updatePwd(String pass,String checkPass) {
        System.out.println(pass);
        System.out.println(checkPass);
        //获取前端传递参数进行校验
        if (!StringUtils.hasLength(pass)||!StringUtils.hasLength(checkPass)){
            return Result.error("密码与校验密码不能为空");
        }
        if(!pass.equals(checkPass)){
            return Result.error("两次密码不一致");
        }
        //获取用户id
        Map<String,Object> claims = ThreadLocalUtil.get();
        int id= (Integer) claims.get("id");
        String passwrod = Md5Util.getMD5String(pass);
        //修改密码
        userService.updatePwd(id,passwrod);
        return Result.success("修改成功");
    }
}
