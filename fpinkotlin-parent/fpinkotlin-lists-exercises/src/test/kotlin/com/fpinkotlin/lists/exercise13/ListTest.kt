package com.fpinkotlin.lists.exercise13


import com.fpinkotlin.generators.forAll
import com.fpinkotlin.generators.list
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec
import java.util.*

class ListTest: StringSpec() {

    val f: (Int) -> (String) -> String = { a -> { b -> "$a + ($b)"} }

    init {

        "cofoldRight0" {
            forAll(IntListGenerator(0, 0), { (_, second) ->
                second.cofoldRight("0", f) == "0"
            }, 1)
        }

        "cofoldRight" {
            forAll(IntListGenerator(), { (_, second) ->
                second.cofoldRight("0", f) == second.foldRight("0", f)
            })
        }
    }
}

class IntListGenerator(private val minLength: Int = 0, private val maxLength: Int = 100) : Gen<Pair<Array<Int>, List<Int>>> {

    override fun generate(): Pair<Array<Int>, List<Int>> {
        val array: Array<Int> = list(IntGenerator(1_00), minLength, maxLength).generate().toTypedArray()
        return Pair(array, List(*array))
    }
}

class IntGenerator(private val max: Int): Gen<Int> {
    private val RANDOM = Random()
    override fun generate(): Int = RANDOM.nextInt(max)
}

