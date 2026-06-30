package com.shopdds.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shopdds.common.BusinessException;
import com.shopdds.entity.Supermarket;
import com.shopdds.mapper.SupermarketMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 超市服务
 */
@Service
@RequiredArgsConstructor
public class SupermarketService {

    private final SupermarketMapper supermarketMapper;

    public List<Supermarket> list() {
        return supermarketMapper.selectList(new LambdaQueryWrapper<Supermarket>()
                .orderByAsc(Supermarket::getSupermarketId));
    }

    public Supermarket get(String id) {
        return supermarketMapper.selectById(id);
    }

    public void add(Supermarket s) {
        if (supermarketMapper.selectById(s.getSupermarketId()) != null) {
            throw new BusinessException("超市编号已存在");
        }
        supermarketMapper.insert(s);
    }

    public void delete(String id) {
        try {
            supermarketMapper.deleteById(id);
        } catch (Exception e) {
            throw new BusinessException("删除失败：该超市可能存在关联数据");
        }
    }

    /** 局部更新：名称 */
    public void updateName(String id, String name) {
        Supermarket s = new Supermarket();
        s.setSupermarketId(id);
        s.setSupermarketName(name);
        supermarketMapper.updateById(s);
    }

    /** 局部更新：地址 */
    public void updateAddress(String id, String address) {
        Supermarket s = new Supermarket();
        s.setSupermarketId(id);
        s.setSupermarketAdr(address);
        supermarketMapper.updateById(s);
    }

    /** 更换所属超市管理员 */
    public void updateManager(String id, String employeeId) {
        Supermarket s = new Supermarket();
        s.setSupermarketId(id);
        s.setEmployeeId(employeeId);
        supermarketMapper.updateById(s);
    }
}