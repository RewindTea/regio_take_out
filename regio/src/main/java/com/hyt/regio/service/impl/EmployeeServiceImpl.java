package com.hyt.regio.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyt.regio.entity.Employee;
import com.hyt.regio.mapper.EmployeeMapper;
import com.hyt.regio.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
