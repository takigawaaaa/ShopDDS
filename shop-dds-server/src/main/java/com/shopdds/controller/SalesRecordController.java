package com.shopdds.controller;

import com.shopdds.common.Result;
import com.shopdds.entity.SalesRecord;
import com.shopdds.service.SalesRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 销售记录控制器
 */
@RestController
@RequestMapping("/sales-records")
@RequiredArgsConstructor
public class SalesRecordController {

    private final SalesRecordService salesRecordService;

    @GetMapping
    @PreAuthorize("hasRole('HEAD_ADMIN')")
    public Result<List<SalesRecord>> list() {
        return Result.success(salesRecordService.list());
    }
}