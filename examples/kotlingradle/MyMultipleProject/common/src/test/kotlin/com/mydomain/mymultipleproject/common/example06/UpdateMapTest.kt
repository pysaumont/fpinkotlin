package com.mydomain.mymultipleproject.common.example06

import com.mydomain.mymultipleproject.common.example05.MapGenerator
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class UpdateMapTest: StringSpec() {

    init {
        "getCharUsed" {
            forAll(MapGenerator,
                    CharGenerator(Char::isLetterOrDigit)) { map: Map<Char, Int>, char ->
                updateMap(map, char)[char] == map.getOrDefault(char, 0) + 1
                        && updateMap(map, char) - char == map - char
            }
        }
    }
}