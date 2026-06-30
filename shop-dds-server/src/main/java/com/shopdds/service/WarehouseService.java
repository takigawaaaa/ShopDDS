package com.shopdds.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shopdds.common.BusinessException;
import com.shopdds.entity.Warehouse;
import com.shopdds.mapper.WarehouseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 仓库服务
 */
@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseMapper warehouseMapper;

    public List<Warehouse> list() {
        return warehouseMapper.selectList(new LambdaQueryWrapper<Warehouse>()
                .orderByAsc(Warehouse::getWarehouseId));
    }

    public Warehouse get(String id) {
        return warehouseMapper.selectById(id);
    }

    public void add(Warehouse w) {
        if (warehouseMapper.selectById(w.getWarehouseId()) != null) {
            throw new BusinessException("仓库编号已存在");
        }
        warehouseMapper.insert(w);
    }

    public void delete(String id) {
        try {
            warehouseMapper.deleteById(id);
        } catch (Exception e) {
            throw new BusinessException("删除失败：该仓库可能存在关联数据");
        }
    }

    public void updateName(String id, String name) {
        Warehouse w = new Warehouse();
        w.setWarehouseId(id);
        w.setWarehouseName(name);
        warehouseMapper.updateById(w);
    }

    public void updateAddress(String id, String address) {
        Warehouse w = new Warehouse();
        w.setWarehouseId(id);
        w.setWarehouseAdr(address);
        warehouseMapper.updateById(w);
    }

    public void updateManager(String id, String employeeId) {
        Warehouse w = new Warehouse();
        w.setWarehouseId(id);
        w.setEmployeeId(employeeId);
        warehouseMapper.updateById(w);
    }

    /** 由所属管理员编号查仓库 */
    public Warehouse findByManager(String employeeId) {
        return warehouseMapper.selectOne(new LambdaQueryWrapper<Warehouse>()
                .eq(Warehouse::getEmployeeId, employeeId));
    }
}