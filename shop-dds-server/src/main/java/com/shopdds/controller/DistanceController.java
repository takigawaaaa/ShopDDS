package com.shopdds.controller;

import com.shopdds.common.Result;
import com.shopdds.entity.Distance;
import com.shopdds.service.DistanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 距离控制器
 */
@RestController
@RequestMapping("/distances")
@RequiredArgsConstructor
public class DistanceController {

    private final DistanceService distanceService;

    @GetMapping
    public Result<List<Distance>> list() {
        return Result.success(distanceService.list());
    }

    @PostMapping
    @PreAuthorize("hasRole('HEAD_ADMIN')")
    public Result<Void> addOrUpdate(@RequestBody Distance d) {
        distanceService.addOrUpdate(d.getWarehouseId(), d.getSupermarketId(), d.getDistance());
        return Result.success();
    }

    @DeleteMapping
    @PreAuthorize("hasRole('HEAD_ADMIN')")
    public Result<Void> deleteAll() {
        distanceService.deleteAll();
        return Result.success();
    }

    @DeleteMapping("/{warehouseId}/{supermarketId}")
    @PreAuthorize("hasRole('HEAD_ADMIN')")
    public Result<Void> deleteOne(@PathVariable String warehouseId, @PathVariable String supermarketId) {
        distanceService.deleteOne(warehouseId, supermarketId);
        return Result.success();
    }
}