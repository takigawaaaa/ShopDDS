<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getCommodities } from '@/api/common'
import { submitApplication } from '@/api/submission'
import type { DropdownItem } from '@/types'

const loading = ref(false)
const submitting = ref(false)
const commodities = ref<DropdownItem[]>([])
const form = ref({
  commodityId: '',
  amount: 1
})
const recent = ref<{ commodityId: string; amount: number; time: string }[]>([])

const loadCommodities = async () => {
  loading.value = true
  try {
    const res = await getCommodities()
    commodities.value = res.data || []
  } finally {
    loading.value = false
  }
}

const onSubmit = async () => {
  if (!form.value.commodityId) {
    ElMessage.warning('请选择商品')
    return
  }
  if (!form.value.amount || form.value.amount <= 0) {
    ElMessage.warning('请输入有效的需求数量')
    return
  }
  submitting.value = true
  try {
    await submitApplication(form.value.commodityId, form.value.amount)
    ElMessage.success('需求申请提交成功')
    recent.value.unshift({
      commodityId: form.value.commodityId,
      amount: form.value.amount,
      time: new Date().toLocaleString()
    })
    if (recent.value.length > 5) recent.value.pop()
    form.value.commodityId = ''
    form.value.amount = 1
  } catch (e) {
    ElMessage.error('提交失败')
  } finally {
    submitting.value = false
  }
}

onMounted(loadCommodities)
</script>

<template>
  <div class="page-container">
    <el-card shadow="never">
      <template #header>提交需求申请</template>

      <el-form
        :model="form"
        label-width="110px"
        style="max-width: 520px"
        v-loading="loading"
      >
        <el-form-item label="商品">
          <el-select
            v-model="form.commodityId"
            placeholder="请选择商品"
            filterable
            style="width: 100%"
          >
            <el-option
              v-for="c in commodities"
              :key="c.id"
              :label="c.name"
              :value="c.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="需求数量">
          <el-input-number v-model="form.amount" :min="1" :precision="0" style="width: 100%" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="submitting" @click="onSubmit">
            提交申请
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" style="margin-top: 20px">
      <template #header>最近提交记录</template>
      <el-empty v-if="!recent.length" description="暂无提交记录" :image-size="60" />
      <el-table v-else :data="recent" stripe border style="width: 100%">
        <el-table-column prop="commodityId" label="商品编号" />
        <el-table-column prop="amount" label="数量" />
        <el-table-column prop="time" label="提交时间" />
      </el-table>
    </el-card>
  </div>
</template>