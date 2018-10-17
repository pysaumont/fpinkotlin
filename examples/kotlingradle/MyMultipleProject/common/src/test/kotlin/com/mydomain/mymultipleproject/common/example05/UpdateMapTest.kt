package com.mydomain.mymultipleproject.common.example05

import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec
import java.util.*

class UpdateMapTest: StringSpec() {

    private val random = Random()

    private val min = 'a'

    private val max = 'z'

    init {
        "getCharUsed" {
            forAll(mapGenerator()) { map: Map<Char, Int> ->
                (random.nextInt(max.toInt() - min.toInt()) + min.toInt()).toChar().let {
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