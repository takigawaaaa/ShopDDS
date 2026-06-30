package com.shopdds.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shopdds.dto.DropdownItem;
import com.shopdds.entity.Commodity;
import com.shopdds.entity.Employee;
import com.shopdds.entity.HeadAdmin;
import com.shopdds.entity.Supermarket;
import com.shopdds.entity.Warehouse;
import com.shopdds.mapper.CommodityMapper;
import com.shopdds.mapper.EmployeeMapper;
import com.shopdds.mapper.HeadAdminMapper;
import com.shopdds.mapper.SupermarketMapper;
import com.shopdds.mapper.WarehouseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 下拉框数据服务
 * <p>
 * 对应原系统的 *IdAcquireServlet / *ManagerAcquireServlet —— 返回 {id, name} 列表。
 */
@Service
@RequiredArgsConstructor
public class CommonService {

    private final CommodityMapper commodityMapper;
    private final WarehouseMapper warehouseMapper;
    private final SupermarketMapper supermarketMapper;
    private final EmployeeMapper employeeMapper;
    private final HeadAdminMapper headAdminMapper;

    public List<DropdownItem> commodities() {
        return commodityMapper.selectList(new LambdaQueryWrapper<Commodity>()
                        .orderByAsc(Commodity::getCommodityId)).stream()
                .map(c -> new DropdownItem(c.getCommodityId(), c.getCommodityName()))
                .toList();
    }

    public List<DropdownItem> warehouses() {
        return warehouseMapper.selectList(new LambdaQueryWrapper<Warehouse>()
                        .orderByAsc(Warehouse::getWarehouseId)).stream()
                .map(w -> new DropdownItem(w.getWarehouseId(), w.getWarehouseName()))
                .toList();
    }

    public List<DropdownItem> supermarkets() {
        return supermarketMapper.selectList(new LambdaQueryWrapper<Supermarket>()
                        .orderByAsc(Supermarket::getSupermarketId)).stream()
                .map(s -> new DropdownItem(s.getSupermarketId(), s.getSupermarketName()))
                .toList();
    }

    public List<DropdownItem> employees() {
        return employeeMapper.selectList(new LambdaQueryWrapper<Employee>()
                        .orderByAsc(Employee::getEmployeeId)).stream()
                .map(e -> new DropdownItem(e.getEmployeeId(), e.getEmployeeName()))
                .toList();
    }

    public List<DropdownItem> headAdmins() {
        return headAdminMapper.selectList(new LambdaQueryWrapper<HeadAdmin>()
                        .orderByAsc(HeadAdmin::getHeadadminId)).stream()
                .map(a -> new DropdownItem(a.getHeadadminId(), a.getHeadadminName()))
                .toList();
    }

    /** 仅仓库管理员 */
    public List<DropdownItem> warehouseManagers() {
        return employeeMapper.selectList(new LambdaQueryWrapper<Employee>()
                        .eq(Employee::getEmployeeIde, "仓库管理员")
                        .orderByAsc(Employee::getEmployeeId)).stream()
                .map(e -> new DropdownItem(e.getEmployeeId(), e.getEmployeeName()))
                .toList();
    }

    /** 仅超市管理员 */
    public List<DropdownItem> supermarketManagers() {
        return employeeMapper.selectList(new LambdaQueryWrapper<Employee>()
                        .eq(Employee::getEmployeeIde, "超市管理员")
                        .orderByAsc(Employee::getEmployeeId)).stream()
                .map(e -> new DropdownItem(e.getEmployeeId(), e.getEmployeeName()))
                .toList();
    }
}