package com.shopdds.controller;

import com.shopdds.common.Result;
import com.shopdds.entity.Allocate;
import com.shopdds.security.CurrentUser;
import com.shopdds.security.LoginUser;
import com.shopdds.service.AllocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 物资调拨控制器（Vogel 运输问题求解）
 */
@RestController
@RequestMapping("/allocation")
@RequiredArgsConstructor
public class AllocationController {

    private final AllocationService allocationService;

    /** 执行调拨（仅总部管理员） */
    @PostMapping("/execute")
    @PreAuthorize("hasRole('HEAD_ADMIN')")
    public Result<AllocationService.Result> execute() {
        return Result.success(allocationService.execute());
    }

    /** 全部调拨结果（总部查看） */
    @GetMapping("/allocations")
    @PreAuthorize("hasRole('HEAD_ADMIN')")
    public Result<List<Allocate>> listAll() {
        return Result.success(allocationService.findAll());
    }

    /** 仓库发物表（仓库管理员查看本仓库） */
    @GetMapping("/warehouse/allocations")
    @PreAuthorize("hasRole('WAREHOUSE_ADMIN')")
    public Result<List<Allocate>> listByWarehouse(@CurrentUser LoginUser user) {
        return Result.success(allocationService.findByWarehouse(user.getFacilityId()));
    }

    /** 超市收物表（超市管理员查看本超市） */
    @GetMapping("/supermarket/allocations")
    @PreAuthorize("hasRole('SUPERMARKET_ADMIN')")
    public Result<List<Allocate>> listBySupermarket(@CurrentUser LoginUser user) {
        return Result.success(allocationService.findBySupermarket(user.getFacilityId()));
    }

    /** 人工干预：修改某条调拨数量 */
    @PutMapping("/allocations")
    @PreAuthorize("hasRole('HEAD_ADMIN')")
    public Result<Void> update(@RequestBody Map<String, Object> body) {
        String supermarketId = (String) body.get("supermarketId");
        String warehouseId = (String) body.get("warehouseId");
        String commodityId = (String) body.get("commodityId");
        Integer amount = toInt(body.get("allocateAmount"));
        allocationService.updateAmount(supermarketId, warehouseId, commodityId, amount);
        return Result.success();
    }

    private static Integer toInt(Object o) {
        return o == null ? null : ((Number) o).intValue();
    }

    /** 清空调拨表 */
    @DeleteMapping("/allocations")
    @PreAuthorize("hasRole('HEAD_ADMIN')")
    public Result<Void> deleteAll() {
        allocationService.deleteAll();
        return Result.success();
    }
}