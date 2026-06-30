package com.shopdds.service;

import com.shopdds.mapper.ApplicationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 申请服务（超市管理员提交需求申请）
 */
@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationMapper applicationMapper;

    /**
     * 提交申请（超市管理员调用）
     */
    public void submit(String supermarketId, String commodityId, Integer amount) {
        if (amount == null || amount < 0) {
            throw new com.shopdds.common.BusinessException("申请量必须为非负数");
        }
        applicationMapper.upsert(supermarketId, commodityId, amount);
    }
}