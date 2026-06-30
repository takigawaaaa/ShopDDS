<template>
  <div class="page-container">
    <el-card class="box-card">
      <template #header>
        <div style="display:flex;justify-content:space-between;align-items:center;">
          <span class="page-title">物资分配</span>
          <div>
            <el-button type="primary" :icon="Cpu" :loading="executing" @click="onExecute">执行分配</el-button>
            <el-popconfirm title="确定清空全部分配结果？" @confirm="onClear" width="240">
              <template #reference><el-button :icon="Delete">清空</el-button></template>
            </el-popconfirm>
          </div>
        </div>
      </template>

      <el-alert type="info" :closable="false" show-icon style="margin-bottom:16px;">
        分配规则：库存充足则按申请足额分配；不足则按比例缩减，剩余库存按余数从大到小补齐。须先完成"数据汇总"。
      </el-alert>

      <el-table :data="list" v-loading="loading" stripe border size="small">
        <el-table-column prop="supermarketId" label="超市编号" width="140" />
        <el-table-column prop="commodityId" label="商品编号" width="140" />
        <el-table-column prop="assignAmount" label="分配数量" />
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="openEdit(row)">人工干预</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" title="人工干预分配数量" width="420px">
      <el-form label-width="100px">
        <el-form-item label="超市编号">{{ editRow.supermarketId }}</el-form-item>
        <el-form-item label="商品编号">{{ editRow.commodityId }}</el-form-item>
        <el-form-item label="分配数量">
          <el-input-number v-model="editRow.assignAmount" :min="0" />
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
import { executeDistribution, listAssigns, updateAssign, clearAssigns } from '@/api/business'
import type { Assign } from '@/types'

const loading = ref(false)
const executing = ref(false)
const saving = ref(false)
const list = ref<Assign[]>([])
const dialogVisible = ref(false)
const editRow = ref<Assign>({ supermarketId: '', commodityId: '', assignAmount: 0 })

async function load() {
  loading.value = true
  try {
    const res = await listAssigns()
    list.value = res.data
  } finally {
    loading.value = false
  }
}

async function onExecute() {
  executing.value = true
  try {
    const res = await executeDistribution()
    ElMessage.success(res.msg || `物资分配完成，共 ${res.data} 条`)
    await load()
  } finally {
    executing.value = false
  }
}

function openEdit(row: Assign) {
  editRow.value = { ...row }
  dialogVisible.value = true
}

async function onSave() {
  saving.value = true
  try {
    await updateAssign(editRow.value.supermarketId, editRow.value.commodityId, editRow.value.assignAmount)
    ElMessage.success('修改成功')
    dialogVisible.value = false
    await load()
  } finally {
    saving.value = false
  }
}

async function onClear() {
  await clearAssigns()
  ElMessage.success('已清空')
  await load()
}

onMounted(load)
</script>