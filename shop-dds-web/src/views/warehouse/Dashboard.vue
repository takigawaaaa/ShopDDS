<script setup lang="ts">
import { computed } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'

const auth = useAuthStore()
const router = useRouter()

const facilityName = computed(() => auth.user?.facilityName || '仓库管理员')
const userName = computed(() => auth.user?.name || '')

const features = [
  { title: '商品查询', desc: '查看所有商品基础信息', icon: 'Goods', path: '/warehouse/commodity' },
  { title: '提交库存', desc: '录入仓库现有库存数量', icon: 'Box', path: '/warehouse/inventory' },
  { title: '发物表', desc: '查看本仓库已调拨记录', icon: 'Files', path: '/warehouse/dispatch' }
]

const go = (path: string) => router.push(path)
</script>

<template>
  <div class="page-container">
    <el-card shadow="never" class="welcome-card">
      <div class="welcome">
        <el-icon class="welcome-icon"><OfficeBuilding /></el-icon>
        <div class="welcome-text">
          <h2>欢迎，{{ userName || '仓库管理员' }}</h2>
          <p>当前所属仓库：<strong>{{ facilityName }}</strong></p>
          <p class="sub">在这里您可以管理商品库存、提交库存记录并查看发物表。</p>
        </div>
      </div>
    </el-card>

    <el-row :gutter="20" class="feature-row">
      <el-col v-for="f in features" :key="f.title" :xs="24" :sm="12" :md="8">
        <el-card shadow="hover" class="feature-card" @click="go(f.path)">
          <el-icon class="feature-icon"><component :is="f.icon" /></el-icon>
          <h3>{{ f.title }}</h3>
          <p>{{ f.desc }}</p>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.welcome-card {
  margin-bottom: 20px;
  background: linear-gradient(135deg, #ecf5ff 0%, #f5f7fa 100%);
}
.welcome {
  display: flex;
  align-items: center;
  gap: 20px;
}
.welcome-icon {
  font-size: 56px;
  color: #409eff;
}
.welcome-text h2 {
  margin: 0 0 8px 0;
  font-size: 22px;
}
.welcome-text p {
  margin: 4px 0;
  color: #606266;
}
.welcome-text .sub {
  font-size: 13px;
  color: #909399;
}
.feature-row {
  margin-bottom: 20px;
}
.feature-card {
  cursor: pointer;
  text-align: center;
  transition: transform 0.2s;
}
.feature-card:hover {
  transform: translateY(-3px);
}
.feature-icon {
  font-size: 40px;
  color: #409eff;
  margin-bottom: 12px;
}
.feature-card h3 {
  margin: 0 0 8px 0;
}
.feature-card p {
  margin: 0;
  color: #909399;
  font-size: 13px;
}
</style>