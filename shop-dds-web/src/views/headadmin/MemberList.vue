<template>
  <div class="page-container">
    <el-card class="box-card">
      <template #header>
        <div style="display:flex;justify-content:space-between;align-items:center;">
          <span class="page-title">会员信息</span>
          <el-input v-model="keyword" placeholder="搜索姓名/卡号/电话" clearable :prefix-icon="Search" style="width:260px;" />
        </div>
      </template>
      <el-table :data="filtered" v-loading="loading" stripe border style="width:100%">
        <el-table-column prop="memberId" label="编号" width="140" />
        <el-table-column prop="memberName" label="姓名" width="160" />
        <el-table-column prop="memberCardnum" label="卡号" width="180" />
        <el-table-column prop="memberTel" label="电话" width="180" />
        <el-table-column prop="memberPoints" label="积分" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { Search } from '@element-plus/icons-vue'
import { listMembers } from '@/api/member'
import type { Member } from '@/types'

const list = ref<Member[]>([])
const loading = ref(false)
const keyword = ref('')

const filtered = computed(() => {
  const k = keyword.value.trim().toLowerCase()
  if (!k) return list.value
  return list.value.filter(m =>
    (m.memberName ?? '').toLowerCase().includes(k) ||
    (m.memberCardnum ?? '').toLowerCase().includes(k) ||
    (m.memberTel ?? '').toLowerCase().includes(k)
  )
})

async function load() {
  loading.value = true
  try {
    const res = await listMembers()
    list.value = res.data ?? []
  } finally {
    loading.value = false
  }
}

onMounted(load)
</script>

<style scoped>
.page-title{ font-size:16px; font-weight:600; }
</style>