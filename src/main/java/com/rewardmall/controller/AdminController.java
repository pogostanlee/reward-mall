package com.rewardmall.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rewardmall.mapper.InventoryMapper;
import com.rewardmall.mapper.ProductMapper;
import com.rewardmall.mapper.UserMapper;
import com.rewardmall.pojo.*;
import com.rewardmall.pojo.VO.CustomerQueryVO;
import com.rewardmall.pojo.VO.DepositQueryVO;
import com.rewardmall.pojo.VO.InboundQueryVO;
import com.rewardmall.service.AdminService;
import com.rewardmall.service.CuntomerService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/admin")
@RestController
public class AdminController {
    @Autowired
    private CuntomerService customerService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private InventoryMapper inventoryMapper;
    @Autowired
    private AdminService adminService;

    //获取所有用户信息
    @RequestMapping("/customerList")
    public PageBean<Customer> customerList(CustomerQueryVO customerQueryVO) {
        //获取前端传递数据进行查询
        PageBean<Customer> customerPageBean = customerService.adminList(customerQueryVO);
        return customerPageBean;
    }

    //获取所有支行信息
    @RequestMapping("/branchList")
    public Result<List<User>> branchList() {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();

        //获取前端传递数据进行查询
        List<User> list = userMapper.selectList(null);
        //封装返回数据
        return Result.success(list);
    }

    //获取所有商品信息
    @RequestMapping("/allProductsList")
    public PageBean<Product> allProductsList(String name, Long currentPage, Long pageSize) {
        //获取前端传递数据进行查询
        Page<Product> productPage = new Page<>(currentPage, pageSize);
        QueryWrapper<Product> productQueryWrapper = new QueryWrapper<>();
        if (name != null) {
            productQueryWrapper.like("name", name);
        }
        Page<Product> pageInfo = productMapper.selectPage(productPage, productQueryWrapper);
        PageBean<Product> pageBean = new PageBean<>();
        pageBean.setItems(pageInfo.getRecords());
        pageBean.setTotal(pageInfo.getTotal());
        return pageBean;
    }

    //修改商品信息
    @RequestMapping("/updateProduct")
    public Result<String> updateProduct(Product product) {
        //获取前端传递数据进行修改
        productMapper.updateById(product);
        return Result.success("修改成功");
    }

    //添加商品信息
    @RequestMapping("/addProduct")
    public Result<String> addProduct(Product product) {
        //根据name查询是否有重名
        QueryWrapper<Product> productQueryWrapper = new QueryWrapper<>();
        productQueryWrapper.eq("name", product.getName());
        Product product1 = productMapper.selectOne(productQueryWrapper);
        if (product1 != null) {
            return Result.error("商品已存在");
        }
        //获取前端传递数据进行添加
        productMapper.insert(product);
        return Result.success("添加成功");
    }

    //获取所有库存信息
    @RequestMapping("/getInventory")
    public PageBean<Inventory> allProductsList(Integer productId, Integer branchId, Long currentPage, Long pageSize) {
        //获取前端传递数据进行查询
        Page<Inventory> inventoryPage = new Page<>(currentPage, pageSize);
        QueryWrapper<Inventory> inventoryQueryWrapper = new QueryWrapper<>();
        if (productId != null) {
            inventoryQueryWrapper.eq("productId", productId);
        }
        if (branchId != null) {
            inventoryQueryWrapper.eq("branchId", branchId);
        }
        Page<Inventory> pageInfo = inventoryMapper.selectPage(inventoryPage, inventoryQueryWrapper);
        PageBean<Inventory> pageBean = new PageBean<>();
        pageBean.setItems(pageInfo.getRecords());
        pageBean.setTotal(pageInfo.getTotal());
        return pageBean;
    }

    //添加库存信息
    @RequestMapping("/addInventory")
    public Result<String> addInventory(Integer productId, Integer branchId, Integer quantity) {
        Result<String> result = adminService.addInventory(productId, branchId, quantity);
        return result;
    }

    //全量导出用户信息
    @RequestMapping("/exportCustomer")
    public void exportCustomer(HttpServletResponse response) {
        adminService.exportCustomer(response);
    }

    //查询所有存款信息
    @RequestMapping("/getDeposit")
    public PageBean<Deposit> getDeposit(DepositQueryVO depositQueryVO, Long currentPage, Long pageSize) {
        PageBean<Deposit> depositPageBean = adminService.getAllDeposit(depositQueryVO, currentPage, pageSize);
        return depositPageBean;
    }

    //导出所有存款信息
    @RequestMapping("/exportDeposit")
    public void exportDeposit(HttpServletResponse response,DepositQueryVO depositQueryVO) {
        System.out.println(depositQueryVO);
        adminService.exportDeposit(response, depositQueryVO);
    }

    //获取所有入库记录
    @RequestMapping("/inbound")
    public PageBean<InboundRecord> inbound(InboundQueryVO inboundQueryVO, Long currentPage, Long pageSize) {
        PageBean<InboundRecord> pageBean = adminService.getInboundList(inboundQueryVO, currentPage, pageSize);
        return pageBean;
    }

    //获取上交产品记录
    @RequestMapping("/rebound")
    public PageBean<ReboundRecords> rebound(InboundQueryVO inboundQueryVO, Long currentPage, Long pageSize) {
        PageBean<ReboundRecords> pageBean = adminService.getReboundList(inboundQueryVO, currentPage, pageSize);
        return pageBean;
    }

    //添加上交产品记录
    @RequestMapping("/addRebound")
    public Result<String> addRebound(Integer productId, Integer branchId, Integer quantity) {
        //获取前端传递数据进行添加
        Result<String> result = adminService.addRebound(productId, branchId, quantity);
        return result;
    }
}