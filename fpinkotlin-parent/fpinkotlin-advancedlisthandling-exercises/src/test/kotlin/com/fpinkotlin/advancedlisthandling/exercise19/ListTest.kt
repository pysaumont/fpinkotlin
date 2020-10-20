package com.fpinkotlin.advancedlisthandling.exercise19


import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class ListTest: StringSpec() {

    init {

        "range" {
            forAll(Gen.choose(0, 100)) { number ->
                range(0, number).foldLeft(0) { a -> { b -> a + b }} == (0 until number).sum()
            }
        }
    }
}
