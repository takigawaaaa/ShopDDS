<template>
  <div class="page-container">
    <el-card class="box-card">
      <template #header>
        <div style="display:flex;justify-content:space-between;align-items:center;">
          <span class="page-title">数据汇总</span>
          <el-button type="primary" :icon="Refresh" :loading="aggregating" @click="onAggregate">执行汇总</el-button>
        </div>
      </template>

      <el-alert type="info" :closable="false" show-icon style="margin-bottom:16px;">
        汇总操作将各仓库库存(inv)按商品汇总到总库存(allinv)，将各超市申请(apl)按商品汇总到总申请(allapl)，为物资分配做准备。
      </el-alert>

      <el-row :gutter="16">
        <el-col :span="12">
          <el-card shadow="never">
            <template #header><span style="font-weight:600;color:#409eff;">总库存汇总（allinv）</span></template>
            <el-table :data="summary.inventorySummary || []" v-loading="loading" stripe border size="small">
              <el-table-column prop="commodityId" label="商品编号" />
              <el-table-column prop="totalInventory" label="可供总量" />
            </el-table>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card shadow="never">
            <template #header><span style="font-weight:600;color:#67c23a;">总申请汇总（allapl）</span></template>
            <el-table :data="summary.applicationSummary || []" v-loading="loading" stripe border size="small">
              <el-table-column prop="commodityId" label="商品编号" />
              <el-table-column prop="totalApplication" label="需求总量" />
            </el-table>
          </el-card>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Refresh } from '@element-plus/icons-vue'
import { aggregate, getSummary } from '@/api/business'
import type { SummaryResult } from '@/types'

const loading = ref(false)
const aggregating = ref(false)
const summary = ref<SummaryResult>({ inventorySummary: [], applicationSummary: [] })

async function load() {
  loading.value = true
  try {
    const res = await getSummary()
    summary.value = res.data
  } finally {
    loading.value = false
  }
}

async function onAggregate() {
  aggregating.value = true
  try {
    await aggregate()
    ElMessage.success('数据汇总成功')
    await load()
  } finally {
    aggregating.value = false
  }
}

onMounted(load)
</script>