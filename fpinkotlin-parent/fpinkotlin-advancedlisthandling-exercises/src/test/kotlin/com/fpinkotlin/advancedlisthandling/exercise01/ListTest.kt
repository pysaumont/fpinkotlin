package com.fpinkotlin.advancedlisthandling.exercise01


import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec


class ListTest: StringSpec() {

    init {

        "length" {
            forAll(Gen.list(Gen.choose(1, 1_000))) { list ->
                List(*(list.toTypedArray())).lengthMemoized() == list.size
            }
        }
    }
}