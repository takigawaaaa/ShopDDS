<template>
  <div class="page-container">
    <el-card class="box-card welcome-card" shadow="never">
      <div class="welcome">
        <h1 class="welcome-title">欢迎使用 DDS 门店分销决策系统</h1>
        <p class="welcome-desc">
          本系统面向连锁零售企业，围绕门店补货与库存调拨场景，提供从数据汇总、物资分配到跨店调拨的一站式决策支持，
          帮助总部管理员基于库存与申请数据快速完成分销决策。
        </p>
      </div>
    </el-card>

    <el-row :gutter="20" class="flow-row">
      <el-col :span="8">
        <el-card class="flow-card" shadow="hover">
          <div class="flow-icon step-1"><el-icon><DataLine /></el-icon></div>
          <h2 class="flow-title">汇总</h2>
          <p class="flow-text">汇总各仓库库存与各超市申请，形成全盘物资视图</p>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="flow-card" shadow="hover">
          <div class="flow-icon step-2"><el-icon><Goods /></el-icon></div>
          <h2 class="flow-title">分配</h2>
          <p class="flow-text">基于距离与申请量，将库存分配到各超市门店</p>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="flow-card" shadow="hover">
          <div class="flow-icon step-3"><el-icon><Switch /></el-icon></div>
          <h2 class="flow-title">调拨</h2>
          <p class="flow-text">对未满足需求的门店进行跨仓库物资调拨</p>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="box-card" shadow="never" style="margin-top:20px;">
      <template #header><span class="page-title">管理功能</span></template>
      <el-row :gutter="16">
        <el-col :span="6" v-for="f in features" :key="f.title">
          <div class="feature-item">
            <el-icon class="feature-icon"><component :is="f.icon" /></el-icon>
            <div class="feature-title">{{ f.title }}</div>
            <div class="feature-desc">{{ f.desc }}</div>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { DataLine, Goods, Switch, User, OfficeBuilding, Box, Tickets, Coin, Connection } from '@element-plus/icons-vue'

const features = [
  { title: '员工管理', desc: '总部/仓库/超市员工', icon: User },
  { title: '设施管理', desc: '仓库与超市信息', icon: OfficeBuilding },
  { title: '商品管理', desc: '商品与价格维护', icon: Box },
  { title: '距离维护', desc: '仓库超市距离矩阵', icon: Connection },
  { title: '会员信息', desc: '会员基本资料', icon: Tickets },
  { title: '销售记录', desc: '门店销售流水', icon: Coin },
  { title: '汇总决策', desc: '库存申请汇总', icon: DataLine },
  { title: '分配调拨', desc: '物资分配调拨', icon: Goods }
]
</script>

<style scoped>
.welcome-card{ margin-bottom:20px; }
.welcome{ text-align:center; padding:20px 0; }
.welcome-title{ font-size:24px; font-weight:600; margin:0 0 10px; color:#303133; }
.welcome-sub{ font-size:14px; color:#409eff; margin:0 0 14px; }
.welcome-desc{ font-size:13px; color:#606266; line-height:1.8; max-width:780px; margin:0 auto; }
.flow-row{ margin-top:0; }
.flow-card{ text-align:center; padding:10px 0; }
.flow-icon{ width:60px; height:60px; border-radius:50%; display:flex; align-items:center; justify-content:center; margin:0 auto 14px; font-size:28px; color:#fff; }
.step-1{ background:#409eff; }
.step-2{ background:#67c23a; }
.step-3{ background:#e6a23c; }
.flow-title{ font-size:18px; font-weight:600; margin:0 0 8px; }
.flow-text{ font-size:13px; color:#909399; line-height:1.6; }
.feature-item{ text-align:center; padding:14px; border:1px solid #ebeef5; border-radius:6px; }
.feature-icon{ font-size:24px; color:#409eff; }
.feature-title{ font-size:14px; font-weight:600; margin:8px 0 4px; }
.feature-desc{ font-size:12px; color:#909399; }
.page-title{ font-size:16px; font-weight:600; }
</style>