package com.jacky.zhujt.network

import com.jacky.zhujt.user.Lesson
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("api/teacher/{type}/{num}")
    suspend fun getLesson(@Query("type") type: Int, @Query("num") num: Int): BaseResponse<List<Lesson>>
}