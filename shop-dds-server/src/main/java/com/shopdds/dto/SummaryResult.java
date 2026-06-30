package com.shopdds.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 汇总结果：含库存汇总与申请汇总两张表
 */
@Data
public class SummaryResult {

    private List<AllInventory> inventorySummary;
    private List<AllApplication> applicationSummary;

    @Data
    @AllArgsConstructor
    public static class AllInventory {
        private String commodityId;
        private Integer totalInventory;
    }

    @Data
    @AllArgsConstructor
    public static class AllApplication {
        private String commodityId;
        private Integer totalApplication;
    }
}