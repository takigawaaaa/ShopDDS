# ShopDDS 连锁超市物资分配调拨决策支持系统

> 福州大学 23 级信息管理与信息系统专业《管理信息系统项目实践》第 12 小组

连锁超市物资分配调拨决策支持系统，基于 **Spring Boot 3 + Vue 3 + Element Plus** 前后端分离架构重构。

## 📐 技术栈

| 层 | 技术 |
|---|---|
| 后端 | Spring Boot 3.2 · MyBatis-Plus · Spring Security · JWT · BCrypt · MySQL 8 |
| 前端 | Vue 3 (Composition API) · TypeScript · Element Plus · Pinia · Vue Router · Axios · Vite |
| 构建 | Maven · npm |

## 🏗️ 项目结构

```
.
├── shop-dds-server/      # 后端 Spring Boot 服务
│   ├── pom.xml
│   ├── src/main/java/com/shopdds/
│   │   ├── ShopDdsApplication.java
│   │   ├── config/        # Security / MyBatis-Plus / 密码迁移 Runner
│   │   ├── common/        # Result / 异常 / 角色枚举
│   │   ├── security/      # JWT / LoginUser / @CurrentUser
│   │   ├── controller/    # 15 个 REST 控制器
│   │   ├── service/       # 业务服务
│   │   │   └── algorithm/ # Vogel 运输问题求解器
│   │   ├── mapper/        # MyBatis-Plus Mapper
│   │   ├── entity/        # 14 个实体
│   │   └── dto/
│   └── src/main/resources/
│       ├── application.yml
│       ├── mapper/        # MyBatis XML（复合主键表自定义 SQL）
│       └── db/            # schema.sql + data.sql
│
└── shop-dds-web/         # 前端 Vue 3 应用
    ├── package.json
    ├── vite.config.ts
    └── src/
        ├── api/          # 14 个 API 模块
        ├── router/       # 角色路由守卫
        ├── stores/       # Pinia auth
        ├── layouts/      # 3 个角色布局
        ├── views/        # 登录 / 总部 / 仓库 / 超市 页面
        ├── components/   # 通用 TableDisplay
        └── types/        # TypeScript 类型
```

## 🔄 核心业务流程

```
超市提交申请(apl)  →  仓库提交库存(inv)
                           ↓
              总部「数据汇总」 inv→allinv, apl→allapl
                           ↓
              总部「物资分配」 按比例缩减 + 最大余数法 → assign
                           ↓
              总部「物资调拨」 Vogel 似近法求解运输问题 → allocate
                           ↓
              仓库看「发物表」   超市看「收物表」
```

## 🚀 快速启动

### 1. 准备数据库

```sql
CREATE DATABASE dds DEFAULT CHARACTER SET utf8mb4;
```

后端首次启动会自动执行 `schema.sql` 建表 + `data.sql` 导入种子数据，并由 `PasswordMigrationRunner` 把明文密码迁移为 BCrypt 哈希。

### 2. 修改后端配置

编辑 `shop-dds-server/src/main/resources/application.yml` 中的数据源账号密码：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/dds?...
    username: root
    password: 你的密码
```

### 3. 启动后端

```bash
cd shop-dds-server
./mvnw spring-boot:run     # 或在 IDE 中运行 ShopDdsApplication
```

后端运行于 `http://localhost:8080/api`。

### 4. 启动前端

```bash
cd shop-dds-web
npm install
npm run dev
```

前端运行于 `http://localhost:5173`（已配置 /api 代理到后端）。

### 5. 默认账号（种子数据，密码已自动 BCrypt）

| 角色 | 编号 | 密码 |
|---|---|---|
| 总部管理员 | `101`（等价老账号） | 见 `headadmin.csv` |
| 仓库管理员 | `e001` 等 | `123456` |
| 超市管理员 | `e006` 等 | `123456` |

> 总部管理员密码以原 CSV 为准；员工默认密码为 `123456`。

## 🔐 安全特性

- BCrypt 密码哈希（自动迁移明文密码）
- JWT 无状态认证 + Spring Security 角色鉴权
- 前端 Axios 拦截器统一附带 Token、401 自动跳登录
- Vue 模板自动转义，杜绝原 JSP `alert('<%= msg %>')` 的 XSS 漏洞

## 📚 算法说明

- **Vogel 似近法（VAM）**：运输问题初始基本可行解求解，复用自原项目 `util/vogel.java` 的 `TP_vogel()` 纯算法实现。
- **比例分配**：库存不足时按申请比例缩减 + 最大余数补齐，复用自原 `DistributeServlet`。