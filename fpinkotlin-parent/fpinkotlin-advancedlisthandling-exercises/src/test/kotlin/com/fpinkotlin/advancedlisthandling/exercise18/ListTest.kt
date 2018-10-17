package com.fpinkotlin.advancedlisthandling.exercise18


import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class ListTest: StringSpec() {

    init {

        "unfold" {
            forAll(Gen.choose(0, 100)) { number ->
                val result1 = unfold(0) {
                    if (it < number) Option(Pair(it, it + 1)) else Option()
                }
                val result2 = unfold(number) {
                    if (it > 0) Option(Pair(it - 1, it - 1)) else Option.invoke()
                }
                result1.toString() == result2.reverse().toString() &&
                        result1.foldLeft(0) { a -> { b -> a + b } } == (0 until number).sum()
            }
        }
    }
}
