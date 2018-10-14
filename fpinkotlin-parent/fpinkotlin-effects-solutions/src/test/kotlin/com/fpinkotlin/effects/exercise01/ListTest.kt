package com.fpinkotlin.effects.exercise01

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class ListTest: StringSpec() {

    init {

        "forEach empty" {
            val list = List<Int>()
            var result: String = ""
            list.forEach { result += it }
            result shouldBe ""
        }

        "forEach 1" {
            val list = List(1)
            var result: String = ""
            list.forEach { result += it }
            result shouldBe "1"
        }

        "forEach more" {
            val list = List(1, 2, 3, 4, 5)
            var result: String = ""
            list.forEach { result += it }
            result shouldBe "12345"
        }
    }
}
