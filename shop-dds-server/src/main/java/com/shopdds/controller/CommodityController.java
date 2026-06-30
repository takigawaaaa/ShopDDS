package com.shopdds.controller;

import com.shopdds.common.Result;
import com.shopdds.entity.Commodity;
import com.shopdds.service.CommodityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品控制器
 */
@RestController
@RequestMapping("/commodities")
@RequiredArgsConstructor
public class CommodityController {

    private final CommodityService commodityService;

    @GetMapping
    public Result<List<Commodity>> list() {
        return Result.success(commodityService.list());
    }

    @GetMapping("/{id}")
    public Result<Commodity> get(@PathVariable String id) {
        return Result.success(commodityService.get(id));
    }

    /** 新增商品（仅总部管理员） */
    @PostMapping
    @PreAuthorize("hasRole('HEAD_ADMIN')")
    public Result<Void> add(@RequestBody Commodity c) {
        commodityService.add(c);
        return Result.success();
    }

    /** 修改商品（仅总部管理员） */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('HEAD_ADMIN')")
    public Result<Void> update(@PathVariable String id, @RequestBody Commodity c) {
        c.setCommodityId(id);
        commodityService.update(c);
        return Result.success();
    }

    /** 删除商品（仅总部管理员） */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('HEAD_ADMIN')")
    public Result<Void> delete(@PathVariable String id) {
        commodityService.delete(id);
        return Result.success();
    }
}