package com.shopdds.controller;

import com.shopdds.common.Result;
import com.shopdds.entity.HeadAdmin;
import com.shopdds.service.HeadAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 总部管理员控制器
 */
@RestController
@RequestMapping("/headadmins")
@RequiredArgsConstructor
@PreAuthorize("hasRole('HEAD_ADMIN')")
public class HeadAdminController {

    private final HeadAdminService headAdminService;

    @GetMapping
    public Result<List<HeadAdmin>> list() {
        return Result.success(headAdminService.list());
    }

    @PostMapping
    public Result<Void> add(@RequestBody HeadAdmin a) {
        headAdminService.add(a);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable String id) {
        headAdminService.delete(id);
        return Result.success();
    }
}