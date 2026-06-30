package com.shopdds.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 汇总结果项（某个商品的总库存 / 总申请）
 */
@Data
@AllArgsConstructor
public class SummaryItem {

    private String commodityId;
    private Integer totalInventory;
    private Integer totalApplication;
}