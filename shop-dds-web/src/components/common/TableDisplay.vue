<template>
  <div class="page-container">
    <el-card class="box-card">
      <template #header>
        <div class="table-header">
          <span class="page-title">{{ title }}</span>
          <div>
            <slot name="actions" />
          </div>
        </div>
      </template>

      <el-table :data="data" stripe border style="width: 100%" v-loading="loading">
        <slot />
      </el-table>

      <slot name="footer" />
    </el-card>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  title: string
  data: any[]
  loading?: boolean
}>()
</script>

<style scoped>
.table-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.page-title {
  font-size: 16px;
  font-weight: 600;
}
</style>