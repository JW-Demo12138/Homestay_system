# 测试评分API接口
$url = "http://localhost:8080/api/rating/create"
$headers = @{"Content-Type"="application/json; charset=utf-8"}
$body = '{"homestayId": 1, "userId": 1, "rating": 5, "comment": "good homestay"}'

Write-Host "请求体: $body"
Write-Host "发送评分请求..."
try {
    $response = Invoke-RestMethod -Uri $url -Method POST -Headers $headers -Body $body
    Write-Host "响应:"
    $response | ConvertTo-Json -Depth 3
} catch {
    Write-Host "错误: $($_.Exception.Message)"
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $errorResponse = $reader.ReadToEnd()
        Write-Host "错误响应: $errorResponse"
    }
}

# 检查数据库中的评分数据
Write-Host "\n检查数据库中的评分数据..."
& mysql -u root -p123456 -e "USE homestay; SELECT * FROM homestay_rating;"