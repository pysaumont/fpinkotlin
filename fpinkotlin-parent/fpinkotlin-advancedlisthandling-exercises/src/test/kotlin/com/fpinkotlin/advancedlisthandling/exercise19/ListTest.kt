package com.fpinkotlin.advancedlisthandling.exercise19


import com.fpinkotlin.generators.IntGenerator
import com.fpinkotlin.generators.forAll
import io.kotlintest.specs.StringSpec

class ListTest: StringSpec() {

    init {

        "unfold" {
            forAll(IntGenerator(0, 100), { number ->
                range(0, number).foldLeft(0) { a -> { b -> a + b }} == (0 until number).sum()
            })
        }
    }
}
