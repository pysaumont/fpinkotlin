package com.fpinkotlin.recursion.exercise04

import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class MakeStringTest : StringSpec() {

    init {
        
        "makeString" {

            forAll(Gen.list(Gen.string())) { list ->
                makeString(list, ",") == list.joinToString(",")
            }
        }
    }
}

