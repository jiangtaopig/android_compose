package com.jacky.zhujt.network

class ApiException(
    val errorCode: Int,    // 服务器自定义错误码（如 4001=Token过期）
    val errorMsg: String   // 错误描述信息
) : Exception("API Error: $errorCode - $errorMsg")