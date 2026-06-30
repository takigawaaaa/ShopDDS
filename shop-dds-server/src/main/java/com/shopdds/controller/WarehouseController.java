package com.shopdds.controller;

import com.shopdds.common.Result;
import com.shopdds.entity.Warehouse;
import com.shopdds.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 仓库控制器
 */
@RestController
@RequestMapping("/warehouses")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;

    @GetMapping
    public Result<List<Warehouse>> list() {
        return Result.success(warehouseService.list());
    }

    @PostMapping
    @PreAuthorize("hasRole('HEAD_ADMIN')")
    public Result<Void> add(@RequestBody Warehouse w) {
        warehouseService.add(w);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('HEAD_ADMIN')")
    public Result<Void> delete(@PathVariable String id) {
        warehouseService.delete(id);
        return Result.success();
    }

    @PatchMapping("/{id}/name")
    @PreAuthorize("hasRole('HEAD_ADMIN')")
    public Result<Void> updateName(@PathVariable String id, @RequestBody Map<String, String> body) {
        warehouseService.updateName(id, body.get("name"));
        return Result.success();
    }

    @PatchMapping("/{id}/address")
    @PreAuthorize("hasRole('HEAD_ADMIN')")
    public Result<Void> updateAddress(@PathVariable String id, @RequestBody Map<String, String> body) {
        warehouseService.updateAddress(id, body.get("address"));
        return Result.success();
    }

    @PatchMapping("/{id}/manager")
    @PreAuthorize("hasRole('HEAD_ADMIN')")
    public Result<Void> updateManager(@PathVariable String id, @RequestBody Map<String, String> body) {
        warehouseService.updateManager(id, body.get("employeeId"));
        return Result.success();
    }
}