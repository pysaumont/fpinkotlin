package com.fpinkotlin.actors.coroutines.example15

import kotlinx.coroutines.experimental.CoroutineName
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking


fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")

fun main(args: Array<String>) = runBlocking(CoroutineName("Principal")) {
    log("Started main coroutine")
    // run two background value computations
    val v1 = async(CoroutineName("secondaryV1")) {
        delay(500)
        log("Computing v1")
        252
    }
    val v2 = async(CoroutineName("secondaryV2")) {
        delay(1000)
        log("Computing v2")
        6
    }
    log("The answer for v1 / v2 = ${v1.await() / v2.await()}")
}