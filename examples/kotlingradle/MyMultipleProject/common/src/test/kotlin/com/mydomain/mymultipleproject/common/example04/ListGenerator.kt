package com.mydomain.mymultipleproject.common.example04

import io.kotlintest.properties.Gen
import io.kotlintest.properties.shrinking.ListShrinker
import java.util.*


class ListGenerator<T>(private val gen: Gen<T>,
                       private val maxLength: Int) : Gen<List<T>> {

    private val random = Random()

    override fun constants(): Iterable<List<T>> = listOf(gen.constants().toList())

    override fun random(): Sequence<List<T>> = generateSequence {
        val size = random.nextInt(maxLength)
        gen.random().take(size).toList()
    }

    override fun shrinker() = ListShrinker<T>()
}
