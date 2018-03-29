package com.fpinkotlin.optionaldata.exercise04

import com.fpinkotlin.generators.forAll
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec

class OptionTest: StringSpec() {

    init {

        "flatMap" {
            forAll(Gen.int(), { x ->
                Option(x).flatMap { Option(it / 2) } == Option(x / 2)
            })
        }

        "flatMapNone" {
            forAll(Gen.int(), { x ->
                (Option<Int>(null).flatMap { Option(it * x) }).isEmpty()
            }, 1)
        }

        "map" {

            forAll(Gen.int(), { x ->
                val expected = Option(x / 4)
                val result =  Option(x).map { it / 2 }.map { it / 2 }
                println(result)
                println(expected)
                result == expected
            })

        }
    }
}
