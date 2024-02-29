package com.hyt.regio.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hyt.regio.common.R;
import com.hyt.regio.entity.Employee;
import com.hyt.regio.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
}
