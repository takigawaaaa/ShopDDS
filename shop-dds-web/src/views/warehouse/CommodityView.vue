<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { listCommodities } from '@/api/commodity'
import type { Commodity } from '@/types'

const loading = ref(false)
const commodities = ref<Commodity[]>([])
const keyword = ref('')

const filtered = computed(() => {
  const kw = keyword.value.trim().toLowerCase()
  if (!kw) return commodities.value
  return commodities.value.filter(
    (c) =>
      c.commodityName.toLowerCase().includes(kw) ||
      c.commodityId.toLowerCase().includes(kw)
  )
})

const loadData = async () => {
  loading.value = true
  try {
    const res = await listCommodities()
    commodities.value = res.data || []
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
          <span>商品查询</span>
          <el-input
            v-model="keyword"
            placeholder="按商品名称或编号搜索"
            clearable
            style="width: 240px"
            :prefix-icon="'Search'"
          />
        </div>
      </template>

      <el-table
        v-loading="loading"
        :data="filtered"
        stripe
        border
        style="width: 100%"
      >
        <el-table-column prop="commodityId" label="商品编号" min-width="120" />
        <el-table-column prop="commodityBarcode" label="商品条码" min-width="140" />
        <el-table-column prop="commodityName" label="商品名称" min-width="160" />
        <el-table-column prop="commodityClass" label="商品类别" min-width="100" />
        <el-table-column prop="commodityUnit" label="计量单位" min-width="90" />
        <el-table-column prop="commodityPrice" label="单价(元)" min-width="100" align="right" />
        <el-empty v-if="!filtered.length && !loading" slot="empty" description="暂无商品数据" />
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
</style>