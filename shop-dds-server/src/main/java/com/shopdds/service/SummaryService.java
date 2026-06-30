package com.shopdds.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shopdds.dto.SummaryResult;
import com.shopdds.entity.AllApplication;
import com.shopdds.entity.AllInventory;
import com.shopdds.mapper.AllApplicationMapper;
import com.shopdds.mapper.AllInventoryMapper;
import com.shopdds.mapper.ApplicationMapper;
import com.shopdds.mapper.InventoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 数据汇总服务
 * <p>
 * 将 inv（仓库库存）按商品汇总到 allinv，将 apl（超市申请）按商品汇总到 allapl。
 * 对应原 {@code SummarizeDataServlet}。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SummaryService {

    private final InventoryMapper inventoryMapper;
    private final ApplicationMapper applicationMapper;
    private final AllInventoryMapper allInventoryMapper;
    private final AllApplicationMapper allApplicationMapper;

    /**
     * 执行汇总：inv→allinv，apl→allapl
     */
    @Transactional
    public void aggregate() {
        inventoryMapper.summarizeToAllinv();
        applicationMapper.summarizeToAllapl();
        log.info("数据汇总完成");
    }

    /**
     * 查询汇总结果
     */
    public SummaryResult getSummary() {
        List<AllInventory> inv = allInventoryMapper.selectList(new LambdaQueryWrapper<AllInventory>()
                .orderByAsc(AllInventory::getCommodityId));
        List<AllApplication> apl = allApplicationMapper.selectList(new LambdaQueryWrapper<AllApplication>()
                .orderByAsc(AllApplication::getCommodityId));

        SummaryResult result = new SummaryResult();
        result.setInventorySummary(inv.stream()
                .map(i -> new SummaryResult.AllInventory(i.getCommodityId(), i.getAllinvAmount()))
                .toList());
        result.setApplicationSummary(apl.stream()
                .map(a -> new SummaryResult.AllApplication(a.getCommodityId(), a.getAllaplAmount()))
                .toList());
        return result;
    }

    /**
     * 清空总库存汇总
     */
    @Transactional
    public void clearAllInventory() {
        inventoryMapper.clearAllinv();
    }

    /**
     * 清空总申请汇总
     */
    @Transactional
    public void clearAllApplication() {
        allApplicationMapper.delete(new LambdaQueryWrapper<>());
    }
}