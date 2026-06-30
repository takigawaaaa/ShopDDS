<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { listMyWareAllocations } from '@/api/business'
import { useAuthStore } from '@/stores/auth'
import type { Allocate } from '@/types'

const auth = useAuthStore()
const loading = ref(false)
const allocations = ref<Allocate[]>([])

const facilityName = computed(() => auth.user?.facilityName || '本仓库')

const loadData = async () => {
  loading.value = true
  try {
    const res = await listMyWareAllocations()
    allocations.value = res.data || []
  } finally {
    loading.value = false
  }
}

onMounted(loadData)
</script>

<template>
  <div class="page-container">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <div>
            <span class="title">发物表</span>
            <span class="subtitle">仓库名称：{{ facilityName }}</span>
          </div>
        </div>
      </template>

      <el-table
        v-loading="loading"
        :data="allocations"
        stripe
        border
        style="width: 100%"
      >
        <el-table-column prop="commodityId" label="商品编号" min-width="120" />
        <el-table-column prop="supermarketId" label="超市编号" min-width="120" />
        <el-table-column prop="warehouseId" label="仓库编号" min-width="120" />
        <el-table-column prop="allocateAmount" label="调拨数量" min-width="100" align="right" />
        <el-empty v-if="!allocations.length && !loading" slot="empty" description="暂无发物记录" />
      </el-table>
    </el-card>
  </div>
</template>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.title {
  font-size: 16px;
  font-weight: 600;
}
.subtitle {
  margin-left: 12px;
  color: #909399;
  font-size: 13px;
}
</style>