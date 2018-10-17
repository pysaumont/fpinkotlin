package com.fpinkotlin.advancedlisthandling.exercise03


import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec


class ListTest: StringSpec() {

    init {

        "headSafe" {
            forAll(Gen.list(Gen.choose(1, 1_000))) { list ->
                List(*(list.toTypedArray())).lastSafe().getOrElse(0) ==  if (list.isNotEmpty()) list[list.size - 1] else 0
            }
        }
    }
}
