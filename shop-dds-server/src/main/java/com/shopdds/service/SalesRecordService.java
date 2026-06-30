package com.shopdds.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shopdds.entity.SalesRecord;
import com.shopdds.mapper.SalesRecordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 销售记录服务
 */
@Service
@RequiredArgsConstructor
public class SalesRecordService {

    private final SalesRecordMapper salesRecordMapper;

    public List<SalesRecord> list() {
        return salesRecordMapper.selectList(new LambdaQueryWrapper<SalesRecord>()
                .orderByDesc(SalesRecord::getSaleTime));
    }
}