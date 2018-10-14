package com.mydomain.mymultipleproject.common.example05

import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec
import java.util.*

class UpdateMapTest: StringSpec() {

    val random = Random()

    init {
        "getCharUsed" {
            forAll(MapGenerator) { map: Map<Char, Int> ->
                (random.nextInt(122 - 96) + 96).toChar().let {
                    if (map.containsKey(it)) {
                        updateMap(map, it)[it] == map[it]!! + 1
                    } else {
                        updateMap(map, it)[it] == 1
                    } && updateMap(map, it) - it == map - it
                }
            }
        }
    }
}