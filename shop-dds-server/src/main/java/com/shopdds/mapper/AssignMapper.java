package com.shopdds.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shopdds.entity.Assign;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分配表 assign（复合主键 supermarket_Id + commodity_Id）
 */
@Mapper
public interface AssignMapper extends BaseMapper<Assign> {

    /** 批量保存分配结果（覆盖） */
    int upsert(@Param("supermarketId") String supermarketId,
               @Param("commodityId") String commodityId,
               @Param("amount") Integer amount);

    /** 批量插入分配结果 */
    int batchUpsert(@Param("list") List<Assign> list);

    /** 全部分配结果 */
    List<Assign> findAll();

    /** 查询有分配的商品 ID（DISTINCT） */
    List<String> distinctCommodityIds();

    /** 按商品查询分配（作为调拨的需求方） */
    List<Assign> findByCommodity(@Param("commodityId") String commodityId);

    int deleteAll();
}