package com.odds.kotlindemo

import kotlinx.coroutines.*

/**
 * ClassName:Test
 * Description:
 * 2022/3/23 4:12 下午
 * wiki:
 */
class Test {

}

/*
 * Copyright 2016-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */




fun main() = runBlocking { // this: CoroutineScope
    launch { // launch a new coroutine and continue
        delay(1000L) // non-blocking delay for 1 second (default time unit is ms)
        println("World!") // print after delay
    }
    println("Hello") // main coroutine continues while a previous one is delayed
}