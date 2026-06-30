<script setup lang="ts">
import { computed } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'

const auth = useAuthStore()
const router = useRouter()

const facilityName = computed(() => auth.user?.facilityName || '超市')
const userName = computed(() => auth.user?.name || '')

const features = [
  { title: '商品查询', desc: '查看所有商品基础信息', icon: 'Goods', path: '/supermarket/commodity' },
  { title: '提交需求申请', desc: '录入本超市商品需求', icon: 'EditPen', path: '/supermarket/application' },
  { title: '我的分配', desc: '查看总部分配结果', icon: 'List', path: '/supermarket/my-assign' },
  { title: '收物表', desc: '查看本超市收到的调拨', icon: 'Files', path: '/supermarket/receive' },
  { title: '会员积分结算', desc: '按消费金额换算积分', icon: 'GoldMedal', path: '/supermarket/member-points' }
]

const go = (path: string) => router.push(path)
</script>

<template>
  <div class="page-container">
    <el-card shadow="never" class="welcome-card">
      <div class="welcome">
        <el-icon class="welcome-icon"><ShoppingCart /></el-icon>
        <div class="welcome-text">
          <h2>欢迎，{{ userName || '超市管理员' }}</h2>
          <p>当前所属超市：<strong>{{ facilityName }}</strong></p>
          <p class="sub">在这里您可以提交需求申请、查看分配与收物记录、为会员结算积分。</p>
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
  background: linear-gradient(135deg, #f0f9eb 0%, #f5f7fa 100%);
}
.welcome {
  display: flex;
  align-items: center;
  gap: 20px;
}
.welcome-icon {
  font-size: 56px;
  color: #67c23a;
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
  color: #67c23a;
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