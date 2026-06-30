<template>
  <div class="page-container">
    <el-card class="box-card">
      <template #header>
        <div style="display:flex;justify-content:space-between;align-items:center;">
          <span class="page-title">销售记录</span>
          <el-input v-model="keyword" placeholder="搜索门店/商品/会员" clearable :prefix-icon="Search" style="width:260px;" />
        </div>
      </template>
      <el-table :data="filtered" v-loading="loading" stripe border style="width:100%">
        <el-table-column prop="transid" label="交易号" width="180" />
        <el-table-column prop="supermarketId" label="门店编号" width="140" />
        <el-table-column prop="commodityId" label="商品编号" width="140" />
        <el-table-column prop="quantity" label="数量" width="100" />
        <el-table-column prop="totalPrice" label="总价" width="120" />
        <el-table-column prop="memberId" label="会员编号" width="140" />
        <el-table-column prop="saleTime" label="销售时间" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { Search } from '@element-plus/icons-vue'
import { listSalesRecords } from '@/api/member'
import type { SalesRecord } from '@/types'

const list = ref<SalesRecord[]>([])
const loading = ref(false)
const keyword = ref('')

const filtered = computed(() => {
  const k = keyword.value.trim().toLowerCase()
  if (!k) return list.value
  return list.value.filter(r =>
    (r.supermarketId ?? '').toLowerCase().includes(k) ||
    (r.commodityId ?? '').toLowerCase().includes(k) ||
    (r.memberId ?? '').toLowerCase().includes(k)
  )
})

async function load() {
  loading.value = true
  try {
    const res = await listSalesRecords()
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