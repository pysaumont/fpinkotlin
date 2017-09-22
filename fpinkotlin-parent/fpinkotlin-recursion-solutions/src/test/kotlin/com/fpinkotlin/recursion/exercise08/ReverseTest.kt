package com.fpinkotlin.recursion.exercise08


import com.fpinkotlin.generators.IntListPairGenerator
import com.fpinkotlin.generators.forAll
import io.kotlintest.specs.StringSpec

class ReverseTest : StringSpec() {

    init {

        "reverse" {
            forAll(IntListPairGenerator(), { (list1, list2) ->
                reverse(list1 + list2) == reverse(list2) + reverse(list1)
            })
        }
    }
}