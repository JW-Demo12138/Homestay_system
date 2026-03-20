export const formatDate = (date, format = 'yyyy-MM-dd') => {
  if (!date) return ''
  
  const d = new Date(date)
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const hour = String(d.getHours()).padStart(2, '0')
  const minute = String(d.getMinutes()).padStart(2, '0')
  const second = String(d.getSeconds()).padStart(2, '0')
  
  return format
    .replace('yyyy', year)
    .replace('MM', month)
    .replace('dd', day)
    .replace('HH', hour)
    .replace('mm', minute)
    .replace('ss', second)
}

export const formatPrice = (price) => {
  if (price === null || price === undefined) return '0.00'
  return Number(price).toFixed(2)
}

export const getImageUrl = (url, isLocal = false) => {
  if (!url) return ''
  if (url.startsWith('http')) return url
  // 处理可能的重复路径
  const cleanUrl = url.split(',').filter(Boolean)[0] || url
  // 根据是否使用本地路径返回不同的URL
  if (isLocal) {
    // 本地路径处理 - 特色体验图片实际保存在后端的uploads/avatars目录
    return `/uploads/avatars/${cleanUrl.includes('/') ? cleanUrl.split('/').pop() : cleanUrl}`
  } else {
    // 后端服务器路径处理
    return `http://localhost:8080${cleanUrl.startsWith('/') ? '' : '/'}${cleanUrl}`
  }
}
