package com.shopdds.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shopdds.entity.Application;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 申请表 apl（复合主键 supermarket_Id + commodity_Id）
 */
@Mapper
public interface ApplicationMapper extends BaseMapper<Application> {

    /** 提交/更新申请（由超市管理员调用） */
    int upsert(@Param("supermarketId") String supermarketId,
               @Param("commodityId") String commodityId,
               @Param("amount") Integer amount);

    /** 按商品汇总申请量 */
    int summarizeToAllapl();

    /** 查询全部申请（供分配使用） */
    List<Application> findAll();

    /** 按商品查询申请 */
    List<Application> findByCommodity(@Param("commodityId") String commodityId);
}