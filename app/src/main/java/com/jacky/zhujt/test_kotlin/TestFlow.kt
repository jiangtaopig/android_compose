package com.jacky.zhujt.test_kotlin

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking


data class UserInfo(val name: String, val age: Int)
data class Article(val name: String, val author: String, val publishDate: String)
data class Weather(val temperature: Double, val status: String)
data class HomePageData(val articles: List<Article>, val weather: Weather)
class TestFlow {

    suspend fun testReduce() {
        val result = flow {
            for (i in 1..10) {
                emit(i)
            }
        }.reduce { sum, value ->
            println("sum = $sum, value = $value")
            sum + value
        }
        println(" result = $result")
    }

    /**
     * 和 reduce 唯一的区别就是 fold 有个初始值
     */
    suspend fun testFold() {
        val result = flow {
            for (i in 'A' .. 'Z') {
                emit(i)
            }
        }.fold("26个字母:") {
            acc, value ->
            "$acc , $value"
        }

        println("testFold ---> $result")
    }


    /**
     * 比如说我们想要获取用户的数据，但是获取用户数据必须要有 token 授权信息才行，
     * 因此我们得先发起一个请求去获取 token 信息，然后再发起另一个请求去获取用户数据.
     * 而这个问题我们就可以借助 flatMapConcat 函数来解决
     */
    private fun getToken(name: String): Flow<String> = flow {
        emit("TOKEN:X23HUDH789UAB0_$name")
    }

    private fun fetchUser(token: String, name: String) : Flow<UserInfo> = flow {
        kotlinx.coroutines.delay(200)
        val user = UserInfo(name, 23)
        emit(user)
    }

    suspend fun fetchUserByName(name: String) {
        val result = getToken(name)
            .flatMapConcat { token -> fetchUser(token, name) }
            .flowOn(Dispatchers.IO)
            .collect { userInfo ->
                println("fetchUserByName --- userInfo = $userInfo")
            }

//        println("fetchUserByName  -- result = $result")
    }


    /**
     * zip 的理解，zip 是并行执行的
     * 实际的案例，比如 APP 有个页面需要请求 历史文章和今日天气，那么就可以使用 zip 来处理了
     */

    private fun fetchArticles(author: String): Flow<List<Article>> = flow {
        kotlinx.coroutines.delay(2000)
        val articles = listOf(
            Article("盗墓笔记", author, "2016-02-12")
            , Article("盗墓笔记2", author, "2016-06-17")
        )
        emit(articles)
    }

    private fun fetchTodayWeather(address: String): Flow<Weather> = flow {
        kotlinx.coroutines.delay(1000)
        val weather = Weather(23.3, address)
        emit(weather)
    }
    
    suspend fun fetchHomePageData(author: String, address: String) {
        val start = System.currentTimeMillis()
        fetchArticles(author)
                // zip 是并行执行的
            .zip(fetchTodayWeather(address)) {
                articles, weather -> 
                val homePageData = HomePageData(articles, weather)
                homePageData
            }.flowOn(Dispatchers.IO)
            .collect {
                val articles = it.articles
                val weather = it.weather
                val end = System.currentTimeMillis()
                /**
                 * fetchArticles 设置延时 2000 ，fetchTodayWeather 延时 1000，由于是并行的，所以总耗时 --- 耗时: >>> 2138 ms
                 */
                println("耗时: >>> ${end - start} ms")
                println("articles = $articles")
                println("weather = $weather")
            }
    }

}


fun main() {
    val testFlow = TestFlow()
    // runBlocking 它的作用是提供协程作用域给稍后的Flow使用，并且在代码块中的所有代码执行完之前阻塞当前线程
    runBlocking {
//        testFlow.testReduce()
//        testFlow.testFold()

//        testFlow.fetchUserByName("zhujt")
        testFlow.fetchHomePageData("南派三叔", "上海")





    }
}