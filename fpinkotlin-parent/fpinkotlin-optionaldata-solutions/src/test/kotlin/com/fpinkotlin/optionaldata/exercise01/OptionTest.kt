package com.fpinkotlin.optionaldata.exercise01

import io.kotlintest.properties.forAll
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import java.util.*

class OptionTest: StringSpec() {

    private val random = Random()

    init {

        "getOrElse" {
            val x = random.nextInt()
            val y: Int? = null
            forAll { z: Int ->
                Option(z).getOrElse(x) == z && Option(y).getOrElse(x) == x
            }
        }

        "getOrElse None" {
            val option: Option<Int> = Option.None
            option.getOrElse(1) shouldBe  1
        }
    }
}
