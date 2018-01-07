package com.fpinkotlin.optionaldata.exercise01

import com.fpinkotlin.generators.forAll
import io.kotlintest.matchers.shouldBe
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec

class OptionTest: StringSpec() {

    init {

        "getOrElse" {
            val x = Gen.int().generate()
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
