package com.fpinkotlin.actors.listing12

import com.fpinkotlin.common.List
import com.fpinkotlin.common.Result
import com.fpinkotlin.common.range
import java.util.concurrent.Semaphore


private val semaphore = Semaphore(1)
private const val listLength = 2000
private const val workers = 8
private val rnd = java.util.Random(0)
private val testList =
    range(0, listLength).map { rnd.nextInt(35) }

fun main() {
    semaphore.acquire()
    val startTime = System.currentTimeMillis()
    val client =
        object: AbstractActor<List<Int>>("Client") {
            override fun onReceive(message: List<Int>,
                          sender: Result<Actor<List<Int>>>) {
                println("Total time: " + (System.currentTimeMillis() - startTime))
                println("Input: ${testList.splitAt(40).first}")
                println("Result: ${message.splitAt(40).first}")
                semaphore.release()
            }
        }

    val receiver = Receiver("Receiver", client)
    val manager = Manager("Manager", testList, receiver, workers)
    manager.start()
    semaphore.acquire()
}
