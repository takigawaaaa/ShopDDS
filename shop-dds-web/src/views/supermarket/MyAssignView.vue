<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { listMyAssigns } from '@/api/business'
import type { Assign } from '@/types'

const loading = ref(false)
const assigns = ref<Assign[]>([])

const loadData = async () => {
  loading.value = true
  try {
    const res = await listMyAssigns()
    assigns.value = res.data || []
  } finally {
    loading.value = false
  }
}

onMounted(loadData)
</script>

<template>
  <div class="page-container">
    <el-card shadow="never">
      <template #header>我的分配</template>

      <el-table
        v-loading="loading"
        :data="assigns"
        stripe
        border
        style="width: 100%"
      >
        <el-table-column prop="supermarketId" label="超市编号" min-width="140" />
        <el-table-column prop="commodityId" label="商品编号" min-width="140" />
        <el-table-column prop="assignAmount" label="分配数量" min-width="120" align="right" />
        <el-empty v-if="!assigns.length && !loading" slot="empty" description="暂无分配记录" />
      </el-table>
    </el-card>
  </div>
</template>