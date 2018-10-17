package com.mydomain.mymultipleproject.common.example06

import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class UpdateMapTest: StringSpec() {

    init {
        "getCharUsed" {
            forAll(MapGenerator,
                    charGenerator(Char::isLetterOrDigit)) { map: Map<Char, Int>, char ->
                updateMap(map, char)[char] == map.getOrDefault(char, 0) + 1
                        && updateMap(map, char) - char == map - char
            }
        }
    }
}