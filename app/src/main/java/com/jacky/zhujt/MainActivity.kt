package com.jacky.zhujt

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jacky.zhujt.ui.theme.ZhujtTheme
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZhujtTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .background(Color.LightGray)
            .fillMaxHeight()
            .padding(all = 8.dp)
            .fillMaxWidth()
    ) {
        Text(text = "Hello $name")
        Text(text = "Hello Jacky, It's me Iron Man！", modifier = Modifier.clickable {
            Toast.makeText(context, "xxxx", Toast.LENGTH_SHORT).show()
        })

//        WebViewExample()
        HelloContent2()

        Button(
            onClick = {
                val intent = Intent(context, TestActivity::class.java)
                context.startActivity(intent)
            },
        ) {
            Text(text = "TextActivity")
        }
        Button(
            onClick = {
                val intent = Intent(context, FlowActivity::class.java)
                context.startActivity(intent)
            },
        ) {
            Text(text = "FlowActivity")
        }

//        TextScreen()
        TextScreen2()

//        ShowListScreen()

        TimeCounter()
    }
}

@Composable
fun TimeCounter() {
    val scope = rememberCoroutineScope()
    val model = TestViewModel()

    var timer by remember {
        mutableStateOf(0)
    }

    Column() {
        Text(text = "计数：$timer")
        Button(onClick = {
            scope.launch {
                // 由于Flow的collect函数是一个挂起函数，因此必须在协程作用域或其他挂起函数中才能调用
                model.timeFlow.collectLatest {
                    Log.e(TAG, "flow  collect ===== $it")
                    delay(3000)
                    Log.e(TAG, "flow  collect ===== $timer --- done")
                    timer = it
                }

                // 另外，只要调用了collect函数之后就相当于进入了一个死循环，它的下一行代码是永远都不会执行到的。
                // 因此，如果你的代码中有多个Flow需要collect，下面这种写法就是完全错误的：
                /**
                 * lifecycleScope.launch {
                     mainViewModel.flow1.collect {
                      ....
                    }
                     mainViewModel.flow2.collect {
                      ...
                    }
                }
                 */
            }
        }) {
            Text(text = "开始")
        }
    }
}


data class Book(val name: String, val author: String)

fun getListData(): List<Book> {
    val books = mutableListOf<Book>()
    for (i in 1..10) {
        val book = Book("盗墓笔记新图$i", "马斯克$i")
        books.add(book)
    }
    return books
}

@Composable
fun ShowListScreen() {
    val list = getListData()

    LazyColumn() {
        items(items = list, key = { it.name }) { book ->
            ListItemView(book)
        }
    }
}

@Composable
fun ListItemView(book: Book) {
    val context = LocalContext.current
    Card(modifier = Modifier
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
                .background(Color.Blue)
        ) {
            Text(text = book.name, color = Color.White, modifier = Modifier.padding(2.dp))
            Text(text = book.author, color = Color.Red, modifier = Modifier.padding(1.dp))
        }
    }
}

@Composable
fun TextScreen() {
    // 状态提升到 Composable 内部
    var coroutineValue by remember { mutableStateOf("") }

    // 获取与 Composable 生命周期绑定的协程作用域
    val scope = rememberCoroutineScope()

    Column {
        Text(text = "协程值： $coroutineValue")

        Button(
            onClick = {
                scope.launch {
                    Log.e(TAG, "TextScreen , 获取数据 thread = ${Thread.currentThread().name}")
                    coroutineValue = getMessages()
                }
            },
        ) {
            Text(text = "协程测试")
        }
    }
}

/**
 *  通过 ViewModel 更新 UI
 */
@Composable
fun TextScreen2(viewModel: TestViewModel = TestViewModel()) {

    val data by remember {
        viewModel.textValue
    }

    Column {
        Text(text = "协程值： $data")

        Button(
            onClick = { viewModel.fetchData() },
        ) {
            Text(text = "协程测试")
        }
    }
}

suspend fun getMessages(): String {
    return withContext(Dispatchers.IO) {
        Log.e(TAG, "getMessages , thread = ${Thread.currentThread().name}")
        // 模拟网络耗时
        delay(1000)
        "123"
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ZhujtTheme {
        Greeting("Android")
    }
}


// 状态提升
@Composable
private fun HelloContent2() {

    var name by remember {
        mutableStateOf("")
    }

    Text(text = "HelloContent2 $name")

    val valueChangeCallback = { nn: String ->
        name = nn
    }
    HelloContent(name = name, onNameChange = valueChangeCallback)
}

@Composable
private fun HelloContent(name: String, onNameChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Hello! $name",
            modifier = Modifier.padding(bottom = 8.dp),

            )
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Name") }
        )
    }
}