package com.shopdds.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shopdds.entity.Allocate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 调拨表 allocate（复合主键 supermarket_Id + warehouse_Id + commodity_Id）
 */
@Mapper
public interface AllocateMapper extends BaseMapper<Allocate> {

    /** 批量保存调拨结果（覆盖） */
    int batchUpsert(@Param("list") List<Allocate> list);

    /** 全部调拨结果 */
    List<Allocate> findAll();

    /** 按仓库查询（仓库发物表） */
    List<Allocate> findByWarehouse(@Param("warehouseId") String warehouseId);

    /** 按超市查询（超市收物表） */
    List<Allocate> findBySupermarket(@Param("supermarketId") String supermarketId);

    /** 更新某条调拨数量（人工干预） */
    int updateAmount(@Param("supermarketId") String supermarketId,
                     @Param("warehouseId") String warehouseId,
                     @Param("commodityId") String commodityId,
                     @Param("amount") Integer amount);

    int deleteAll();
}