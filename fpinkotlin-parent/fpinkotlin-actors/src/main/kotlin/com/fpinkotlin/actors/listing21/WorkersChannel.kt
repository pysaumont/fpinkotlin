package com.fpinkotlin.actors.listing21

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.Comparator

/**
 * Functions like [ReceiveChannel.take] and [ReceiveChannel.fold] are deprecated in Kotlin 1.3
 * and will be removed in Kotlin 1.4 and replaced with [Flow]. This versions uses [Flow]
 * (experimental in Kotlin 1.3) combined with reactive stream publishers.
 */
fun main() {

    /**
     * Launch a worker job. A worker job consist in reading data from the input channel, processing it
     * and writing the result to the output channel, repeating this process as long as there are
     * some data available in the inputChannel.
     *
     * @param inputChannel
     *          The input channel, providing data for processing
     * @param outputChannel
     *          The output channel, to which the result of processing each piece of data is written
     * @return  A [Job] representing a reference to the current worker job. Thi reference will allow
     *          further manipulation of the job, such as cancellation or joining (waiting for the job
     *          to complete)
     */
    fun launchWorker(inputChannel: ReceiveChannel<Pair<Int, Int>>,
                     outputChannel: Channel<Pair<Int, Int>>): Job = GlobalScope.launch {
        for (pair in inputChannel) {
            outputChannel.send(Pair(pair.first, slowFibonacci(pair.second)))
        }
    }

    /**
     * A comparator to compare pairs of integer by comparing their first elements.
     */
    val pairComparator: Comparator<Pair<Int, Int>> = Comparator {
        a, b -> a.first.compareTo(b.first)
    }

    /**
     * Launch the receiver job that receives the data and either sends it to the output
     * channel it it is the expected one or store it in a sorted set otherwise.
     *
     * @param inputChannel
     *          The input channel providing the results.
     * @return  a Flow.
     */
    fun launchReceiver(inputChannel: ReceiveChannel<Pair<Int, Int>>): Flow<Pair<Int, Int>> = flow {
        val set = sortedSetOf(pairComparator)
        var expected = 0
        for (pair in inputChannel) {
            if (pair.first == expected) {
                emit(pair)
                expected++
                while (!set.isEmpty() && set.first().first == expected) {
                    val x = set.first()
                    emit(x)
                    set.remove(x)
                    expected++
                }
            } else {
                set.add(pair)
            }
        }
    }

    val numbers = 20_000

    runBlocking {

        /**
         * The number of parallel workers. Change this value to see how it affects
         * the result
         */
        val workers = 4

        /**
         * The channel where to send the results from the workers. Note that the
         * channel has no queue. As a consequence, producers will block when
         * sending to this channel until a consumer is ready to consume data.
         */
        val resultChannel = Channel<Pair<Int, Int>>()

        /**
         * Initialise the producer channel
         */
        val producer: ReceiveChannel<Pair<Int, Int>> = produce {
                Random(0).let {
                    for (x in 0 until numbers) {
                        send(Pair(x, it.nextInt(35)))
                    }
                }
        }

        /**
         * Launch four parallel jobs. Change this number
         * to see how it affects the processing speed.
         */
        (0 until workers).map {
            launchWorker(producer, resultChannel)
        }

        val startTime = System.currentTimeMillis()

        launchReceiver(resultChannel).take(numbers).map { it.second }.toList().let {
            println("Total time: " + (System.currentTimeMillis() - startTime))
            println("Result: ${it.take(40)}")
        }

        /**
         * Cancel all
         */
        producer.cancel()
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
