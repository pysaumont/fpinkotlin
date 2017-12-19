package com.fpinkotlin.advancedlisthandling.exercise23


import com.fpinkotlin.generators.IntGenerator
import java.math.BigInteger
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


/*
 * Count the time necessary to fold a list of 35 000 random longs
 * between 0 and 30 by adding the result of applying a particularly
 * slow implementation of the fibonacci function.
 */
object SerialParallelFoldLeftBenchmark {

    @JvmStatic
    fun main(args: Array<String>) {
        val testLimit = 35000

        val generator = IntGenerator(0, 30)

        val testList: List<Long> = range(0, testLimit).map {
                generator.generate().toLong()
        }

        val es2 = Executors.newFixedThreadPool(2)
        val es4 = Executors.newFixedThreadPool(4)
        val es8 = Executors.newFixedThreadPool(8)

        testSerial(5, testList, System.currentTimeMillis())
        println("Duration serial 1 thread: ${testSerial(10, testList, System.currentTimeMillis())}")
        testParallel(es2, 5, testList, System.currentTimeMillis())
        println("Duration parallel 2 threads: ${testParallel(es2,10, testList, System.currentTimeMillis())}")
        testParallel(es4, 5, testList, System.currentTimeMillis())
        println("Duration parallel 4 threads: ${testParallel(es4,10, testList, System.currentTimeMillis())}")
        testParallel(es8, 5, testList, System.currentTimeMillis())
        println("Duration parallel 8 threads: ${testParallel(es8,10, testList, System.currentTimeMillis())}")
        es2.shutdown()
        es4.shutdown()
        es8.shutdown()
    }

    private val f = { a: BigInteger -> { b: Long -> a.add(BigInteger.valueOf(fibo(b))) } }
    private val g = { a: BigInteger -> { b: BigInteger -> a.add(b) } }

    private fun testSerial(n: Int, list: List<Long>, startTime: Long): Long {
        (0 until n).forEach {
            list.foldLeft(BigInteger.ZERO, f).let { println("Result:  $it") }
        }
        return System.currentTimeMillis() - startTime
    }

    private fun testParallel(es: ExecutorService, n: Int, list: List<Long>, startTime: Long): Long {
        (0 until n).forEach {
            list.parFoldLeft(es, BigInteger.ZERO, f, g).forEach({ println("Result: $it") }, { println("Exception:  ${it.message}") }, { println("Empty result") })
        }
        return System.currentTimeMillis() - startTime
    }

    private fun fibo(x: Long): Long {
        return when (x) {
            0L   -> 0
            1L   -> 1
            else -> fibo(x - 1) + fibo(x - 2)
        }
    }
}
