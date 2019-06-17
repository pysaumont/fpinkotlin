package com.fpinkotlin.actors.listing16

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.fold
import kotlinx.coroutines.channels.produce
import java.util.*
import kotlin.Comparator

/**
 * A more idiomatic (and faster) version of the same problem, using a [SortedSet] instead of a [Heap]
 * to reorder the results.
 */
fun main() {

    /**
     * This is the function producing the channel receiving the input. It is defined as an extension function of the
     * [CoroutineScope] class, allowing to call [produce] directly (equivalent to [this.produce] where [this] is
     * the [CoroutineScope]. Beware that [CoroutineScope.produce] and [Channel.send] are both experimental and could
     * change in a future version of Kotlin coroutines.
     *
     * @param numbers
     *          The producer works from a list of pairs of integers, making all elements available to the receive channel.
     * @return  A receive channel with all data elements available.
     */
    fun CoroutineScope.produceTasks(numbers: Sequence<Pair<Int, Int>>): ReceiveChannel<Pair<Int, Int>> =
            produce {
                numbers.forEach {
                    send(it)
                }
            }

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
                     outputChannel: Channel<Pair<Int, Int>>): Job =
            GlobalScope.launch {
                for (pair in inputChannel) {
                    outputChannel.send(Pair(pair.first, fibonacci(pair.second)))
                }
            }

    /**
     * This function defines a + operator on [SortedSet] with a functional
     * syntax. This function is however not referentially transparent since
     * it modifies its argument. However, this is not a problem here because
     * it will be used only by the output channel, with no concurrent access.
     *
     * Note that we can't use the Kotlin + operator on sets, which is referentially
     * transparent, because even when used on a SortedSet, it returns a [Set].
     *
     * We could however define a referentially transparent + operator on [SortedSet] as
     *
     *  operator fun <T> SortedSet<T>.plus(element: T): SortedSet<T> {
     *      val result = sortedSetOf(this)
     *      result.add(element)
     *      return result
     *  }
     *
     *  This is a good example of how to chose the right level of referential transparency.
     *  Our function is not usable outside of the [main] function and is never applied
     *  to a [Set] existing outside of the [main] function. So it is perfectly safe to use.
     */
    operator fun <T> SortedSet<T>.plus(element: T): SortedSet<T> {
        this.add(element)
        return this
    }

    /**
     * Source data is a series of random positive integers in the range [0 - 35].
     * The 35 limit is chosen in order to avoid recursion problems when using the
     * [slowFibonacci] function. We could have used a [Sequence] instead of a
     * [List], in order to lazily create the input dat, which would have been more
     * representative of the general use case. However, generating
     */
    val numbers = sequence {
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
    }.take(200_000)

    /**
     * A comparator to compare pairs of integer by comparing their first elements.
     */
    val comparator: Comparator<Pair<Int, Int>> = Comparator {
        a, b -> a.first.compareTo(b.first)
    }

    /**
     * Measure the time needed to process the data
     */
    val result = measureTimeMillis {
        /**
         * The whole process is run inside a [runBlocking] block in order to allow using
         * suspending functions.
         */
        runBlocking {
            /**
             * Initialise the producer channel
             */
            val producer =
                    produceTasks(numbers.mapIndexed { index, num ->
                        Pair(index, num)
                    })

            /**
             * The channel where to send the results. Note that the channel queue size
             * is specified as 200_000 which is the exact size we need for the channel
             * to contain all our data. This allows for Kotlin to use a more efficient
             * data structure for the queue. You may prefer to use an unbounded queue
             * with Channel<Pair<Int, Int>>(UNLIMITED), which is in fact equivalent to
             * Channel<Pair<Int, Int>>(INT.MAX_VALUE). Try it and see how it affects
             * performance.
             */
            val resultChannel =
                    Channel<Pair<Int, Int>>(200_000)

            /**
             * Launch four parallel jobs. Change this number
             * to see how it affects the processing time.
             */
            val jobs = (0 until 4).map {
                launchWorker(producer, resultChannel)
            }

            /**
             * Wait for all jobs to complete.
             */
            jobs.forEach { job ->
                job.join()
            }

            /**
             * Close the channel in order to be able to bulk process its
             * content. This is not the ideal way to handle the problem.
             * It would be better to accumulate the results
             * while they are produced.
             */
            resultChannel.close()

            /**
             * [sortedSetOf(Comparator)] returns a [TreeSet] so [acc] must be specified of type
             * [SortedSet]. Alternatively, the [SortedSet.plus] operator function could be declared
             * as [TreeSet.plus]
             */
            resultChannel.fold(sortedSetOf(comparator)) { acc: SortedSet<Pair<Int, Int>>, p ->
                acc + p
            }
        }
    }
    println("Duration: ${result.first}")
    println("Input:    ${numbers.take(40).toList()}")
    println("Result:   ${result.second.take(40).map(Pair<Int, Int>::second).toList()}")
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
