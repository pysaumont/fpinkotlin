package com.fpinkotlin.actors.listing18

import com.fpinkotlin.common.Result
import java.util.concurrent.Semaphore


private val semaphore = Semaphore(1)
private const val listLength = 200_000
private const val workers = 4
private val rnd = java.util.Random(0)
private val testList = (0 until listLength).map { rnd.nextInt(35) }

fun main() {
    semaphore.acquire()
    val startTime = System.currentTimeMillis()
    val client =
        object: AbstractActor<List<Int>>("Client") {
            override fun onReceive(message: List<Int>,
                                   sender: Result<Actor<List<Int>>>) {
                println("Total time: " + (System.currentTimeMillis() - startTime))
                println("Input: ${testList.take(40)}")
                println("Result: ${message.take(40)}")
                semaphore.release()
            }
        }

    val receiver = Receiver("Receiver", client)

    val manager = Manager("Manager", testList, receiver, workers)

    manager.start()
    semaphore.acquire()
}