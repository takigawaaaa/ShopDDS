package com.shopdds.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shopdds.common.BusinessException;
import com.shopdds.common.RoleEnum;
import com.shopdds.dto.LoginRequest;
import com.shopdds.dto.LoginResponse;
import com.shopdds.entity.Employee;
import com.shopdds.entity.HeadAdmin;
import com.shopdds.entity.Supermarket;
import com.shopdds.entity.Warehouse;
import com.shopdds.mapper.EmployeeMapper;
import com.shopdds.mapper.HeadAdminMapper;
import com.shopdds.mapper.SupermarketMapper;
import com.shopdds.mapper.WarehouseMapper;
import com.shopdds.security.JwtTokenProvider;
import com.shopdds.security.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 认证服务：登录、修改密码
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final HeadAdminMapper headAdminMapper;
    private final EmployeeMapper employeeMapper;
    private final WarehouseMapper warehouseMapper;
    private final SupermarketMapper supermarketMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 统一登录
     */
    public LoginResponse login(LoginRequest req) {
        RoleEnum role;
        try {
            role = RoleEnum.valueOf(req.getRole());
        } catch (IllegalArgumentException e) {
            throw new BusinessException("未知角色：" + req.getRole());
        }

        return switch (role) {
            case HEAD_ADMIN -> loginHeadAdmin(req, role);
            case WAREHOUSE_ADMIN, SUPERMARKET_ADMIN -> loginEmployee(req, role);
        };
    }

    private LoginResponse loginHeadAdmin(LoginRequest req, RoleEnum role) {
        HeadAdmin admin = headAdminMapper.selectById(req.getUsername());
        if (admin == null) {
            throw new BusinessException("账号或密码错误");
        }
        if (!passwordEncoder.matches(req.getPassword(), admin.getHeadadminPwd())) {
            throw new BusinessException("账号或密码错误");
        }
        LoginUser user = LoginUser.builder()
                .userId(admin.getHeadadminId())
                .name(admin.getHeadadminName())
                .role(role)
                .build();
        return toResponse(jwtTokenProvider.generate(user), user);
    }

    private LoginResponse loginEmployee(LoginRequest req, RoleEnum role) {
        Employee emp = employeeMapper.selectById(req.getUsername());
        if (emp == null) {
            throw new BusinessException("账号或密码错误");
        }
        if (!passwordEncoder.matches(req.getPassword(), emp.getEmployeePwd())) {
            throw new BusinessException("账号或密码错误");
        }
        // 校验身份是否匹配
        RoleEnum actual = RoleEnum.fromCnName(emp.getEmployeeIde());
        if (actual != role) {
            throw new BusinessException("该账号无权以" + role.getCnName() + "身份登录");
        }

        String facilityId = null;
        String facilityName = null;
        if (role == RoleEnum.WAREHOUSE_ADMIN) {
            Warehouse wh = warehouseMapper.selectOne(new LambdaQueryWrapper<Warehouse>()
                    .eq(Warehouse::getEmployeeId, emp.getEmployeeId()));
            if (wh == null) {
                throw new BusinessException("该仓库管理员未关联仓库");
            }
            facilityId = wh.getWarehouseId();
            facilityName = wh.getWarehouseName();
        } else {
            Supermarket sm = supermarketMapper.selectOne(new LambdaQueryWrapper<Supermarket>()
                    .eq(Supermarket::getEmployeeId, emp.getEmployeeId()));
            if (sm == null) {
                throw new BusinessException("该超市管理员未关联超市");
            }
            facilityId = sm.getSupermarketId();
            facilityName = sm.getSupermarketName();
        }

        LoginUser user = LoginUser.builder()
                .userId(emp.getEmployeeId())
                .name(emp.getEmployeeName())
                .role(role)
                .facilityId(facilityId)
                .facilityName(facilityName)
                .build();
        return toResponse(jwtTokenProvider.generate(user), user);
    }

    /**
     * 总部管理员修改密码
     */
    public void changeHeadAdminPassword(String headadminId, String oldPwd, String newPwd) {
        HeadAdmin admin = headAdminMapper.selectById(headadminId);
        if (admin == null) {
            throw new BusinessException("用户不存在");
        }
        if (!passwordEncoder.matches(oldPwd, admin.getHeadadminPwd())) {
            throw new BusinessException("原密码错误");
        }
        admin.setHeadadminPwd(passwordEncoder.encode(newPwd));
        headAdminMapper.updateById(admin);
    }

    /**
     * 员工修改密码
     */
    public void changeEmployeePassword(String employeeId, String oldPwd, String newPwd) {
        Employee emp = employeeMapper.selectById(employeeId);
        if (emp == null) {
            throw new BusinessException("用户不存在");
        }
        if (!passwordEncoder.matches(oldPwd, emp.getEmployeePwd())) {
            throw new BusinessException("原密码错误");
        }
        emp.setEmployeePwd(passwordEncoder.encode(newPwd));
        employeeMapper.updateById(emp);
    }

    private LoginResponse toResponse(String token, LoginUser user) {
        LoginResponse resp = new LoginResponse();
        resp.setToken(token);
        resp.setUserId(user.getUserId());
        resp.setName(user.getName());
        resp.setRole(user.getRole().getCode());
        resp.setFacilityId(user.getFacilityId());
        resp.setFacilityName(user.getFacilityName());
        return resp;
    }
}