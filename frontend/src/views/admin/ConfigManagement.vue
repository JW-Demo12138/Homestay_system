<template>
  <div class="config-management">
    <div class="page-header">
      <h2>系统配置</h2>
    </div>

    <el-tabs v-model="activeTab">
      <el-tab-pane label="支付配置" name="payment">
        <el-card class="config-card">
          <el-form :model="paymentConfig" label-width="200px">
            <el-form-item label="订单支付超时时间">
              <el-input-number
                v-model="paymentConfig['order.payment.timeout']"
                :min="1"
                :max="1440"
                :step="5"
              />
              <span class="form-tip">分钟</span>
            </el-form-item>
            <el-form-item label="订单自动确认收货">
              <el-input-number
                v-model="paymentConfig['order.auto.confirm.days']"
                :min="1"
                :max="30"
                :step="1"
              />
              <span class="form-tip">天</span>
            </el-form-item>
            <el-form-item label="平台手续费率">
              <el-input-number
                v-model="paymentConfig['platform.fee.rate']"
                :min="0"
                :max="100"
                :step="0.5"
                :precision="2"
              />
              <span class="form-tip">%</span>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="savePaymentConfig" :loading="saveLoading">
                保存配置
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { configAPI } from '@/api/config'
import { ElMessage } from 'element-plus'

const activeTab = ref('payment')
const saveLoading = ref(false)

const paymentConfig = reactive({
  'order.payment.timeout': 30,
  'order.auto.confirm.days': 7,
  'platform.fee.rate': 5
})

const loadConfigs = async () => {
  try {
    const configs = await configAPI.list()
    if (configs && configs.length) {
      configs.forEach(item => {
        if (item.configKey in paymentConfig || item.config_key in paymentConfig) {
          const key = item.configKey || item.config_key
          paymentConfig[key] = Number(item.configValue || item.config_value)
        }
      })
    }
  } catch (error) {
    console.error('加载配置失败:', error)
  }
}

const savePaymentConfig = async () => {
  try {
    saveLoading.value = true
    const updates = {}
    Object.keys(paymentConfig).forEach(key => {
      updates[key] = String(paymentConfig[key])
    })
    await configAPI.batchUpdate(updates)
    ElMessage.success('支付配置保存成功')
  } catch (error) {
    console.error('保存配置失败:', error)
    ElMessage.error('保存配置失败')
  } finally {
    saveLoading.value = false
  }
}

onMounted(() => {
  loadConfigs()
})
</script>

<style scoped>
.config-management {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.config-card {
  border-radius: 8px;
}

.form-tip {
  margin-left: 15px;
  color: #999;
  font-size: 13px;
}
</style>
