# 修改前端代码实现API调用

## 目标
将前端代码中的本地模拟数据操作替换为实际的API调用，实现真正的数据库操作功能。

## 修改内容

### 1. OrderView.vue
- 导入orderApi
- 添加getOrders函数，调用orderApi.getUserList()获取订单列表
- 修改handlePay函数，调用orderApi.pay()支付订单
- 修改handleCancel函数，调用orderApi.cancel()取消订单
- 修改handleConfirm函数，调用orderApi.updateStatus()确认入住
- 添加onMounted钩子，组件挂载时获取订单列表
- 添加错误处理和加载状态

### 2. UserView.vue
- 检查并优化现有的API调用
- 确保所有用户相关操作都正确调用后端API
- 添加更完善的错误处理
- 优化加载状态管理

### 3. 验证修改
- 确保所有前端操作都能正确调用后端API
- 验证数据能正确写入数据库
- 验证数据能正确从数据库读取

## 技术要点
- 使用async/await处理异步API调用
- 添加try/catch进行错误处理
- 使用ElMessage显示操作结果
- 添加加载状态管理，提升用户体验
- 确保API调用参数与后端接口一致

## 预期效果
修改完成后，前端的民宿管理、订单管理和用户管理等功能将真正与后端交互，实现数据的持久化存储和读取，而不是使用本地模拟数据。