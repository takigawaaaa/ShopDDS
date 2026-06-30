package com.shopdds.service;

import com.shopdds.entity.Distance;
import com.shopdds.mapper.DistanceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 距离服务（仓库-超市距离，用于调拨优化）
 */
@Service
@RequiredArgsConstructor
public class DistanceService {

    private final DistanceMapper distanceMapper;

    public List<Distance> list() {
        return distanceMapper.findAll();
    }

    public void addOrUpdate(String warehouseId, String supermarketId, Double distance) {
        if (distance == null || distance < 0) {
            throw new com.shopdds.common.BusinessException("距离必须为非负数");
        }
        distanceMapper.upsert(warehouseId, supermarketId, distance);
    }

    public void deleteOne(String warehouseId, String supermarketId) {
        distanceMapper.deleteOne(warehouseId, supermarketId);
    }

    public void deleteAll() {
        distanceMapper.deleteAll();
    }
}