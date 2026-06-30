package com.shopdds.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shopdds.entity.Employee;
import com.shopdds.entity.HeadAdmin;
import com.shopdds.mapper.EmployeeMapper;
import com.shopdds.mapper.HeadAdminMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 密码迁移 Runner
 * <p>
 * 应用首次启动后自动执行：将 headadmin / employee 表中仍是明文的密码
 * 迁移为 BCrypt 哈希。已加密（以 $2 开头）的密码会被跳过，保证幂等。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PasswordMigrationRunner implements CommandLineRunner {

    private final HeadAdminMapper headAdminMapper;
    private final EmployeeMapper employeeMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        migrateHeadAdmins();
        migrateEmployees();
    }

    private void migrateHeadAdmins() {
        List<HeadAdmin> list = headAdminMapper.selectList(null);
        int count = 0;
        for (HeadAdmin a : list) {
            String pwd = a.getHeadadminPwd();
            if (pwd != null && !pwd.isEmpty() && !pwd.startsWith("$2")) {
                a.setHeadadminPwd(passwordEncoder.encode(pwd));
                headAdminMapper.updateById(a);
                count++;
            }
        }
        if (count > 0) log.info("已迁移 {} 个总部管理员密码为 BCrypt", count);
    }

    private void migrateEmployees() {
        List<Employee> list = employeeMapper.selectList(null);
        int count = 0;
        for (Employee e : list) {
            String pwd = e.getEmployeePwd();
            if (pwd != null && !pwd.isEmpty() && !pwd.startsWith("$2")) {
                e.setEmployeePwd(passwordEncoder.encode(pwd));
                employeeMapper.updateById(e);
                count++;
            }
        }
        if (count > 0) log.info("已迁移 {} 个员工密码为 BCrypt", count);
    }
}