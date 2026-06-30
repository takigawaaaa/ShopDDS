package com.shopdds.controller;

import com.shopdds.common.Result;
import com.shopdds.service.CommonService;
import com.shopdds.dto.DropdownItem;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 下拉框数据控制器
 */
@RestController
@RequestMapping("/common")
@RequiredArgsConstructor
public class CommonController {

    private final CommonService commonService;

    @GetMapping("/commodities")
    public Result<List<DropdownItem>> commodities() {
        return Result.success(commonService.commodities());
    }

    @GetMapping("/warehouses")
    public Result<List<DropdownItem>> warehouses() {
        return Result.success(commonService.warehouses());
    }

    @GetMapping("/supermarkets")
    public Result<List<DropdownItem>> supermarkets() {
        return Result.success(commonService.supermarkets());
    }

    @GetMapping("/employees")
    public Result<List<DropdownItem>> employees() {
        return Result.success(commonService.employees());
    }

    @GetMapping("/headadmins")
    public Result<List<DropdownItem>> headAdmins() {
        return Result.success(commonService.headAdmins());
    }

    @GetMapping("/warehouse-managers")
    public Result<List<DropdownItem>> warehouseManagers() {
        return Result.success(commonService.warehouseManagers());
    }

    @GetMapping("/supermarket-managers")
    public Result<List<DropdownItem>> supermarketManagers() {
        return Result.success(commonService.supermarketManagers());
    }
}