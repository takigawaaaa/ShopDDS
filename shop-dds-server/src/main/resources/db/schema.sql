-- =====================================================
-- ShopDDS 数据库初始化脚本
-- 连锁超市物资分配调拨决策支持系统
-- =====================================================
-- 使用前请先: CREATE DATABASE dds DEFAULT CHARACTER SET utf8mb4;
-- 然后切换到 dds 数据库执行本脚本。

SET NAMES utf8mb4;

-- ============ 1. 实体表 ============
















-- 商品表
CREATE TABLE IF NOT EXISTS commodity (
    commodity_Id      VARCHAR(5)   NOT NULL,
    commodity_Barcode VARCHAR(20)  NOT NULL,
    commodity_Name    VARCHAR(20)  NOT NULL,
    commodity_Class   VARCHAR(10)  NOT NULL,
    commodity_Unit    VARCHAR(5)   NOT NULL,
    commodity_Price   DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (commodity_Id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 总部管理员表（密码 BCrypt 哈希）
CREATE TABLE IF NOT EXISTS headadmin (
    headadmin_Id   VARCHAR(10) NOT NULL,
    headadmin_Name VARCHAR(10) NOT NULL,
    headadmin_Pwd  VARCHAR(100) NOT NULL,
    headadmin_Tel  VARCHAR(11) NOT NULL,
    PRIMARY KEY (headadmin_Id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 员工表（密码 BCrypt 哈希；employee_Ide 区分仓库/超市管理员）
CREATE TABLE IF NOT EXISTS employee (
    employee_Id   VARCHAR(10) NOT NULL,
    employee_Name VARCHAR(10) NOT NULL,
    employee_Pwd  VARCHAR(100) NOT NULL,
    employee_Ide  VARCHAR(10) NOT NULL,
    employee_Tel  VARCHAR(11) NOT NULL,
    PRIMARY KEY (employee_Id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 超市表
CREATE TABLE IF NOT EXISTS supermarket (
    supermarket_Id   VARCHAR(5)  NOT NULL,
    supermarket_Name VARCHAR(10) NOT NULL,
    supermarket_Adr  VARCHAR(20) NOT NULL,
    supermarket_Tel  VARCHAR(11) NOT NULL,
    employee_Id      VARCHAR(10) NOT NULL,
    PRIMARY KEY (supermarket_Id),
    FOREIGN KEY (employee_Id) REFERENCES employee(employee_Id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 仓库表
CREATE TABLE IF NOT EXISTS warehouse (
    warehouse_Id   VARCHAR(5)  NOT NULL,
    warehouse_Name VARCHAR(10) NOT NULL,
    warehouse_Adr  VARCHAR(20) NOT NULL,
    warehouse_Tel  VARCHAR(11) NOT NULL,
    employee_Id    VARCHAR(10) NOT NULL,
    PRIMARY KEY (warehouse_Id),
    FOREIGN KEY (employee_Id) REFERENCES employee(employee_Id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 会员表
CREATE TABLE IF NOT EXISTS member (
    member_Id     VARCHAR(10) NOT NULL,
    member_Name   VARCHAR(10) NOT NULL,
    member_Cardnum VARCHAR(20) NOT NULL,
    member_Tel    VARCHAR(11) NOT NULL,
    member_Points INT DEFAULT 0,
    PRIMARY KEY (member_Id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============ 2. 关系/流程表 ============

-- 各仓库库存表（复合主键）
CREATE TABLE IF NOT EXISTS inv (
    commodity_Id VARCHAR(10) NOT NULL,
    warehouse_Id VARCHAR(10) NOT NULL,
    inv_Amount   INT,
    inv_Time     TIME,
    PRIMARY KEY (commodity_Id, warehouse_Id),
    FOREIGN KEY (commodity_Id) REFERENCES commodity(commodity_Id),
    FOREIGN KEY (warehouse_Id) REFERENCES warehouse(warehouse_Id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 各超市申请表（复合主键）
CREATE TABLE IF NOT EXISTS apl (
    supermarket_Id VARCHAR(10) NOT NULL,
    commodity_Id   VARCHAR(10) NOT NULL,
    apl_Amount     INT,
    apl_Time       TIME,
    PRIMARY KEY (supermarket_Id, commodity_Id),
    FOREIGN KEY (supermarket_Id) REFERENCES supermarket(supermarket_Id),
    FOREIGN KEY (commodity_Id) REFERENCES commodity(commodity_Id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 总库存表
CREATE TABLE IF NOT EXISTS allinv (
    commodity_Id   VARCHAR(10) NOT NULL,
    allinv_Amount  INT,
    PRIMARY KEY (commodity_Id),
    FOREIGN KEY (commodity_Id) REFERENCES commodity(commodity_Id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 总申请表
CREATE TABLE IF NOT EXISTS allapl (
    commodity_Id   VARCHAR(10) NOT NULL,
    allapl_Amount  INT,
    PRIMARY KEY (commodity_Id),
    FOREIGN KEY (commodity_Id) REFERENCES commodity(commodity_Id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 距离表（复合主键）
CREATE TABLE IF NOT EXISTS dis (
    warehouse_Id   VARCHAR(10)   NOT NULL,
    supermarket_Id VARCHAR(10)   NOT NULL,
    distance       DOUBLE(100,2),
    PRIMARY KEY (warehouse_Id, supermarket_Id),
    FOREIGN KEY (warehouse_Id) REFERENCES warehouse(warehouse_Id),
    FOREIGN KEY (supermarket_Id) REFERENCES supermarket(supermarket_Id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 物资分配表（复合主键）
CREATE TABLE IF NOT EXISTS assign (
    supermarket_Id VARCHAR(10) NOT NULL,
    commodity_Id   VARCHAR(10) NOT NULL,
    assign_Amount  INT,
    PRIMARY KEY (supermarket_Id, commodity_Id),
    FOREIGN KEY (supermarket_Id) REFERENCES supermarket(supermarket_Id),
    FOREIGN KEY (commodity_Id) REFERENCES commodity(commodity_Id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 物资调拨表（复合主键）
CREATE TABLE IF NOT EXISTS allocate (
    supermarket_Id  VARCHAR(10) NOT NULL,
    warehouse_Id    VARCHAR(10) NOT NULL,
    commodity_Id    VARCHAR(10) NOT NULL,
    allocate_Amount INT,
    PRIMARY KEY (supermarket_Id, warehouse_Id, commodity_Id),
    FOREIGN KEY (supermarket_Id) REFERENCES supermarket(supermarket_Id),
    FOREIGN KEY (warehouse_Id) REFERENCES warehouse(warehouse_Id),
    FOREIGN KEY (commodity_Id) REFERENCES commodity(commodity_Id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 销售流水表
CREATE TABLE IF NOT EXISTS transaction_record (
    transid       VARCHAR(20) NOT NULL,
    supermarket_id VARCHAR(5),
    commodity_id   VARCHAR(5),
    quantity       INT,
    totalPrice     DECIMAL(10,2),
    memberId       VARCHAR(10),
    saleTime       VARCHAR(30),
    PRIMARY KEY (transid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 索引（注：MySQL 5.7 / 早期 8.0 不支持 CREATE INDEX IF NOT EXISTS，
-- 而 inv/apl 的查询已由复合主键 (commodity_Id, warehouse_Id) 等覆盖，故不再额外建索引以保证兼容性）