package com.fpinkotlin.optionaldata.exercise02

import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec
import java.util.*

class OptionTest: StringSpec() {

    private val random = Random()

    init {

        "getOrElse" {
            forAll { x: Int, z: Int ->
                Option(z).getOrElse { x } == z && Option(null as Int?).getOrElse { x } == x
            }
        }
    }
}
