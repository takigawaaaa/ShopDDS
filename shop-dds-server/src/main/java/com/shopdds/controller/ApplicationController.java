package com.shopdds.controller;

import com.shopdds.common.Result;
import com.shopdds.security.CurrentUser;
import com.shopdds.security.LoginUser;
import com.shopdds.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 超市申请控制器（超市管理员提交需求申请）
 */
@RestController
@RequestMapping("/supermarket/applications")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SUPERMARKET_ADMIN')")
public class ApplicationController {

    private final ApplicationService applicationService;

    /**
     * 提交申请：商品 + 数量，超市编号从登录用户解析
     */
    @PostMapping
    public Result<Void> submit(@CurrentUser LoginUser user, @RequestBody Map<String, Object> body) {
        String commodityId = (String) body.get("commodityId");
        Integer amount = toInt(body.get("amount"));
        applicationService.submit(user.getFacilityId(), commodityId, amount);
        return Result.success();
    }

    private static Integer toInt(Object o) {
        return o == null ? null : ((Number) o).intValue();
    }
}