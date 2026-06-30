package com.shopdds.service;

import com.shopdds.mapper.InventoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 库存服务（仓库管理员提交库存）
 */
@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryMapper inventoryMapper;

    /**
     * 提交库存（仓库管理员调用）
     */
    public void submit(String commodityId, String warehouseId, Integer amount) {
        if (amount == null || amount < 0) {
            throw new com.shopdds.common.BusinessException("库存量必须为非负数");
        }
        inventoryMapper.upsert(commodityId, warehouseId, amount);
    }
}