package com.fpinkotlin.actors.coroutines.example26

import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.newSingleThreadContext
import kotlinx.coroutines.experimental.runBlocking
import kotlin.coroutines.experimental.CoroutineContext
import kotlin.system.measureTimeMillis


val counterContext = newSingleThreadContext("CounterContext")

suspend fun massiveRun(context: CoroutineContext, action: suspend () -> Unit) {
    val n = 1000 // number of coroutines to launch
    val k = 1000 // times an action is repeated by each coroutine
    val time = measureTimeMillis {
        val jobs = List(n) {
            launch(context) {
                repeat(k) { action() }
            }
        }
        jobs.forEach { it.join() }
    }
    println("Completed ${n * k} actions in $time ms")
}

var counter = 0
//var counter = AtomicInteger()

fun main(args: Array<String>) = runBlocking<Unit> {
//    massiveRun(CommonPool) {
        //counter++ // Not Atomic, so won't work, even with @Volatile
//        withContext(counterContext) {
//            counter++
//        }
//        counter.getAndIncrement()
//    }
    massiveRun(counterContext) { // run each coroutine in the single-threaded context
        counter++
    }
    println("Counter = $counter")
}