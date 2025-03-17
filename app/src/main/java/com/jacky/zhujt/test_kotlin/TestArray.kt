package com.jacky.zhujt.test_kotlin

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TestArray {

    fun test1() {
        val array = arrayOf(1, 2, 4)
        val a = array[1]
        println("a = $a")
    }

    fun getStringLength(obj: Any): Int? {
        if(obj !is String) return null
        return obj.length
    }

    fun testFor() {
        loop1@ for(i in 1 .. 5) {
            for (j in 1 .. 5) {
                println(" i = $i, j = $j")
                if (i == 2 && j == 3) break@loop1
            }
        }
    }

    fun test2(x: Any) {
        val s = try { x as String } catch(e: ClassCastException) { null }
        println("s ==== $s")
    }
}

data class A(val name: String)

typealias Predicate<T> = (T) -> Boolean

fun foo(p: Predicate<Int>) :Boolean {
   return p(22)
}

fun doSum(x: Int, y: Int, sum: (a: Int, b: Int) -> Int): Int {
    return sum(x, y)

}

fun main() {
    println("main start")
   val job = CoroutineScope(Dispatchers.IO).launch {
       println("协程 start。。。。。 thread = ${Thread.currentThread().name}")
       delay(200)
       println("协程 end。。。。。")
    }


    val positive : (a: Int) -> Boolean = {it >0}
    val sumFun = {a: Int, b: Int -> a+b}

    val sum = doSum(4, 5, sumFun)
    println(" sum -----$sum")

    println(" --------- " + foo(positive))

    val testArray = TestArray()
    testArray.test1()

    val list = mutableListOf<String>()
    list.add("a")
    list.add("b")

    list.also {
        it.add("also")
    }

    list.let {
        it.add("let")
    }

    list.forEach { println(" it = $it") }


    val numbers: MutableList<Int> = mutableListOf(1, 2, 3)
    val readOnlyView: List<Int> = numbers
    println(numbers)        // 输出 "[1, 2, 3]"
    numbers.add(4)
    println(readOnlyView)   // 输出 "[1, 2, 3, 4]"
//    readOnlyView.clear()  // 编译报错

    val map = mutableMapOf(1 to "a", 2 to "b")
    map[3] = "c"

    map.forEach { k, v -> println("k = $k, v = $v") }

    val filterMap = map.filter { (k, v) -> k > 1 }
        .map { (k, v) ->
            A("$k"+ "_" +v) }

    println("filterMap = $filterMap")


    val items = listOf("apple", "banana", "kiwi")
    for (item in items) {
        println(item)
    }

    items.filter { x -> x.length > 4 }

    val len = testArray.getStringLength("abcdf")
    println(" len = $len")

    testArray.testFor()

    for (i in 10 downTo 2 step 2) {
        println("--- i = $i")
    }

    testArray.test2(1)

}