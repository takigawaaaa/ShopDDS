package com.shopdds.controller;

import com.shopdds.common.Result;
import com.shopdds.entity.Assign;
import com.shopdds.security.CurrentUser;
import com.shopdds.security.LoginUser;
import com.shopdds.service.DistributionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 物资分配控制器
 */
@RestController
@RequestMapping("/distribution")
@RequiredArgsConstructor
public class DistributionController {

    private final DistributionService distributionService;

    /** 执行分配（仅总部管理员） */
    @PostMapping("/execute")
    @PreAuthorize("hasRole('HEAD_ADMIN')")
    public Result<Integer> execute() {
        int count = distributionService.execute();
        return Result.success("物资分配完成，共 " + count + " 条", count);
    }

    /** 全部分配结果（总部查看） */
    @GetMapping("/assigns")
    @PreAuthorize("hasRole('HEAD_ADMIN')")
    public Result<List<Assign>> listAll() {
        return Result.success(distributionService.findAll());
    }

    /** 本超市分配结果 */
    @GetMapping("/supermarket/assigns")
    @PreAuthorize("hasRole('SUPERMARKET_ADMIN')")
    public Result<List<Assign>> listBySupermarket(@CurrentUser LoginUser user) {
        return Result.success(distributionService.findBySupermarket(user.getFacilityId()));
    }

    /** 人工干预：修改某人某商品的分配数量 */
    @PutMapping("/assigns")
    @PreAuthorize("hasRole('HEAD_ADMIN')")
    public Result<Void> update(@RequestBody Map<String, Object> body) {
        String supermarketId = (String) body.get("supermarketId");
        String commodityId = (String) body.get("commodityId");
        Integer amount = toInt(body.get("assignAmount"));
        distributionService.update(supermarketId, commodityId, amount);
        return Result.success();
    }

    private static Integer toInt(Object o) {
        return o == null ? null : ((Number) o).intValue();
    }

    /** 清空分配表 */
    @DeleteMapping("/assigns")
    @PreAuthorize("hasRole('HEAD_ADMIN')")
    public Result<Void> deleteAll() {
        distributionService.deleteAll();
        return Result.success();
    }
}