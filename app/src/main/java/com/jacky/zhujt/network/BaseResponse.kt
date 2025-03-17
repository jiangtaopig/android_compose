package com.jacky.zhujt.network

data class BaseResponse<T>(
    val status: Int,    // 状态码（如 200=成功）
    val msg: String,    // 消息提示
    val data: T?       // 泛型数据（可能为null）
)