package com.shopdds.controller;

import com.shopdds.common.Result;
import com.shopdds.dto.LoginRequest;
import com.shopdds.dto.LoginResponse;
import com.shopdds.security.LoginUser;
import com.shopdds.security.CurrentUser;
import com.shopdds.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 认证控制器：登录、修改密码
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /** 统一登录（三角色共用） */
    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
        return Result.success("登录成功", authService.login(req));
    }

    /** 总部管理员修改密码 */
    @PutMapping("/headadmin/password")
    public Result<Void> changeHeadAdminPassword(@CurrentUser LoginUser user,
                                                 @RequestBody Map<String, String> body) {
        authService.changeHeadAdminPassword(user.getUserId(), body.get("oldPassword"), body.get("newPassword"));
        return Result.success();
    }

    /** 员工修改密码 */
    @PutMapping("/employee/password")
    public Result<Void> changeEmployeePassword(@CurrentUser LoginUser user,
                                               @RequestBody Map<String, String> body) {
        authService.changeEmployeePassword(user.getUserId(), body.get("oldPassword"), body.get("newPassword"));
        return Result.success();
    }
}