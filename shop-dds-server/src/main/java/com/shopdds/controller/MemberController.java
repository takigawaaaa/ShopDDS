package com.shopdds.controller;

import com.shopdds.common.Result;
import com.shopdds.entity.Member;
import com.shopdds.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 会员控制器
 */
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    @PreAuthorize("hasRole('HEAD_ADMIN')")
    public Result<List<Member>> list() {
        return Result.success(memberService.list());
    }

    /** 增加积分（消费金额按比例折算积分） */
    @PatchMapping("/points")
    @PreAuthorize("hasAnyRole('HEAD_ADMIN','SUPERMARKET_ADMIN')")
    public Result<Void> addPoints(@RequestBody Map<String, Object> body) {
        String cardnum = (String) body.get("memberCardnum");
        Integer delta = toInt(body.get("points"));
        memberService.addPoints(cardnum, delta);
        return Result.success();
    }

    private static Integer toInt(Object o) {
        return o == null ? null : ((Number) o).intValue();
    }
}