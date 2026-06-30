package com.shopdds.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shopdds.entity.Inventory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 库存表 inv（复合主键 commodity_Id + warehouse_Id）
 */
@Mapper
public interface InventoryMapper extends BaseMapper<Inventory> {

    /** 提交/更新库存（由仓库管理员调用） */
    int upsert(@Param("commodityId") String commodityId,
               @Param("warehouseId") String warehouseId,
               @Param("amount") Integer amount);

    /** 按商品汇总库存量（供汇总接口使用） */
    int summarizeToAllinv();

    /** 清空总库存汇总表 */
    int clearAllinv();

    /** 查询指定商品的各仓库库存 */
    List<Inventory> findByCommodity(@Param("commodityId") String commodityId);
}