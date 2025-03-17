package com.jacky.zhujt

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.jacky.zhujt.user.Lesson
import com.jacky.zhujt.user.UserModel

class FlowActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
//            LessonScreen()
            LessonScreen2()
        }
    }


    @Composable
    fun LessonScreen2(viewModel: UserModel = UserModel()) {
        val context = LocalContext.current

        // 需要通过 remember 帮助刷新 UI ，否则每次刷新次函数， uiState 的值都是 Loading
        val uiState by remember {
            viewModel.uiState2
        }

        // 获取数据
        viewModel.fetchLesson2()

        when {
            uiState.isLoading -> LoadingView()
            uiState.error != null -> Toast.makeText(context, uiState.error,
                Toast.LENGTH_LONG).show()
            else -> uiState.data?.let { MyLessonScreen(it) }
        }
    }

    @Composable
    fun LoadingView() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(color = Color.LightGray),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator ()
        }
    }

    @Composable
    fun LessonScreen(viewModel: UserModel = UserModel()) {
        val context = LocalContext.current

        /**
         * 1. StateFlow 是热流（Hot Flow），它会始终持有最新状态，即使没有活跃的收集者。
         * 当 UI 重新订阅时（例如因重组重新触发 collectAsState），StateFlow 会立即将最新值发送给新的订阅者。
         *
         * 2. StateFlow 要求必须设置初始值（initialValue），这使得 Compose 在首次订阅时即可获取有效状态，无需依赖 remember 跨重组保存初始值。
         */

        // collectAsState() 在 Compose 中的作用是将 Flow 转换为 Compose 的 State 对象
        val uiState by viewModel.uiState.collectAsState()

        when {
            uiState.isLoading -> LoadingView()
            uiState.error != null -> Toast.makeText(context, uiState.error, Toast.LENGTH_LONG)
                .show()
            else -> uiState.data?.let { MyLessonScreen(it) }
        }
    }

    @Composable
    fun MyLessonScreen(lessons: List<Lesson>) {
        LazyColumn() {
            items(items = lessons, key = { it.name }) { lesson ->
                ListItemView(lesson)
            }
        }
    }

    @Composable
    fun ListItemView(lesson: Lesson) {
        val context = LocalContext.current
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp) // 外边距
                .clickable {
                    Toast
                        .makeText(context, "clicked!", Toast.LENGTH_LONG)
                        .show()
                }, elevation = 3.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(all = 8.dp)
                    .fillMaxWidth()
//                .height(60.dp)
//                    .background(Color.Blue)
            ) {
                Text(text = lesson.name, color = Color.Black, modifier = Modifier.padding(2.dp))
                Text(
                    text = lesson.description,
                    color = Color.Red,
                    modifier = Modifier.padding(1.dp)
                )
            }
        }
    }

}