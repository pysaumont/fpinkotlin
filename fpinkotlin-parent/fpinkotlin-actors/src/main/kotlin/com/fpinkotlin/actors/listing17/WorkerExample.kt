package com.fpinkotlin.actors.listing17

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.*

/**
 * The base class for actor messages
 */
sealed class ActorMessage

/**
 * A message that is used to send a task to a worker. It contains both
 * the value to process and the number of the task, useful for reordering
 * results after parallel processing.
 */
class ComputeMessage(val index: Int, val value: Int) : ActorMessage()

/**
 * A message containing the deferred result of the computation.
 */
class ResultMessage(val response: CompletableDeferred<List<Int>>) : ActorMessage()

/**
 * A comparator to compare pairs of integers by comparing their first elements.
 */
val pairComparator: Comparator<ComputeMessage> = Comparator {
    a, b -> a.index.compareTo(b.index)
}

/**
 * A function producing a [ReceiveChannel] from a [Sequence] of data to process. The
 * channel can be seen as a mutable sequence, since each time a value is received,
 * it is removed from the channel.
 *
 * @param numbers
 *          The sequence of numbers to process
 * @return  A channel of message
 */
fun CoroutineScope.produceData(numbers: Sequence<ComputeMessage>): ReceiveChannel<ComputeMessage> =
    produce {
        numbers.forEach {
            send(it)
        }
    }

/**
 * The parallel process launcher.
 *
 * @param workers
 *          The number of coroutines to use as workers for parallelizing the tasks.
 * @param process
 *          The process to execute in parallel
 */
suspend fun parallelProcess(workers: Int, process: suspend () -> Unit) {
    coroutineScope {
        repeat(workers) {
            launch {
                process()
            }
        }
    }
}

/**
 * The processor actor
 */
fun CoroutineScope.processorActor() =
    actor<ActorMessage> {
        val set = sortedSetOf(pairComparator)
        for (msg in channel) {
            when (msg) {
                is ComputeMessage ->  set.add(msg)
                is ResultMessage -> msg.response.complete(set.toList().map {
                    it.value
                })
            }
        }
    }

fun main() {
    runBlocking {
        val numbers = 20_000
        val workers = 1

        val sequence = sequence {
            /**
             * The pseudo random generator is initialized with an integer value (0) in order
             * to always produce the same sequence of pseudo random numbers, which is useful
             * for comparisons. It is initialized here in order that the sequence may be
             * iterated several times producing the same result. This is used for displaying
             * the numbers along with the corresponding Fibonacci values.
             */
            Random(0).let {
                yieldAll(generateSequence {
                    it.nextInt(35)
                })
            }
        }.take(numbers)

        /**
         * The first 40 values, used to display the result.
         */
        val input = sequence.take(40).toList()

        val result = measureTimeMillis {

            /**
             * Create a channel holding the data to process
             */
            val receiveChannel = produceData(sequence.mapIndexed { index, num ->
                ComputeMessage(index, num)
            })

            /**
             * Create the processing actor
             */
            val processorActor = processorActor()

            withContext(Dispatchers.Default) {
                parallelProcess(workers) {
                    for (msg in receiveChannel) processorActor.send(ComputeMessage(msg.index, slowFibonacci(msg.value)))
                }
            }

            /**
             * Create a response message and send it to the processing actor
             * to be completed with the actual result
             */
            val response = CompletableDeferred<List<Int>>()
            processorActor.send(ResultMessage(response))

            /**
             * Wait for the completed result
             */
            val result = response.await()
            processorActor.close()

            /**
             * Return the result
             */
            result
        }
        val output = result.second.take(40)
        println("Duration: ${result.first}")
        println("Input:    $input")
        println("Result:   $output")
    }
}

/**
 * Calls a function and return a pair containing the time needed for computing the result
 * and the result itself.
 */
inline fun <T> measureTimeMillis(function: () -> T): Pair<Long, T> =
    System.currentTimeMillis().let { start ->
        function().let { result ->
            Pair(System.currentTimeMillis() - start, result)
        }
    }

/**
 * A fast function for computing the value of a number in the Fibonacci series
 * given its position in the series. Positions are counted from 0.
 *
 * @param pos
 *          The position in the Fibonacci series for which we want to compute
 *          the value
 * @return  The value at the given position
 */
private fun fibonacci(pos: Int): Int {
    tailrec fun fibonacci(acc1: Int, acc2: Int, x: Int): Int = when (x) {
        0    -> 1
        1    -> acc1 + acc2
        else -> fibonacci(acc2, acc1 + acc2, x - 1)
    }
    return fibonacci(0, 1, pos)
}

/**
 * A slow version of a function computing the value of a number in the
 * Fibonacci series given its position in the series. Positions are
 * counted from 0. Note that this function is doubly recursive and thus
 * will hang forever (long before blowing the stack!) for values above
 * 35 - 40
 *
 * @param pos
 *          The position in the Fibonacci series for which we want to compute
 *          the value
 * @return  The value at the given position
 */
private fun slowFibonacci(pos: Int): Int {
    return when (pos) {
        0    -> 1
        1    -> 1
        else -> slowFibonacci(pos - 1) + slowFibonacci(pos - 2)
    }
}
