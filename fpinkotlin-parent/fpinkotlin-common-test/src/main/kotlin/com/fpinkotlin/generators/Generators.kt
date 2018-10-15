package com.fpinkotlin.generators

import io.kotlintest.properties.Gen
import io.kotlintest.properties.Gen.Companion.double
import io.kotlintest.properties.Gen.Companion.int
import java.util.*


fun <T> list(gen: Gen<T>, minLength: Int = 0, maxLength: Int = 100): Gen<kotlin.collections.List<T>> =

  object : Gen<kotlin.collections.List<T>> {

      override fun constants(): Iterable<List<T>> = listOf()

      override fun random(): Sequence<List<T>> =
          (maxLength - minLength).let {
              if (it <= 0)
                  0
              else
                  minLength + Random().nextInt(it)
          }.let { length ->
              generateSequence(0) { it + 1 }.take(length).map { gen.random().toList() }
          }

  }

fun <T>  forAll(generator: Gen<T>, fn: (a: T) -> Boolean, numTests: Int = 1_000) {
    generator.constants().forEach {
        if (!fn(it)) throw AssertionError("Property failed for\n$it")
    }
    generator.random().take(numTests).forEach {
        if (!fn(it)) throw AssertionError("Property failed for\n$it")
    }
}

class IntGenerator(private val min: Int = 0, val max: Int = Int.MAX_VALUE): Gen<Int> {
    override fun constants(): Iterable<Int> = listOf(min, max)

    override fun random(): Sequence<Int> = int().random().filter { it in min..max }
}

class DoubleGenerator(private val min: Double = 0.0, val max: Double = Double.MAX_VALUE): Gen<Double> {
    override fun constants(): Iterable<Double> = listOf(min, max)

    override fun random(): Sequence<Double> = double().random().filter { it in min..max }
}

class CharGenerator(private val min: Char = ' ', val max: Char = '~'): Gen<Char> {
    override fun constants(): Iterable<Char> = listOf(min, max)

    override fun random(): Sequence<Char> =
            int().random().filter { it in min.toInt()..max.toInt() }.map { it.toChar() }
}

class IntPairGenerator(private val min: Int = 0, private val max: Int = 100) : Gen<Pair<Int, Int>> {
    override fun constants(): Iterable<Pair<Int, Int>> =
            listOf(
                    Pair(0, min),
                    Pair(0, max),
                    Pair(0, 0),
                    Pair(min, min),
                    Pair(min, max),
                    Pair(min, 0),
                    Pair(max, max),
                    Pair(max, min),
                    Pair(max, 0))

    override fun random(): Sequence<Pair<Int, Int>> =
            int().random()
                    .filter { it in min..max }
                    .zip(int().random().filter {  it in min..max  })
}

class IntTripleGenerator(private val min: Int = 0, private val max: Int = 100) : Gen<Triple<Int, Int, Int>> {
    override fun constants(): Iterable<Triple<Int, Int, Int>> =
            listOf(
                    Triple(0, 0, 0),
                    Triple(0, 0, min),
                    Triple(0, 0, max),
                    Triple(0, min, 0),
                    Triple(0, min, min),
                    Triple(0, min, max),
                    Triple(0, max, 0),
                    Triple(0, max, min),
                    Triple(0, max, max),
                    Triple(min, 0, 0),
                    Triple(min, 0, min),
                    Triple(min, 0, max),
                    Triple(min, min, 0),
                    Triple(min, min, min),
                    Triple(min, min, max),
                    Triple(min, max, 0),
                    Triple(min, max, min),
                    Triple(min, max, max),
                    Triple(max, 0, 0),
                    Triple(max, 0, min),
                    Triple(max, 0, max),
                    Triple(max, min, 0),
                    Triple(max, min, min),
                    Triple(max, min, max),
                    Triple(max, max, 0),
                    Triple(max, max, min),
                    Triple(max, max, max))

    override fun random(): Sequence<Triple<Int, Int, Int>> =
            int().random()
                    .filter { it in min..max }
                    .zip(int().random()
                            .filter { it in min..max })
                    .zip(int().random()
                            .filter { it in min..max })
                    .map { Triple(it.first.first, it.first.second, it.second) }
}

class IntDoublePairGenerator: Gen<Pair<Int, Double>> {
    override fun constants(): Iterable<Pair<Int, Double>> =
            listOf(
                    Pair(0, Double.MIN_VALUE),
                    Pair(0, Double.MAX_VALUE),
                    Pair(0, 0.0),
                    Pair(0, Double.NEGATIVE_INFINITY),
                    Pair(0, Double.POSITIVE_INFINITY),
                    Pair(0, Double.NaN),
                    Pair(Int.MIN_VALUE, Double.MIN_VALUE),
                    Pair(Int.MIN_VALUE, Double.MAX_VALUE),
                    Pair(Int.MIN_VALUE, 0.0),
                    Pair(Int.MIN_VALUE, Double.NEGATIVE_INFINITY),
                    Pair(Int.MIN_VALUE, Double.POSITIVE_INFINITY),
                    Pair(Int.MIN_VALUE, Double.NaN),
                    Pair(Int.MAX_VALUE, Double.MIN_VALUE),
                    Pair(Int.MAX_VALUE, Double.MAX_VALUE),
                    Pair(Int.MAX_VALUE, 0.0),
                    Pair(Int.MAX_VALUE, Double.NEGATIVE_INFINITY),
                    Pair(Int.MAX_VALUE, Double.POSITIVE_INFINITY),
                    Pair(Int.MAX_VALUE, Double.NaN))

    override fun random(): Sequence<Pair<Int, Double>> =
            int().random().let {
                it.zip(double().random())
            }
}

typealias Stack<A> = com.fpinkotlin.common.List<A>

class IntListGenerator(private val minLength: Int = 0,
                       private val maxLength: Int = 100,
                       private val minValue: Int = Int.MIN_VALUE,
                       private val maxValue: Int = Int.MAX_VALUE): Gen<Pair<Array<Int>, Stack<Int>>> {

    override fun constants(): Iterable<Pair<Array<Int>, Stack<Int>>> =
            listOf(Pair(arrayOf(), Stack()))

    override fun random(): Sequence<Pair<Array<Int>, Stack<Int>>> =
            list(IntGenerator(minValue, maxValue), minLength, maxLength)
                    .map { Pair(it.toTypedArray(),
                            it.fold(Stack<Int>()) { list, i ->
                                Stack.cons(i, list)}) }.random()
}

class DoubleListGenerator(private val minLength: Int = 0,
                          private val maxLength: Int = 100,
                          private val minValue: Double = Double.MIN_VALUE,
                          private val maxValue: Double = Double.MAX_VALUE): Gen<Pair<Array<Double>, List<Double>>> {

    override fun constants(): Iterable<Pair<Array<Double>, List<Double>>> =
            listOf(Pair(arrayOf(), listOf()))

    override fun random(): Sequence<Pair<Array<Double>, List<Double>>> =
            list(DoubleGenerator(minValue, maxValue), minLength, maxLength)
                    .map { Pair(it.toTypedArray(),
                            it.fold(listOf<Double>()) { list, i ->
                                list + i}) }.random()
}

class CharListGenerator(private val minLength: Int = 0,
                        private val maxLength: Int = 100,
                        private val minValue: Char = ' ',
                        private val maxValue: Char = '~'): Gen<Pair<Array<Char>, Stack<Char>>> {

    override fun constants(): Iterable<Pair<Array<Char>, Stack<Char>>> =
            listOf(Pair(arrayOf(), Stack()))

    override fun random(): Sequence<Pair<Array<Char>, Stack<Char>>> =
            list(CharGenerator(minValue, maxValue), minLength, maxLength)
                    .map { Pair(it.toTypedArray(),
                            it.fold(Stack<Char>()) { list, i ->
                                Stack.cons(i, list)}) }.random()
}

class IntKListGenerator(private val minLength: Int = 0,
                        private val maxLength: Int = 100) : Gen<Pair<Array<Int>, List<Int>>> {

    override fun constants(): Iterable<Pair<Array<Int>, List<Int>>> = listOf()

    override fun random(): Sequence<Pair<Array<Int>, List<Int>>> =
            list(IntGenerator(), minLength, maxLength).random().map { Pair(it.toTypedArray(), it) }
}

class CharKListGenerator(private val minLength: Int = 0,
                        private val maxLength: Int = 100) : Gen<Pair<Array<Char>, List<Char>>> {

    override fun constants(): Iterable<Pair<Array<Char>, List<Char>>> = listOf()

    override fun random(): Sequence<Pair<Array<Char>, List<Char>>> =
            list(CharGenerator(), minLength, maxLength).random().map { Pair(it.toTypedArray(), it) }
}

class IntKListPairGenerator(private val minLength: Int = 0, private val maxLength: Int = 100) : Gen<Pair<List<Int>, List<Int>>> {

    override fun constants(): Iterable<Pair<List<Int>, List<Int>>> =
            listOf(Pair(listOf(), listOf()))

    override fun random(): Sequence<Pair<List<Int>, List<Int>>> =
            IntKListGenerator(minLength, maxLength).random().let {
                IntKListGenerator(minLength, maxLength).random().zip(it)
                        .map { Pair(it.first.second, it.second.second) }
            }
}
