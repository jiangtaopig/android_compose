package com.jacky.zhujt.user

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacky.zhujt.network.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserModel : ViewModel() {

    private val lessons: MutableLiveData<List<Lesson>> by lazy {
        MutableLiveData<List<Lesson>>().also {
            fetchData()
        }
    }

    val testData = MutableLiveData<String>()

    private val userRepository = UserRepository()

    private fun fetchData() {
        viewModelScope.launch(Dispatchers.IO) {
            // 执行网络请求（网页5）
            val result = userRepository.fetchUser()
            Log.e("UserModel", "---- fetchData === result = ${result.getOrNull()}")
            // 请求完成后需要把数据设置进去
            lessons.postValue(result.getOrNull())
            // 主线程中更新 UI
            withContext(Dispatchers.Main) {

            }
        }
    }
    fun getLesson(): LiveData<List<Lesson>> {
        return lessons
    }
    fun getTestData() {
        viewModelScope.launch {
            try {

                val result = getTestDataFromNetwork()
                testData.postValue(result)
            } catch (e: Exception) {

            }
        }
    }

    private suspend fun getTestDataFromNetwork() :String {
        delay(2000)
        return "Hello world ----- !"
    }

    /**
     * ---------------------------------------- 使用 flow ------------------------------------------------------
     */

    // 使用 stateIn 把 冷流变成热流 StateFlow

    val uiState = userRepository.fetchLessons()
        .catch { e -> emit(UiState(error = e.message)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState(isLoading = true) // StateFlow， 必须要有初始值
        )

    var uiState2 = mutableStateOf<UiState<List<Lesson>>>(UiState(isLoading = true))

    // 不使用 stateIn 转成 热流
   fun fetchLesson2() {
        viewModelScope.launch {
            userRepository.fetchLessons()
                .collect {
                    uiState2.value = it
                }
        }
    }


}