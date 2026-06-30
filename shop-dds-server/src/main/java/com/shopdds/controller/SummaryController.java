package com.shopdds.controller;

import com.shopdds.common.Result;
import com.shopdds.dto.SummaryResult;
import com.shopdds.service.SummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 数据汇总控制器
 */
@RestController
@RequestMapping("/summary")
@RequiredArgsConstructor
@PreAuthorize("hasRole('HEAD_ADMIN')")
public class SummaryController {

    private final SummaryService summaryService;

    /** 执行汇总 */
    @PostMapping("/aggregate")
    public Result<Void> aggregate() {
        summaryService.aggregate();
        return Result.success("数据汇总成功");
    }

    /** 查看汇总结果 */
    @GetMapping
    public Result<SummaryResult> get() {
        return Result.success(summaryService.getSummary());
    }

    @DeleteMapping("/inventory")
    public Result<Void> clearInventory() {
        summaryService.clearAllInventory();
        return Result.success();
    }

    @DeleteMapping("/application")
    public Result<Void> clearApplication() {
        summaryService.clearAllApplication();
        return Result.success();
    }
}