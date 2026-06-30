package com.shopdds.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 距离表 dis（仓库-超市距离，用于调拨优化）
 * <p>复合主键 (warehouse_Id, supermarket_Id)</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("dis")
public class Distance {

    @TableField("warehouse_Id")
    private String warehouseId;

    @TableField("supermarket_Id")
    private String supermarketId;

    @TableField("distance")
    private Double distance;
}