package com.shopdds;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ShopDDS 应用启动类
 * 连锁超市物资分配调拨决策支持系统 - 后端
 * 福州大学23级信管专业《管理信息系统项目实践》第12小组
 */
@SpringBootApplication
@MapperScan("com.shopdds.mapper")
public class ShopDdsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopDdsApplication.class, args);
        System.out.println("""

                ========================================
                  ShopDDS 后端启动成功
                  连锁超市物资分配调拨决策支持系统
                  福州大学 23 级信管专业 第12小组
                ========================================
                """);
    }
}