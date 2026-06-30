package com.shopdds.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 会员表 member
 */
@Data
@TableName("member")
public class Member {

    @TableId("member_Id")
    @TableField("member_Id")
    private String memberId;

    @TableField("member_Name")
    private String memberName;

    @TableField("member_Cardnum")
    private String memberCardnum;

    @TableField("member_Tel")
    private String memberTel;

    @TableField("member_Points")
    private Integer memberPoints;
}