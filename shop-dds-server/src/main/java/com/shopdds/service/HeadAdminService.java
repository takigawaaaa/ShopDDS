package com.shopdds.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shopdds.common.BusinessException;
import com.shopdds.entity.HeadAdmin;
import com.shopdds.mapper.HeadAdminMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 总部管理员服务
 */
@Service
@RequiredArgsConstructor
public class HeadAdminService {

    private final HeadAdminMapper headAdminMapper;
    private final PasswordEncoder passwordEncoder;

    public List<HeadAdmin> list() {
        return headAdminMapper.selectList(new LambdaQueryWrapper<HeadAdmin>()
                .orderByAsc(HeadAdmin::getHeadadminId));
    }

    public void add(HeadAdmin a) {
        if (headAdminMapper.selectById(a.getHeadadminId()) != null) {
            throw new BusinessException("管理员编号已存在");
        }
        if (a.getHeadadminPwd() != null && !a.getHeadadminPwd().isEmpty()) {
            a.setHeadadminPwd(passwordEncoder.encode(a.getHeadadminPwd()));
        }
        headAdminMapper.insert(a);
    }

    public void delete(String headadminId) {
        headAdminMapper.deleteById(headadminId);
    }
}