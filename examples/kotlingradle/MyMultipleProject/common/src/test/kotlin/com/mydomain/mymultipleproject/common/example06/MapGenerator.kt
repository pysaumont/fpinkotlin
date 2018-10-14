package com.mydomain.mymultipleproject.common.example06

import io.kotlintest.properties.Gen
import java.util.*

object MapGenerator : Gen<Map<Char, Int>> {

    override fun constants(): Iterable<Map<Char, Int>> = listOf(mapOf())

    override fun random(): Sequence<Map<Char, Int>> =
            Random().let { random ->
                generateSequence {
                    (0 until random.nextInt(25)).map {
                        Pair((random.nextInt(122 - 96) + 96).toChar(),
                                random.nextInt(10) + 1)
                    }.fold(mapOf<Char, Int>()) { map, pair ->
                        map + pair
                    }
                }
            }
}
