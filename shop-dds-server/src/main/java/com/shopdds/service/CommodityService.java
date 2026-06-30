package com.shopdds.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shopdds.entity.Commodity;
import com.shopdds.mapper.CommodityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品服务
 */
@Service
@RequiredArgsConstructor
public class CommodityService {

    private final CommodityMapper commodityMapper;

    public List<Commodity> list() {
        return commodityMapper.selectList(new LambdaQueryWrapper<Commodity>()
                .orderByAsc(Commodity::getCommodityId));
    }

    public Commodity get(String commodityId) {
        return commodityMapper.selectById(commodityId);
    }

    public void add(Commodity c) {
        if (commodityMapper.selectById(c.getCommodityId()) != null) {
            throw new com.shopdds.common.BusinessException("商品编号已存在");
        }
        commodityMapper.insert(c);
    }

    public void update(Commodity c) {
        commodityMapper.updateById(c);
    }

    public void delete(String commodityId) {
        commodityMapper.deleteById(commodityId);
    }
}