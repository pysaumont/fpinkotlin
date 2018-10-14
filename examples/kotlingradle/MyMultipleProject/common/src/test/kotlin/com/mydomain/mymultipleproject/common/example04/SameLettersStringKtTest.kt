package com.mydomain.mymultipleproject.common.example04

import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class SameLettersStringKtTest: StringSpec() {

    init {
        "getCharUsed" {
            forAll(StringGenerator(100, 100)) { list: List<Pair<String, Map<Char, Int>>> ->
                getCharUsed(list.map { it.first }).keys.toSet() == list.asSequence().map { it.second }.toSet()
            }
        }
    }
}

