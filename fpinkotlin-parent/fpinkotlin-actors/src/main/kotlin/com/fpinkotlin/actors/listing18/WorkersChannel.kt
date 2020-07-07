package com.fpinkotlin.actors.listing18

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import java.util.*
import kotlin.Comparator


/**
 * This is a version using either [ReceiveChannel.toList] or [ReceiveChannel.fold] functions to collect the result.
 * [ReceiveChannel.fold] is slightly faster using a Kotlin modifiable list for folding, and much slower when using
 * an Kotlin unmodifiable list. Using our immutable data sharing list will be exercised in listing20.
 *
 */
@ExperimentalCoroutinesApi
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
     * @param outputChannel
     *          The output channel where reordered data is to be sent.
     * @return  The job that will reorder data
     */
    fun launchReceiver(inputChannel: ReceiveChannel<Pair<Int, Int>>,
                       outputChannel: Channel<Pair<Int, Int>>): Job = GlobalScope.launch {
        val set = sortedSetOf(pairComparator)
        var expected = 0
        for (pair in inputChannel) {
            if (pair.first == expected) {
                outputChannel.send(pair)
                expected++
                while (!set.isEmpty() && set.first().first == expected) {
                    val x = set.first()
                    outputChannel.send(x)
                    set.remove(x)
                    expected++
                }
            } else {
                set.add(pair)
            }
        }
    }

    /**
     * Launch the client job that process the results. In this version, elements are
     * computed pseudo lazily meaning that only the next element computed eagerly.
     * As a consequence, we must pass the number of elements to consume and the effect
     * to apply to them. When this is done, we mus cancel the channel.
     *
     * Beware not to use fold with Kotlin "non modifiable" list to consume the channel.
     * Unlike our immutable List from chapter5, Kotlin "non modifiable" list
     * does not use data sharing, creating a full new list on each add operation
     * resulting in very poor performance. This will work very slowly:
     *
     * inputChannel.take(num).fold(listOf<Int>()) { list, value ->
     *        list + value
     *    }
     *
     * Replacing Kotlin list with our immutable list will work fine, and would be the
     * faster solution if we weren't forced to reverse the list in the end (see listing20).
     * Using Kotlin modifiable list will also work fine :
     *
     * inputChannel.take(num).fold(mutableListOf()) { list, value ->
     *        list.add(value.second)
     *        list
     *    }
     *
     * A more idiomatic way is to use the toList() function, but this is slower
     * than folding.
     *
     * Also note that [ReceiveChannel.take] is obsolete in Kotlin 1.3.40 (meaning
     * annotated with @ObsoleteCoroutinesApi) and will be removed in future versions.
     *
     * @param inputChannel
     *          The input channel providing the results.
     * @param num
     *          The number of elements to read from the channel
     * @param effect
     *          The effect to apply to elements
     * @return  The job that will consume the data from the input channel
     */
    fun launchClient(inputChannel: ReceiveChannel<Pair<Int, Int>>, num: Int, effect: (List<Int>) -> Unit): Job = GlobalScope.launch {
        effect(inputChannel.take(num).map { it.second }.toList())
//        effect(inputChannel.take(num).fold(mutableListOf()) { list, value ->
//            list.add(value.second)
//            list
//        })
        cancel()
    }

    val numbers = 20_000

    /**
     * Source data is a series of random positive integers in the range [0 - 35].
     * The 35 limit is chosen in order to avoid recursion problems when using the
     * [slowFibonacci] function. We could have used a [Sequence] instead of a
     * [List], in order to lazily create the input dat, which would have been more
     * representative of the general use case. However, generating
     */
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

    val input = sequence.take(40).toList()

    runBlocking {

        /**
         * The number of parallel workers. Change this value to see how it affects
         * the result
         */
        val workers = 4

        /**
         * Initialise the producer channel
         */
        val producer: ReceiveChannel<Pair<Int, Int>> =
            produceTasks(sequence.mapIndexed { index, num ->  Pair(index, num)})

        /**
         * The channel where to send the results from the workers. Note that the
         * channel has no queue. As a consequence, producers will block when
         * sending to this channel until a consumer is ready to consume data.
         */
        val resultChannel = Channel<Pair<Int, Int>>()

        /**
         * Launch four parallel jobs. Change this number
         * to see how it affects the processing speed.
         */
        (0 until workers).map {
            launchWorker(producer, resultChannel)
        }

        val clientChannel = Channel<Pair<Int, Int>>(Channel.UNLIMITED)

        launchReceiver(resultChannel, clientChannel)

        val startTime = System.currentTimeMillis()

        val job = launchClient(clientChannel, numbers) {
            println("Total time: " + (System.currentTimeMillis() - startTime))
            println("Input: $input")
            println("Result: ${it.take(40)}")
        }

        job.join()

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
