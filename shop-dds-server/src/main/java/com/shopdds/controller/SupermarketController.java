package com.shopdds.controller;

import com.shopdds.common.Result;
import com.shopdds.entity.Supermarket;
import com.shopdds.service.SupermarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 超市控制器
 */
@RestController
@RequestMapping("/supermarkets")
@RequiredArgsConstructor
public class SupermarketController {

    private final SupermarketService supermarketService;

    @GetMapping
    public Result<List<Supermarket>> list() {
        return Result.success(supermarketService.list());
    }

    @PostMapping
    @PreAuthorize("hasRole('HEAD_ADMIN')")
    public Result<Void> add(@RequestBody Supermarket s) {
        supermarketService.add(s);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('HEAD_ADMIN')")
    public Result<Void> delete(@PathVariable String id) {
        supermarketService.delete(id);
        return Result.success();
    }

    @PatchMapping("/{id}/name")
    @PreAuthorize("hasRole('HEAD_ADMIN')")
    public Result<Void> updateName(@PathVariable String id, @RequestBody Map<String, String> body) {
        supermarketService.updateName(id, body.get("name"));
        return Result.success();
    }

    @PatchMapping("/{id}/address")
    @PreAuthorize("hasRole('HEAD_ADMIN')")
    public Result<Void> updateAddress(@PathVariable String id, @RequestBody Map<String, String> body) {
        supermarketService.updateAddress(id, body.get("address"));
        return Result.success();
    }

    @PatchMapping("/{id}/manager")
    @PreAuthorize("hasRole('HEAD_ADMIN')")
    public Result<Void> updateManager(@PathVariable String id, @RequestBody Map<String, String> body) {
        supermarketService.updateManager(id, body.get("employeeId"));
        return Result.success();
    }
}