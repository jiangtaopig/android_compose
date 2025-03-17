package com.jacky.zhujt

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


private const val TAG = "TestViewModel"

class TestViewModel : ViewModel() {

    val textValue = mutableStateOf("")

    fun fetchData() {
        viewModelScope.launch {
            Log.e(TAG, "fetchData , 获取数据 thread = ${Thread.currentThread().name}")
            textValue.value = getMessages()
        }
    }

    private suspend fun getMessages(): String {
        return withContext(Dispatchers.IO) {
            Log.e(TAG, "getMessages , thread = ${Thread.currentThread().name}")
            // 模拟网络耗时
            delay(1000)
            "123"
        }
    }

    val timeFlow = flow {
        var time = 0
        while (time < 10) {
            Log.e(TAG, "flow  emit ===== $time")
            emit(time)
            delay(1000)
            time ++
        }
    }

}