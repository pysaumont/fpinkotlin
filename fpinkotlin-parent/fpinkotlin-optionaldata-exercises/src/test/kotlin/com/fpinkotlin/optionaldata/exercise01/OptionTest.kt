package com.fpinkotlin.optionaldata.exercise01

import com.fpinkotlin.generators.forAll
import io.kotlintest.properties.Gen
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import java.util.*

class OptionTest: StringSpec() {

    private val random = Random()

    init {

        "getOrElse" {
            val x = random.nextInt()
            val y: Int? = null
            forAll(Gen.int(), { z ->
                Option(z).getOrElse(x) == z && Option(y).getOrElse(x) == x
            })
        }

        "getOrElse None" {
            val option: Option<Int> = Option.None
            option.getOrElse(1) shouldBe  1
        }
    }
}
