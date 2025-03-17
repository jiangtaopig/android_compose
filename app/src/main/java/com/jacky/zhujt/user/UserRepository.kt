package com.jacky.zhujt.user

import com.jacky.zhujt.network.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow



class UserRepository {

    private val apiService = RetrofitClientUtil.createService(ApiService::class.java)

    suspend fun fetchUser(): Result<List<Lesson>> = try {
        val response = apiService.getLesson(4, 2)
        if (response.status == 1 || response.status == 200 || response.status == 0) {
            Result.success(response.data!!)
        } else {
            Result.failure(ApiException(response.status, response.msg))
        }
    } catch (e: Exception) {
        Result.failure(NetworkException(e.message ?: "Unknown error"))
    }


    /**
     * 使用 flow 的方式请求接口
     */
    fun fetchLessons(): Flow<UiState<List<Lesson>>> = flow {
        emit(UiState(isLoading = true))
        // 因为接口请求太快，所以延时1秒，为了看 loading 效果
        kotlinx.coroutines.delay(1000)
        try {
            val response = apiService.getLesson(4, 4)
            if (response.status == 1 || response.status == 200 || response.status == 0) {
                emit(UiState(data = response.data))
            } else {
                emit(UiState(error = "接口请求错误！"))
            }
        } catch (e: Exception) {
            emit(UiState(error = e.message))
        }
    }



}