package com.hyt.regio.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyt.regio.common.R;
import com.hyt.regio.entity.Employee;
import com.hyt.regio.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

//后台系统

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    /*
    * 后台系统员工登录功能
    * */
    @PostMapping("/login")
    private R<Employee> login(@RequestBody Employee employee, HttpServletRequest request) {
        //将页面提交的密码进行加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //查询数据库中是否有提交的用户名
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        //没有
        if (emp == null) {
            return R.error("登录失败，无此用户");
        }
        //密码比对
        if (!emp.getPassword().equals(password)) {//不一致
            return R.error("密码不一致");
        }
        //查看状态status是否禁用
        if (emp.getStatus() == 0) {
            return R.error("该用户已被禁用");
        }
        //登录成功，存session
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    /*退出登录功能*/
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /*新增员工功能*/
    @PostMapping
    public R<String> save(@RequestBody Employee employee, HttpServletRequest request) {
        log.info("新增员工，员工  信息：{}", employee.toString());
        //设置初始密码
        String passWord = DigestUtils.md5DigestAsHex("123456".getBytes());
        employee.setPassword(passWord);

        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        Long empId = (Long) request.getSession().getAttribute("employee");
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);

        employeeService.save(employee);
        return R.success("新增员工成功");
    }

    /*员工信息分页查询*/
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("page:{}, pageSize:{}, name:{}", page, pageSize, name);

        //分页构造器
        Page pageInfo = new Page(page, pageSize);

        //条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        //添加排序条件，按字母降序
        queryWrapper.orderByDesc(Employee::getName);

        employeeService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /*根据id修改员工信息*/
//    @PutMapping
//    public R<String> update(@RequestBody Employee employee, HttpServletRequest request) {
//
//
//        return null;
//    }
}
