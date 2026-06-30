package com.shopdds.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shopdds.entity.Member;
import com.shopdds.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 会员服务
 */
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberMapper memberMapper;

    public List<Member> list() {
        return memberMapper.selectList(new LambdaQueryWrapper<Member>()
                .orderByAsc(Member::getMemberId));
    }

    /**
     * 增加积分：points = points + delta WHERE cardNumber = ?
     */
    public void addPoints(String memberCardnum, Integer delta) {
        Member m = memberMapper.selectOne(new LambdaQueryWrapper<Member>()
                .eq(Member::getMemberCardnum, memberCardnum));
        if (m == null) {
            throw new com.shopdds.common.BusinessException("会员卡号不存在");
        }
        m.setMemberPoints((m.getMemberPoints() == null ? 0 : m.getMemberPoints()) + delta);
        memberMapper.updateById(m);
    }
}