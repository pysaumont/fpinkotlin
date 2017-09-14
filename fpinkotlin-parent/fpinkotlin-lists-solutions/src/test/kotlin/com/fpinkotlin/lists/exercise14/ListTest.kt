package com.fpinkotlin.lists.exercise14


import com.fpinkotlin.generators.forAll
import com.fpinkotlin.generators.list
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec
import java.util.*

class ListTest: StringSpec() {

    val f: (Int) -> (String) -> String = { a -> { b -> "$a + ($b)"} }

    init {

        "concat0" {
            forAll(IntListGenerator(0, 0), { (_, second) ->
                sum(second) == sum(second.concat(List())) &&
                    sum(second) == sum(List<Int>().concat(second))
            })
        }

        "concat" {
            forAll(IntListPairGenerator(), { (list1, list2) ->
                val concat1 = list1.concat(list2).drop(1)
                val concat2 = list2.reverse().concat(list1.reverse()).reverse().drop(1)
                sum(concat1) == sum(concat2)
            }, 10)
        }

        "concat_" {
            forAll(IntListPairGenerator(), { (list1, list2) ->
                val concat1 = list1.concat_(list2).drop(1)
                val concat2 = list2.reverse().concat_(list1.reverse()).reverse().drop(1)
                sum(concat1) == sum(concat2)
            }, 10)
        }
    }
}

class IntListGenerator(private val minLength: Int = 0, private val maxLength: Int = 100) : Gen<Pair<Array<Int>, List<Int>>> {

    override fun generate(): Pair<Array<Int>, List<Int>> {
        val array: Array<Int> = list(IntGenerator(1_00), minLength, maxLength).generate().toTypedArray()
        return Pair(array, List(*array))
    }
}

class IntListPairGenerator(private val minLength: Int = 0, private val maxLength: Int = 100) : Gen<Pair<List<Int>, List<Int>>> {

    override fun generate(): Pair<List<Int>, List<Int>> {
        val array1: Array<Int> = list(IntGenerator(1_00), minLength, maxLength).generate().toTypedArray()
        val array2: Array<Int> = list(IntGenerator(1_00), minLength, maxLength).generate().toTypedArray()
        return Pair(List(*array1), List(*array2))
    }
}

class IntGenerator(private val max: Int): Gen<Int> {
    private val RANDOM = Random()
    override fun generate(): Int = RANDOM.nextInt(max)
}

