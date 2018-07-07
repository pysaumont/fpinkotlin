package com.fpinkotlin.actors.listing06_10

import com.fpinkotlin.common.List
import com.fpinkotlin.common.Result
import com.fpinkotlin.common.range
import java.util.concurrent.Semaphore


private val semaphore = Semaphore(1) // <1>
private const val listLength = 20_000 // <2>
private const val workers = 8 // <3>
private val rnd = java.util.Random(0)
private val testList = // <4>
    range(0, listLength).map { rnd.nextInt(35) }

fun main(args: Array<String>) {
    semaphore.acquire() // <5>
    val startTime = System.currentTimeMillis()
    val client = // <6>
        object: AbstractActor<Result<List<Int>>>("Client") {
            override fun onReceive(message: Result<List<Int>>,
                          sender: Result<Actor<Result<List<Int>>>>) {
                message.forEach({ processSuccess(it) }, // <7>
                                { processFailure(it.message ?: "Unknown error") })
                println("Total time: " + (System.currentTimeMillis() - startTime))
                semaphore.release() // <10>
            }
        }

    val manager = // <8>
        Manager("Manager", testList, client, workers)
    manager.start()
    semaphore.acquire() // <9>
}

private fun processFailure(message: String) {
    println(message)
}

fun processSuccess(lst: List<Int>) {
    println("Input: ${testList.splitAt(40).first}")
    println("Result: ${lst.splitAt(40).first}")
}
