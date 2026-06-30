package com.shopdds.service;

import com.shopdds.entity.Allocate;
import com.shopdds.entity.Assign;
import com.shopdds.entity.Distance;
import com.shopdds.entity.Inventory;
import com.shopdds.mapper.AllocateMapper;
import com.shopdds.mapper.DistanceMapper;
import com.shopdds.mapper.InventoryMapper;
import com.shopdds.service.algorithm.VogelTransportSolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 物资调拨服务（运输问题求解）
 * <p>
 * 对应原 {@code AllocationServlet} + {@code util/vogel.java}：
 * 对 assign 表中每个商品，以仓库库存为供应、超市分配量为需求、距离为运价，
 * 用 Vogel 似近法求解运输问题，结果写入 allocate 表。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AllocationService {

    private final VogelTransportSolver vogelSolver;
    private final DistributionService distributionService;
    private final InventoryMapper inventoryMapper;
    private final DistanceMapper distanceMapper;
    private final AllocateMapper allocateMapper;

    /**
     * 执行调拨，返回处理的商品数量与每条结果信息
     */
    @Transactional
    public Result execute() {
        List<String> commodityIds = distributionService.distinctCommodityIds();
        if (commodityIds.isEmpty()) {
            return new Result(0, "没有找到需要处理的商品（请先执行物资分配）", List.of());
        }

        List<String> messages = new ArrayList<>();
        int processedCount = 0;

        // 处理每个商品
        List<Allocate> batch = new ArrayList<>();
        for (String commodityId : commodityIds) {
            try {
                String msg = processCommodity(commodityId, batch);
                messages.add(msg);
                processedCount++;
            } catch (Exception e) {
                String errMsg = "处理商品 " + commodityId + " 时出错 - " + e.getMessage();
                messages.add(errMsg);
                log.error(errMsg, e);
            }
        }

        messages.add("所有商品已处理完毕。");
        if (!batch.isEmpty()) {
            allocateMapper.batchUpsert(batch);
        }
        log.info("物资调拨完成，处理商品 {} 个，写入调拨记录 {} 条", processedCount, batch.size());
        return new Result(processedCount, "调拨完成", messages);
    }

    /**
     * 处理单个商品的运输问题
     */
    private String processCommodity(String commodityId, List<Allocate> batch) {
        // 取该商品的供应方（仓库）
        List<Inventory> invList = inventoryMapper.findByCommodity(commodityId);
        if (invList.isEmpty()) return "商品 " + commodityId + " 无仓库库存数据";

        // 取该商品的需求方（超市，来自 assign）
        List<Assign> assignList = distributionService.findByCommodity(commodityId);
        if (assignList.isEmpty()) return "商品 " + commodityId + " 无分配数据";

        String[] warehouseIds = new String[invList.size()];
        double[] supplies = new double[invList.size()];
        for (int i = 0; i < invList.size(); i++) {
            warehouseIds[i] = invList.get(i).getWarehouseId();
            supplies[i] = invList.get(i).getInvAmount();
        }

        String[] supermarketIds = new String[assignList.size()];
        double[] demands = new double[assignList.size()];
        for (int j = 0; j < assignList.size(); j++) {
            supermarketIds[j] = assignList.get(j).getSupermarketId();
            demands[j] = assignList.get(j).getAssignAmount();
        }

        // 构建距离矩阵（运价）
        double[][] costs = buildDistanceMatrix(warehouseIds, supermarketIds);
        if (Arrays.stream(costs).flatMapToDouble(Arrays::stream).anyMatch(d -> d == Double.MAX_VALUE)) {
            return "距离数据不完整，无法处理商品 " + commodityId;
        }

        // 复制输入数组（求解器会修改）
        double[][] result = vogelSolver.solve(costs, supplies.clone(), demands.clone());

        // 收集调拨结果
        for (int i = 0; i < warehouseIds.length; i++) {
            for (int j = 0; j < supermarketIds.length; j++) {
                int amount = (int) Math.round(result[i][j]);
                if (amount > 0) {
                    batch.add(new Allocate(supermarketIds[j], warehouseIds[i], commodityId, amount));
                }
            }
        }
        return "成功处理商品 " + commodityId;
    }

    /**
     * 构建仓库-超市距离矩阵
     */
    private double[][] buildDistanceMatrix(String[] warehouseIds, String[] supermarketIds) {
        double[][] costs = new double[warehouseIds.length][supermarketIds.length];
        List<String> wids = Arrays.asList(warehouseIds);
        List<String> sids = Arrays.asList(supermarketIds);
        List<Distance> distances = distanceMapper.findByPairs(wids, sids);

        // 建立 (wid,sid) -> distance 映射
        java.util.Map<String, Double> map = new java.util.HashMap<>();
        for (Distance d : distances) {
            map.put(d.getWarehouseId() + "|" + d.getSupermarketId(), d.getDistance());
        }

        for (int i = 0; i < warehouseIds.length; i++) {
            for (int j = 0; j < supermarketIds.length; j++) {
                Double dist = map.get(warehouseIds[i] + "|" + supermarketIds[j]);
                costs[i][j] = (dist != null) ? dist : Double.MAX_VALUE;
            }
        }
        return costs;
    }

    /**
     * 全部调拨结果
     */
    public List<Allocate> findAll() {
        return allocateMapper.findAll();
    }

    /**
     * 仓库发物表（按仓库筛选）
     */
    public List<Allocate> findByWarehouse(String warehouseId) {
        return allocateMapper.findByWarehouse(warehouseId);
    }

    /**
     * 超市收物表（按超市筛选）
     */
    public List<Allocate> findBySupermarket(String supermarketId) {
        return allocateMapper.findBySupermarket(supermarketId);
    }

    /**
     * 人工干预：修改某条调拨数量
     */
    @Transactional
    public void updateAmount(String supermarketId, String warehouseId, String commodityId, Integer amount) {
        allocateMapper.updateAmount(supermarketId, warehouseId, commodityId, amount);
    }

    /**
     * 清空调拨表
     */
    @Transactional
    public void deleteAll() {
        allocateMapper.deleteAll();
    }

    /** 调拨执行结果 */
    public record Result(int processedCount, String message, List<String> details) {}
}