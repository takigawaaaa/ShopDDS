<template>
  <div class="page-container">
    <el-card class="box-card">
      <template #header>
        <div style="display:flex;justify-content:space-between;align-items:center;">
          <span class="page-title">物资调拨</span>
          <div>
            <el-button type="primary" :icon="Cpu" :loading="executing" @click="onExecute">执行调拨（Vogel）</el-button>
            <el-popconfirm title="确定清空全部调拨结果？" @confirm="onClear" width="240">
              <template #reference><el-button :icon="Delete">清空</el-button></template>
            </el-popconfirm>
          </div>
        </div>
      </template>

      <el-alert type="info" :closable="false" show-icon style="margin-bottom:16px;">
        调拨基于已分配的 assign 表，以仓库库存为供应、超市分配量为需求、距离为运价，用 Vogel 似近法求解运输问题。须先完成"物资分配"。
      </el-alert>

      <el-table :data="list" v-loading="loading" stripe border size="small">
        <el-table-column prop="supermarketId" label="超市编号" width="120" />
        <el-table-column prop="warehouseId" label="仓库编号" width="120" />
        <el-table-column prop="commodityId" label="商品编号" width="120" />
        <el-table-column prop="allocateAmount" label="调拨数量" />
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="openEdit(row)">人工干预</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 调拨执行结果详情 -->
    <el-dialog v-model="resultVisible" title="调拨执行结果" width="560px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="处理商品数">{{ result?.processedCount }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ result?.message }}</el-descriptions-item>
      </el-descriptions>
      <el-divider content-position="left">处理明细</el-divider>
      <el-scrollbar height="260px">
        <div v-for="(line, idx) in result?.details || []" :key="idx" style="padding:4px 0;font-size:13px;">
          <el-tag v-if="line.includes('成功')" type="success" size="small">成功</el-tag>
          <el-tag v-else-if="line.includes('出错') || line.includes('不完整') || line.includes('无')" type="warning" size="small">提示</el-tag>
          <el-tag v-else type="info" size="small">信息</el-tag>
          <span style="margin-left:8px;">{{ line }}</span>
        </div>
      </el-scrollbar>
    </el-dialog>

    <!-- 人工干预 -->
    <el-dialog v-model="dialogVisible" title="人工干预调拨数量" width="440px">
      <el-form label-width="100px">
        <el-form-item label="超市编号">{{ editRow.supermarketId }}</el-form-item>
        <el-form-item label="仓库编号">{{ editRow.warehouseId }}</el-form-item>
        <el-form-item label="商品编号">{{ editRow.commodityId }}</el-form-item>
        <el-form-item label="调拨数量">
          <el-input-number v-model="editRow.allocateAmount" :min="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="onSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Cpu, Delete } from '@element-plus/icons-vue'
import { executeAllocation, listAllocations, updateAllocate, clearAllocations } from '@/api/business'
import type { Allocate, AllocationResult } from '@/types'

const loading = ref(false)
const executing = ref(false)
const saving = ref(false)
const list = ref<Allocate[]>([])
const dialogVisible = ref(false)
const resultVisible = ref(false)
const editRow = ref<Allocate>({ supermarketId: '', warehouseId: '', commodityId: '', allocateAmount: 0 })
const result = ref<AllocationResult | null>(null)

async function load() {
  loading.value = true
  try {
    const res = await listAllocations()
    list.value = res.data
  } finally {
    loading.value = false
  }
}

async function onExecute() {
  executing.value = true
  try {
    const res = await executeAllocation()
    result.value = res.data
    resultVisible.value = true
    ElMessage.success('调拨执行完成')
    await load()
  } finally {
    executing.value = false
  }
}

function openEdit(row: Allocate) {
  editRow.value = { ...row }
  dialogVisible.value = true
}

async function onSave() {
  saving.value = true
  try {
    await updateAllocate(editRow.value.supermarketId, editRow.value.warehouseId, editRow.value.commodityId, editRow.value.allocateAmount)
    ElMessage.success('修改成功')
    dialogVisible.value = false
    await load()
  } finally {
    saving.value = false
  }
}

async function onClear() {
  await clearAllocations()
  ElMessage.success('已清空')
  await load()
}

onMounted(load)
</script>