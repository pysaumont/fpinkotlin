package com.fpinkotlin.recursion.exercise17


import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class RangeTest : StringSpec() {

    init {

        "range" {
            forAll(Gen.choose(0, 1000), Gen.choose(0, 1000)) { a: Int, b: Int ->
                a > b || map((a until b).toList()) { it * 2} == (a * 2 until b * 2 step 2).toList()
            }
        }
    }
}