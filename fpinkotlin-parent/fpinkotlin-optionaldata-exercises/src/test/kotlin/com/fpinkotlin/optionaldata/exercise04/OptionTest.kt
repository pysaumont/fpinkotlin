package com.fpinkotlin.optionaldata.exercise04

import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class OptionTest: StringSpec() {

    init {

        "flatMap" {
            forAll { x: Int ->
                Option(x).flatMap { Option(it / 2) } == Option(x / 2)
            }
        }

        "flatMapNone" {
            forAll { x: Int ->
                (Option<Int>(null).flatMap { Option(it * x) }).isEmpty()
            }
        }
    }
}
