package com.fpinkotlin.optionaldata.exercise07

import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class OptionTest: StringSpec() {

    init {

        "variance" {
            forAll { list: List<Double> ->
                variance(list) == when {
                    list.isEmpty() -> Option()
                    else -> Option((list.sum() / list.size).let { value ->
                        list.map { x ->
                        Math.pow((x - value), 2.0)
                    }.let { it.sum() / it.size} })
                }
            }
        }
    }
}
