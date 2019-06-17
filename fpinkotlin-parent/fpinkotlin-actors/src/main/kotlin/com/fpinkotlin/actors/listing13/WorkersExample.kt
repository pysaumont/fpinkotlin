package com.fpinkotlin.actors.listing13

import com.fpinkotlin.common.List
import com.fpinkotlin.common.Result
import com.fpinkotlin.common.range
import java.util.concurrent.Semaphore


private val semaphore = Semaphore(1)
private const val listLength = 1_000_000
private const val workers = 2
private val rnd = java.util.Random(0)
private val testList =
    range(0, listLength).map { rnd.nextInt(35) }

fun main() {
    semaphore.acquire()
    val startTime = System.currentTimeMillis()
    val client =
        object: AbstractActor<Result<List<Int>>>("Client") {
            override fun onReceive(message: Result<List<Int>>,
                          sender: Result<Actor<Result<List<Int>>>>) {
                message.forEach({ processSuccess(it) },
                                { processFailure(it.message ?: "Unknown error") })
                println("Total time: " + (System.currentTimeMillis() - startTime))
                semaphore.release()
            }
        }

    val manager =
        Manager("Manager", testList, client, workers)
    manager.start()
    semaphore.acquire()
}

private fun processFailure(message: String) {
    println(message)
}

fun processSuccess(lst: List<Int>) {
    println("Input: ${testList.splitAt(40).first}")
    println("Result: ${lst.splitAt(40).first}")
}
