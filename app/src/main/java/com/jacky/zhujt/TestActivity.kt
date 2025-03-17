package com.jacky.zhujt

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Colors
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.jacky.zhujt.user.Lesson
import com.jacky.zhujt.user.UserModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob


class TestActivity : ComponentActivity() {
    private lateinit var coroutineScope: CoroutineScope

    private lateinit var userModel: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 绑定到主线程和Activity生命周期
        coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
        userModel = ViewModelProvider(this).get(UserModel::class.java)
        var owner : LifecycleOwner = this

        Log.e("TestActivity", "onCreate  == $userModel")

        userModel.testData.observe(this) {
            Log.e("TestActivity", "testData  =$it")
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }
        // 发起数据请求
        userModel.getTestData()

        setContent {
            SayHello(userModel, owner)
        }
    }
}

@Composable
fun SayHello(model: UserModel, owner : LifecycleOwner) {

    // lambda 表达式作为函数的最后一个参数，可以把 lambda 移到括号的外面
    val a = test1("Hello_") { a, b -> a * b }
    // 如果函数的参数只有一个 lambda 表达式，那么函数参数的括号都可以省略
    val b = test2 { name, sex -> name + "___ "+ sex }

    val c = test3(2) {
        // 如果 lambda 只有一个入参，它的这个参数也省略掉不写
        // 用 it表示唯一的参数
        val name = it
    }

    val context = LocalContext.current

    test4 {
        val name = "铁霸王大战"
        Toast.makeText(context, name, Toast.LENGTH_LONG).show()
    }

    Column( modifier = Modifier
        .background(Color.LightGray)
        .fillMaxHeight()
        .padding(all = 8.dp)
        .fillMaxWidth()) {
        Text(text = "你好，Compose $a, $b")

//        Button(onClick = {
////        val userModel = UserModel()
//            model.getLesson()
//                .observe(owner) {
//                    Log.e("TestActivity", "size 22  = ${it.size}")
//                }
//        }) {
//            Text(text = "接口请求")
//        }
        
        LessonScreen(model, owner)
    }
}

@Composable
fun LessonScreen(model: UserModel, owner : LifecycleOwner) {
    var lessons: List<Lesson> = ArrayList()
    var lessonList by remember {
        mutableStateOf(lessons)
    }

    model.getLesson()
        .observe(owner) {
            Log.e("TestActivity", "size 1  = ${it.size}")
            lessonList = it
        }
    Log.e("TestActivity", "size 2  = ${lessonList.size}")
    if (lessonList.isNotEmpty()) {
        Column() {
            lessonList.forEach { lesson ->
                LessonRow(lesson)
            }
        }
    }
}


@Composable
fun LessonRow(lesson: Lesson) {
    Box(modifier = Modifier.padding(all = 5.dp)) {
        Column(modifier = Modifier
            .background(color = Color.Cyan)
            .fillMaxWidth()
            .padding(horizontal = 2.dp, vertical = 4.dp)
        ) {
            Text(text = lesson.name)
            NetworkImage(lesson.picSmall)
        }
    }

}

@Composable
fun NetworkImage(url: String) {
    val context = LocalContext.current
    val painter = rememberImagePainter(
        request = ImageRequest.Builder(context)
            .data(url)
            .crossfade(true) // 渐变动画
//            .placeholder(R.drawable.placeholder) // 占位图
//            .transformations(CircleCropTransformation()) // 圆形裁剪
            .build()
    )

    Image(
        painter = painter,
        contentDescription = "网络图片",
//        modifier = Modifier.fillMaxWidth()
    )
}

fun test1(name: String, opt: (a: Int, b: Int) -> Int) :String {
    return  name + opt(4, 5)
}

fun test2(opt: (name: String, sex: String) -> String): String {
    return opt.invoke("猪猪侠", "男");
}

fun test3(age: Int, opt: (name: String) -> Unit) : Unit {
    opt.invoke("钢铁侠: $age")
}


fun test4(onClick: () -> Unit) {
    onClick.invoke()
}