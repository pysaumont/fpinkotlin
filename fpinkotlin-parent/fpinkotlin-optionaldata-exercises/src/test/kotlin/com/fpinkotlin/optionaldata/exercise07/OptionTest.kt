package com.fpinkotlin.optionaldata.exercise07

import com.fpinkotlin.generators.DoubleListGenerator
import com.fpinkotlin.generators.forAll
import io.kotlintest.specs.StringSpec

class OptionTest: StringSpec() {

    init {

        "variance" {
            forAll(DoubleListGenerator(), { (array, list) ->
                variance(list) == when {
                    array.isEmpty() -> Option()
                    else -> Option((array.sum() / array.size).let { list.map { x ->
                        Math.pow((x - it), 2.0)
                    }.let {it.sum() / it.size} })
                }
            })
        }
    }
}
