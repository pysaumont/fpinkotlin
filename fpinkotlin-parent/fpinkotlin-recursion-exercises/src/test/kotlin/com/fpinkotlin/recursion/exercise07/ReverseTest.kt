package com.fpinkotlin.recursion.exercise07


import com.fpinkotlin.generators.IntKListPairGenerator
import com.fpinkotlin.generators.forAll
import com.fpinkotlin.recursion.exercise08.reverse
import io.kotlintest.specs.StringSpec

class ReverseTest : StringSpec() {

    init {

        "reverse" {
            forAll(IntKListPairGenerator(), { (list1, list2) ->
                reverse(list1 + list2) == reverse(list2) + reverse(list1)
            })
        }
    }
}