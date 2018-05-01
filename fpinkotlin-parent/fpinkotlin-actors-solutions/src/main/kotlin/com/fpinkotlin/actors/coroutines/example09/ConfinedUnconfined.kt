package com.fpinkotlin.actors.coroutines.example09

import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.Unconfined
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.newSingleThreadContext
import kotlinx.coroutines.experimental.runBlocking


fun main1(args: Array<String>) {
    val job = launch(newSingleThreadContext("MyOwnThread")) {
        runBlocking {
            val jobs = arrayListOf<Job>()
            jobs += launch(Unconfined) {
                // not confined -- will work with main thread
                println("      'Unconfined': I'm working in thread ${Thread.currentThread().name}")
                delay(1000)
                println("      'Unconfined': After delay in thread ${Thread.currentThread().name}") // Resume with another thread
            }
            jobs += launch(coroutineContext) {
                // context of the parent, runBlocking coroutine
                println("'coroutineContext': I'm working in thread ${Thread.currentThread().name}")
                delay(1000)
                println("'coroutineContext': After delay in thread ${Thread.currentThread().name}") // Resume with parent thread
            }
            jobs.forEach { it.join() }
        }
    }
    runBlocking { // <-- Blocking
        job.join()
    }
}

fun main(args: Array<String>) {
    newSingleThreadContext("MyOwnThread").use {
        runBlocking {
            val jobs = arrayListOf<Job>()
            jobs += launch(Unconfined) {
                // not confined -- will work with main thread
                println("      'Unconfined': I'm working in thread ${Thread.currentThread().name}")
                delay(1000)
                println("      'Unconfined': After delay in thread ${Thread.currentThread().name}") // Resume with another thread
            }
            jobs += launch(coroutineContext) {
                // context of the parent, runBlocking coroutine
                println("'coroutineContext': I'm working in thread ${Thread.currentThread().name}")
                delay(1000)
                println("'coroutineContext': After delay in thread ${Thread.currentThread().name}") // Resume with parent thread
            }
            jobs.forEach { it.join() }
        }
    }
    // No need to wait !
}