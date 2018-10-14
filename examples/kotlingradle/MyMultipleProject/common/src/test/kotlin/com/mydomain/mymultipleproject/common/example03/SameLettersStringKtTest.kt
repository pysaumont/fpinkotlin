package com.mydomain.mymultipleproject.common.example03

import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class SameLettersStringKtTest: StringSpec() {

    init {
        "getCharUsed" {
            forAll(StringGenerator) { list: List<Pair<String, Map<Char, Int>>> ->
                getCharUsed(list.map { it.first }).keys.toSet() == list.asSequence().map { it.second }.toSet()
            }
        }
    }
}

