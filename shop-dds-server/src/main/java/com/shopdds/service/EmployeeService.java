package com.shopdds.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shopdds.common.BusinessException;
import com.shopdds.entity.Employee;
import com.shopdds.mapper.EmployeeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 员工服务（含仓库管理员 / 超市管理员）
 */
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeMapper employeeMapper;
    private final PasswordEncoder passwordEncoder;

    public List<Employee> list() {
        return employeeMapper.selectList(new LambdaQueryWrapper<Employee>()
                .orderByAsc(Employee::getEmployeeId));
    }

    /** 仅返回某身份的员工（仓库管理员 / 超市管理员） */
    public List<Employee> listByIde(String ide) {
        return employeeMapper.selectList(new LambdaQueryWrapper<Employee>()
                .eq(Employee::getEmployeeIde, ide)
                .orderByAsc(Employee::getEmployeeId));
    }

    public Employee get(String employeeId) {
        return employeeMapper.selectById(employeeId);
    }

    public void add(Employee e) {
        if (employeeMapper.selectById(e.getEmployeeId()) != null) {
            throw new BusinessException("员工编号已存在");
        }
        // 密码 BCrypt 加密
        if (e.getEmployeePwd() != null && !e.getEmployeePwd().isEmpty()) {
            e.setEmployeePwd(passwordEncoder.encode(e.getEmployeePwd()));
        }
        employeeMapper.insert(e);
    }

    public void delete(String employeeId) {
        // 外键约束由数据库处理：若该员工已关联仓库/超市，删除会失败
        try {
            employeeMapper.deleteById(employeeId);
        } catch (Exception ex) {
            throw new BusinessException("删除失败：该员工可能已关联仓库或超市");
        }
    }
}