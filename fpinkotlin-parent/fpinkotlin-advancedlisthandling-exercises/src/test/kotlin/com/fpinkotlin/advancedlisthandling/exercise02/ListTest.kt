package com.fpinkotlin.advancedlisthandling.exercise02


import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec


class ListTest: StringSpec() {

    init {

        "headSafe" {
            forAll(Gen.list(Gen.choose(1, 1_000))) { list ->
                List(*(list.toTypedArray())).headSafe().getOrElse(0) ==  if (list.isNotEmpty()) list[0] else 0
            }
        }
    }
}