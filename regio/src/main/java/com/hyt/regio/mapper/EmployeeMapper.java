package com.hyt.regio.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hyt.regio.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
