package com.fpinkotlin.recursion.exercise08


import com.fpinkotlin.recursion.exercise07.reverse
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class ReverseTest : StringSpec() {

    init {

        "reverse" {
            forAll { list1: List<Int>, list2: List<Int> ->
                reverse(list1 + list2) == reverse(list2) + reverse(list1)
            }
        }
    }
}