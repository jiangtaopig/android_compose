package com.jacky.zhujt.test_kotlin

import kotlinx.coroutines.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

data class User(val name: String, val token: String)

class TestCoroutine {
    suspend fun testCoroutine1() :String {
        println("开始了--------------")
        delay(100)
        return "Hello World"
    }


    suspend fun getUserInfoSuspend() :User? {
        val user = withContext(Dispatchers.IO){
            //模拟网络请求耗时操作
            delay(100)
            User("asd123", "token_1111")
        }
        return user
    }

    suspend fun getUnReadMsgCountSuspend(token:String?) :Int{
        return withContext(Dispatchers.IO){
            //模拟网络请求耗时操作
            delay(10)
            10
        }
    }

    suspend fun test3() {
       val deffer = CoroutineScope(Dispatchers.IO).async {
            delay(300)
            "Who am I?"
        }
        // 使用 await 获取值，即  "Who am I?"
        val content: String = deffer.await()
        println("------ test3  content = $content")
    }

    fun test4() {
        println("------ test4  start thread = ${Thread.currentThread().name}")

        // runBlocking 启动的是一个新的协程并阻塞调用它的线程
        runBlocking {
            println("------ test4 blocking thread = ${Thread.currentThread().name}")
            delay(200)
        }
        println("------ test4  end thread = ${Thread.currentThread().name}")
    }

    fun test5() {
        val coroutineScope = CoroutineScope(Dispatchers.Default)
        coroutineScope.launch {
            val message = getMessage()
            println("------ test5  message = $message, thread = ${Thread.currentThread().name}")
        }
    }

    private suspend fun getMessage(): String {
        return withContext(Dispatchers.IO) {
            delay(1000)
            "123"
        }
    }

}

fun main() {

    println("main start")
    // 设置数量为1的 countDownLatch
    val countDownLatch = CountDownLatch(1)
    val job = CoroutineScope(Dispatchers.IO).launch {
        println("协程 start。。。。。 thread = ${Thread.currentThread().name}")
        delay(200)
        println("协程 end。。。。。")

        countDownLatch.countDown()
    }
    println("----- main do something ----")
    println("----- main sleep ----")
    Thread.sleep(50)

    println("----- main cancel job ----")
    job.cancel()



    val testCoroutine = TestCoroutine()
    testCoroutine.test4()
    println("------------------------------------------------------")
    testCoroutine.test5()
    println("------------------------------------------------------")

    CoroutineScope(Dispatchers.IO).launch {
        val d = testCoroutine.testCoroutine1()
        println(" d = $d")

        // 先拿到用户token，然后请求其他接口
        val user = testCoroutine.getUserInfoSuspend()
        println(" token = ${user?.token}")

        Int.Companion.MIN_VALUE

        val readCount = testCoroutine.getUnReadMsgCountSuspend(user?.token)
        println(" readCount = $readCount")

        testCoroutine.test3()


    }



//    CoroutineScope(Dispatchers.Default)

    // 因为 job 取消了就不会 执行里面的 countDownLatch.countDown()，那么主线程一直阻塞
    // 阻塞主线程，直到其他线程调用了 countDownLatch.countDown()
    countDownLatch.await(2000, TimeUnit.MILLISECONDS)
    println("----- main end ----")





//    val testCoroutine = TestCoroutine()
//    testCoroutine.testCoroutine1()


}