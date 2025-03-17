package com.jacky.zhujt.network

import okhttp3.Interceptor
import okhttp3.Response

class ErrorInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (!response.isSuccessful) {
            val errorBody = response.body?.string()
//            val errorCode = parseErrorCode(errorBody) // 解析错误码
//            throw when (errorCode) {
//                401 -> AuthException("Token expired")
//                500 -> ServerException("Internal error")
//                else -> ApiException(errorCode, "Request failed")
//            }
        }
        return response
    }
}