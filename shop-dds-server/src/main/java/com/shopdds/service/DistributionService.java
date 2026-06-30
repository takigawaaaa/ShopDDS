package com.shopdds.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shopdds.entity.AllApplication;
import com.shopdds.entity.AllInventory;
import com.shopdds.entity.Assign;
import com.shopdds.mapper.AllApplicationMapper;
import com.shopdds.mapper.AllInventoryMapper;
import com.shopdds.mapper.ApplicationMapper;
import com.shopdds.mapper.AssignMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 物资分配服务
 * <p>
 * 对应原 {@code DistributeServlet}：当总库存 >= 总申请时按需足额分配；
 * 当总库存 &lt; 总申请时按比例缩减，剩余库存按余数从大到小分配。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DistributionService {

    private final AllInventoryMapper allInventoryMapper;
    private final AllApplicationMapper allApplicationMapper;
    private final ApplicationMapper applicationMapper;
    private final AssignMapper assignMapper;

    /**
     * 执行分配，返回分配的超市×商品条数
     */
    @Transactional
    public int execute() {
        // 1. 获取总库存
        Map<String, Integer> totalInventory = new HashMap<>();
        for (AllInventory inv : allInventoryMapper.selectList(null)) {
            totalInventory.put(inv.getCommodityId(), inv.getAllinvAmount());
        }

        // 2. 获取总申请
        Map<String, Integer> totalApplication = new HashMap<>();
        for (AllApplication apl : allApplicationMapper.selectList(null)) {
            totalApplication.put(apl.getCommodityId(), apl.getAllaplAmount());
        }

        // 3. 获取各超市的单品申请
        List<com.shopdds.entity.Application> allApl = applicationMapper.findAll();
        Map<String, Map<String, Integer>> supermarketApplications = new HashMap<>();
        for (com.shopdds.entity.Application apl : allApl) {
            supermarketApplications
                    .computeIfAbsent(apl.getSupermarketId(), k -> new HashMap<>())
                    .put(apl.getCommodityId(), apl.getAplAmount());
        }

        List<Assign> batch = new ArrayList<>();

        for (String commodityId : totalApplication.keySet()) {
            int totalApl = totalApplication.getOrDefault(commodityId, 0);
            int totalInv = totalInventory.getOrDefault(commodityId, 0);
            if (totalApl == 0) continue; // 避免除零

            if (totalInv >= totalApl) {
                // 供给充足：按申请量足额分配
                for (String supermarketId : supermarketApplications.keySet()) {
                    int aplAmount = supermarketApplications.get(supermarketId).getOrDefault(commodityId, 0);
                    if (aplAmount > 0) {
                        batch.add(new Assign(supermarketId, commodityId, aplAmount));
                    }
                }
            } else {
                // 供给不足：等比例缩减 + 最大余数法
                double scaleFactor = (double) totalInv / totalApl;
                int totalAssigned = 0;
                Map<String, Integer> assignAmounts = new HashMap<>();
                List<Map.Entry<String, Double>> remainders = new ArrayList<>();

                for (String supermarketId : supermarketApplications.keySet()) {
                    int aplAmount = supermarketApplications.get(supermarketId).getOrDefault(commodityId, 0);
                    double exactAmount = aplAmount * scaleFactor;
                    int assignAmount = (int) Math.floor(exactAmount);

                    totalAssigned += assignAmount;
                    remainders.add(new AbstractMap.SimpleEntry<>(supermarketId, exactAmount - assignAmount));
                    assignAmounts.put(supermarketId, assignAmount);
                }

                // 按余数从大到小分配剩余库存
                int remainingInventory = totalInv - totalAssigned;
                remainders.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));
                int distributeCount = Math.min(remainingInventory, remainders.size());
                for (int i = 0; i < distributeCount; i++) {
                    String supermarketId = remainders.get(i).getKey();
                    assignAmounts.put(supermarketId, assignAmounts.get(supermarketId) + 1);
                }

                // 加入批处理
                for (Map.Entry<String, Integer> e : assignAmounts.entrySet()) {
                    int amt = e.getValue();
                    if (amt > 0) {
                        batch.add(new Assign(e.getKey(), commodityId, amt));
                    }
                }
            }
        }

        if (!batch.isEmpty()) {
            assignMapper.batchUpsert(batch);
        }
        log.info("物资分配完成，共 {} 条分配记录", batch.size());
        return batch.size();
    }

    /**
     * 查询全部分配结果
     */
    public List<Assign> findAll() {
        return assignMapper.findAll();
    }

    /**
     * 人工干预：修改某条分配数量
     */
    @Transactional
    public void update(String supermarketId, String commodityId, Integer amount) {
        assignMapper.upsert(supermarketId, commodityId, amount);
    }

    /**
     * 清空分配表
     */
    @Transactional
    public void deleteAll() {
        assignMapper.deleteAll();
    }

    /**
     * 查询有分配记录的商品 ID（DISTINCT）—— 供调拨使用
     */
    public List<String> distinctCommodityIds() {
        return assignMapper.distinctCommodityIds();
    }

    /**
     * 按商品查询分配（供调拨作为需求方使用）
     */
    public List<Assign> findByCommodity(String commodityId) {
        return assignMapper.findByCommodity(commodityId);
    }

    /**
     * 按超市查询分配
     */
    public List<Assign> findBySupermarket(String supermarketId) {
        return assignMapper.selectList(new LambdaQueryWrapper<Assign>()
                .eq(Assign::getSupermarketId, supermarketId));
    }
}