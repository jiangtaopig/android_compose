package com.jacky.zhujt.test_kotlin

// name 前面不加 val， val p1 = Person("zz") 那么无法通过 p1 访问 Person 的name属性
open class Person constructor(val name: String) {

    constructor(name: String, sex: Boolean): this(name)

    var age: Int = 0
        set(value) { // 类似于 java 的 setAge 方法
        field = if (value >= 0) value else 0
    }

}

/**
 * 扩展是kotlin中非常重要的一个特性，它能让我们对一些已有的类进行功能增加、简化，使他们更好的应对我们的需求。
 */
fun Person.showName() {
    println("Person name = ${this.name}")
}

/**
 * 扩展属性也会有set和get方法，并且要求实现这两个方法，不然会提示编译错误。
 * 因为扩展并不是在目标类上增加了这个属性，所以目标类其实是不持有这个属性的，
 * 我们通过 get 和 set 对这个属性进行读写操作的时候也不能使用 field 指代属性本体。可以使用this，依然表示的目标类。
 */
var Person.isAdult: Boolean
get() = this.age > 18
set(value) {
    if(value) {
        this.age = 22
    } else {
        this.age = 8
    }
}



/**
 * 1. Kotlin 默认所有类为 final（不可继承），必须显式添加 open 修饰符才能作为基类
 * 2. 允许被重写的方法或属性也需标记为 open
 */
// 这里 SuperMan 类有主构造函数
class SuperMan(name: String, fly: Boolean): Person(name) {
    val fly: Boolean
    init {
        this.fly = fly
    }
}

/**
 * Student 类没有主构造函数，所以在次构造函数上面使用 super
 */
class Student : Person {
    constructor(name: String, age: Int) : super(name)
    constructor(name: String): this(name, 0)

    var grade: Int = 1
    set(value) {
        field = if (value > 0) value else 1
    }
}

/**
 * 抽象类
 */
abstract class Animal{
    open var name: String = "Animal"

    abstract fun makeSound()

    open fun changeName(name: String) {
        this.name = name
    }
}

class Dog : Animal() {
    override var name: String = "Dog"


    override fun makeSound() {
        println("I am puppy, woof woof!")
    }

    override fun changeName(name: String) {
        super.changeName(name)
    }

}

// 另一个例子：抽象属性
abstract class Shape {
    // 抽象属性，必须在子类中实现
    abstract val area: Double
}

class Circle(val radius: Double) : Shape() {
    // 实现抽象属性
    override val area: Double
        get() = Math.PI * radius * radius
}


/**
 * 类的委托 ，通过 by 关键字实现委托
 */

interface Base {
    fun showMsg()
}

class BaseImpl(val x: Int) : Base {
    override fun showMsg() {
       println("BaseImpl :: showMsg x = ${x.toString()}")
    }
}

class Printer(b: Base): Base by b

/**
 * --------------------------------------- 类的委托结束 -----------------------------------------------
 */

fun main() {
    val p1 = Person("zz")
    p1.age = 22
    println("111111111111 ---- name = ${p1.name}, age = ${p1.age}")

    p1.showName()
    println("p1 isAdult = ${p1.isAdult}")

    p1.isAdult = false

    println("p1 isAdult = ${p1.isAdult}, age = ${p1.age}")

    val sM = SuperMan("zhu", true)
    println("sM ---- name = ${sM.name}, fly = ${sM.fly}")

    val s1 = Student("zhujt", 23)
    s1.grade = 2
    println("s1 grade = ${s1.grade} , name = ${s1.name}")

    val dog = Dog()
//    dog.changeName("labuladuo")
    dog.makeSound()

    println(" dog name = ${dog.name}")

    val b1 = BaseImpl(33)
    // Printer 类没有实现接口 Base 的方法 showMsg ，而是通过关键字by将实现委托给了b，而输出也和预想的一样。
    Printer(b1).showMsg()

    /**
     * 类的扩展
     */



}