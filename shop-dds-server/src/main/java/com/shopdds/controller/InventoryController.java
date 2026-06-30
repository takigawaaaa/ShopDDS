package com.shopdds.controller;

import com.shopdds.common.Result;
import com.shopdds.security.CurrentUser;
import com.shopdds.security.LoginUser;
import com.shopdds.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 仓库库存控制器（仓库管理员提交库存）
 */
@RestController
@RequestMapping("/warehouse/inventories")
@RequiredArgsConstructor
@PreAuthorize("hasRole('WAREHOUSE_ADMIN')")
public class InventoryController {

    private final InventoryService inventoryService;

    /**
     * 提交库存：商品 + 数量，仓库编号从登录用户解析
     */
    @PostMapping
    public Result<Void> submit(@CurrentUser LoginUser user, @RequestBody Map<String, Object> body) {
        String commodityId = (String) body.get("commodityId");
        Integer amount = toInt(body.get("amount"));
        inventoryService.submit(commodityId, user.getFacilityId(), amount);
        return Result.success();
    }

    private static Integer toInt(Object o) {
        return o == null ? null : ((Number) o).intValue();
    }
}