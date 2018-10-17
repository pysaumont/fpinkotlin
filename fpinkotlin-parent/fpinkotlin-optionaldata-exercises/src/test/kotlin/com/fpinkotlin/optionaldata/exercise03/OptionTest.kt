package com.fpinkotlin.optionaldata.exercise03

import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class OptionTest: StringSpec() {

    init {

        "map" {
            forAll { x: Int ->
                Option(x).map { it.toString() } == Option(x.toString())
            }
        }

        "mapNone" {
            forAll { x: Int ->
                (Option<Int>(null).map { it * x }).isEmpty()
            }
        }
    }
}
