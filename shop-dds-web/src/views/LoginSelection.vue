<template>
  <div class="login-wrap">
    <div class="login-card">
      <h2>ShopDDS</h2>
      <p class="login-sub">连锁超市物资分配调拨决策支持系统</p>
      <p class="login-sub" style="margin-bottom: 8px;">请选择登录身份</p>

      <div class="role-grid">
        <div class="role-btn" @click="goLogin('headadmin')">
          <el-icon class="role-icon"><OfficeBuilding /></el-icon>
          <div class="role-name">总部管理员</div>
          <div class="role-desc">物资分配与调拨决策</div>
        </div>
        <div class="role-btn" @click="goLogin('warehouse')">
          <el-icon class="role-icon"><House /></el-icon>
          <div class="role-name">仓库管理员</div>
          <div class="role-desc">提交库存、查看发物表</div>
        </div>
        <div class="role-btn" @click="goLogin('supermarket')">
          <el-icon class="role-icon"><ShoppingCart /></el-icon>
          <div class="role-name">超市管理员</div>
          <div class="role-desc">提交申请、查看收物表</div>
        </div>
      </div>

      <p class="login-sub" style="margin-top: 24px; font-size: 12px;">
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()

interface RolePage { code: 'headadmin' | 'warehouse' | 'supermarket' }

function goLogin(code: RolePage['code']) {
  if (code === 'headadmin') {
    router.push('/login/headadmin')
  } else {
    const role = code === 'warehouse' ? '仓库管理员' : '超市管理员'
    router.push(`/login/employee?role=${encodeURIComponent(role)}`)
  }
}
</script>