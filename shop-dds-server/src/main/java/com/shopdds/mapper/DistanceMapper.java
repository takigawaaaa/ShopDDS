package com.shopdds.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shopdds.entity.Distance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 距离表 dis（复合主键 warehouse_Id + supermarket_Id）
 */
@Mapper
public interface DistanceMapper extends BaseMapper<Distance> {

    /** 新增/更新距离 */
    int upsert(@Param("warehouseId") String warehouseId,
               @Param("supermarketId") String supermarketId,
               @Param("distance") Double distance);

    /** 全部距离 */
    List<Distance> findAll();

    /** 删除单条距离 */
    int deleteOne(@Param("warehouseId") String warehouseId,
                  @Param("supermarketId") String supermarketId);

    /** 按仓库+超市列表批量查询距离矩阵 */
    List<Distance> findByPairs(@org.apache.ibatis.annotations.Param("warehouseIds") List<String> warehouseIds,
                               @org.apache.ibatis.annotations.Param("supermarketIds") List<String> supermarketIds);

    /** 清空距离表 */
    int deleteAll();
}