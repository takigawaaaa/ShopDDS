package com.shopdds.controller;

import com.shopdds.common.Result;
import com.shopdds.entity.Employee;
import com.shopdds.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 员工控制器
 */
@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    @PreAuthorize("hasRole('HEAD_ADMIN')")
    public Result<List<Employee>> list() {
        return Result.success(employeeService.list());
    }

    /** 仅查仓库管理员（供下拉框 / 关联使用） */
    @GetMapping("/warehouse-managers")
    @PreAuthorize("hasRole('HEAD_ADMIN')")
    public Result<List<Employee>> warehouseManagers() {
        return Result.success(employeeService.listByIde("仓库管理员"));
    }

    /** 仅查超市管理员 */
    @GetMapping("/supermarket-managers")
    @PreAuthorize("hasRole('HEAD_ADMIN')")
    public Result<List<Employee>> supermarketManagers() {
        return Result.success(employeeService.listByIde("超市管理员"));
    }

    @PostMapping
    @PreAuthorize("hasRole('HEAD_ADMIN')")
    public Result<Void> add(@RequestBody Employee e) {
        employeeService.add(e);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('HEAD_ADMIN')")
    public Result<Void> delete(@PathVariable String id) {
        employeeService.delete(id);
        return Result.success();
    }
}