package com.jacky.zhujt.network

class NetworkException(
    val errorType: String,  // 错误类型（如 Timeout/NoConnection）
    cause: Throwable? = null
) : Exception("Network Error: $errorType", cause)