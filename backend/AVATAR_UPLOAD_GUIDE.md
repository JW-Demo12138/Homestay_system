# 头像上传接口使用说明

## 📋 接口信息

**接口地址**: `POST /api/user/upload-avatar`
**Content-Type**: `multipart/form-data`
**需要认证**: 是（需要携带 JWT Token）

---

## 🔧 前端实现示例

### Element Plus Upload 组件

```vue
<template>
  <el-upload
    class="avatar-uploader"
    action="http://localhost:8080/api/user/upload-avatar"
    :headers="uploadHeaders"
    :show-file-list="false"
    :on-success="handleSuccess"
    :on-error="handleError"
    :before-upload="beforeUpload"
    name="avatar"
  >
    <img v-if="imageUrl" :src="imageUrl" class="avatar" />
    <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
  </el-upload>
</template>

<script setup>
import { ref } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const imageUrl = ref('')

// 获取存储的 Token
const getToken = () => {
  return localStorage.getItem('token') || ''
}

// 上传请求头
const uploadHeaders = {
  'Authorization': 'Bearer ' + getToken()
}

// 上传前验证
const beforeUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB!')
    return false
  }
  return true
}

// 上传成功
const handleSuccess = (response, uploadFile) => {
  if (response.code === 200) {
    imageUrl.value = response.data.avatar
    ElMessage.success('头像上传成功!')
  } else {
    ElMessage.error(response.message || '上传失败')
  }
}

// 上传失败
const handleError = (error) => {
  console.error('上传失败:', error)
  ElMessage.error('头像上传失败，请重试')
}
</script>

<style scoped>
.avatar-uploader {
  width: 178px;
  height: 178px;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader:hover {
  border-color: #409eff;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>
```

---

### Axios 实现

```javascript
import axios from 'axios'

const uploadAvatar = async (file) => {
  try {
    const formData = new FormData()
    formData.append('avatar', file)

    const response = await axios.post('http://localhost:8080/api/user/upload-avatar', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    })

    if (response.data.code === 200) {
      console.log('上传成功:', response.data.data)
      return response.data.data
    } else {
      console.error('上传失败:', response.data.message)
      return null
    }
  } catch (error) {
    console.error('上传异常:', error)
    return null
  }
}

// 使用示例
const handleFileChange = async (event) => {
  const file = event.target.files[0]
  if (file) {
    const result = await uploadAvatar(file)
    if (result) {
      console.log('头像URL:', result.avatar)
    }
  }
}
```

---

### Fetch API 实现

```javascript
const uploadAvatar = async (file) => {
  try {
    const formData = new FormData()
    formData.append('avatar', file)

    const response = await fetch('http://localhost:8080/api/user/upload-avatar', {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      },
      body: formData
    })

    const result = await response.json()

    if (result.code === 200) {
      console.log('上传成功:', result.data)
      return result.data
    } else {
      console.error('上传失败:', result.message)
      return null
    }
  } catch (error) {
    console.error('上传异常:', error)
    return null
  }
}
```

---

## ⚠️ 常见问题

### 1. 400 Bad Request

**原因**:
- 参数名不匹配（后端期望 `avatar` 或 `file`）
- 文件为空
- 文件格式不支持
- 文件大小超限

**解决方案**:
```javascript
// 确保 name 属性正确
<el-upload name="avatar" ... />

// 或者使用 file
<el-upload name="file" ... />
```

### 2. 401 Unauthorized

**原因**: 未携带或携带了无效的 JWT Token

**解决方案**:
```javascript
const uploadHeaders = {
  'Authorization': 'Bearer ' + localStorage.getItem('token')
}
```

### 3. 413 Payload Too Large

**原因**: 文件大小超过服务器限制

**解决方案**:
- 压缩图片
- 增加后端配置的文件大小限制
- 检查 `application.yml` 中的 `max-file-size` 配置

### 4. 文件扩展名不支持

**原因**: 上传了不支持的图片格式

**解决方案**:
后端只支持以下格式：
- jpg
- jpeg
- png
- gif

---

## 📝 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| avatar | File | 是 | 上传的头像文件（二选一） |
| file | File | 是 | 上传的头像文件（二选一） |

**注意**: `avatar` 和 `file` 参数名都支持，任选其一即可。

---

## 📤 响应格式

### 成功响应

```json
{
  "code": 200,
  "message": "更新头像成功",
  "data": {
    "id": 1,
    "username": "user123",
    "avatar": "/uploads/avatars/1705324800000.jpg"
  }
}
```

### 失败响应

```json
{
  "code": 400,
  "message": "请选择要上传的文件"
}
```

---

## 🔍 调试建议

### 1. 检查网络请求

打开浏览器开发者工具（F12），查看 Network 标签：
- 检查请求 URL 是否正确
- 检查请求方法是否为 POST
- 检查 Content-Type 是否为 `multipart/form-data`
- 检查是否携带了 Authorization 头

### 2. 检查后端日志

后端会输出详细的日志：
```
上传文件名: avatar.jpg
文件大小: 123456 bytes
文件扩展名: .jpg
创建上传目录: true
文件保存成功: e:\Homestay system\backend\uploads\avatars\1705324800000.jpg
```

### 3. 检查上传目录

确保项目根目录下存在 `uploads/avatars/` 目录：
```bash
ls -la uploads/avatars/
```

---

## 🎯 完整示例

```vue
<template>
  <div>
    <h3>头像上传</h3>
    
    <el-upload
      class="avatar-uploader"
      action="http://localhost:8080/api/user/upload-avatar"
      :headers="uploadHeaders"
      :show-file-list="false"
      :on-success="handleSuccess"
      :on-error="handleError"
      :before-upload="beforeUpload"
      name="avatar"
      accept="image/jpeg,image/jpg,image/png,image/gif"
    >
      <img v-if="imageUrl" :src="imageUrl" class="avatar" />
      <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
    </el-upload>
    
    <div class="tips">
      <p>支持格式：JPG、JPEG、PNG、GIF</p>
      <p>文件大小：不超过 2MB</p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const imageUrl = ref(localStorage.getItem('userAvatar') || '')

const getToken = () => {
  return localStorage.getItem('token') || ''
}

const uploadHeaders = {
  'Authorization': 'Bearer ' + getToken()
}

const beforeUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB!')
    return false
  }
  return true
}

const handleSuccess = (response) => {
  if (response.code === 200) {
    imageUrl.value = response.data.avatar
    localStorage.setItem('userAvatar', response.data.avatar)
    ElMessage.success('头像上传成功!')
  } else {
    ElMessage.error(response.message || '上传失败')
  }
}

const handleError = (error) => {
  console.error('上传失败:', error)
  ElMessage.error('头像上传失败，请重试')
}
</script>

<style scoped>
.avatar-uploader {
  width: 178px;
  height: 178px;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader:hover {
  border-color: #409eff;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
  object-fit: cover;
}

.tips {
  margin-top: 20px;
  font-size: 14px;
  color: #666;
}

.tips p {
  margin: 5px 0;
}
</style>
```

---

## 📚 相关文档

- [FRONTEND_API_DOCUMENTATION.md](file:///e:\Homestay%20system\backend\FRONTEND_API_DOCUMENTATION.md) - 前端API文档
- [MOCK_IMPLEMENTATION_FIXES.md](file:///e:\Homestay%20system\backend\MOCK_IMPLEMENTATION_FIXES.md) - 模拟实现修复文档

---

**文档版本**: v1.0  
**最后更新**: 2024-02-15
