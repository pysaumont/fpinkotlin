package com.mydomain.mymultipleproject.common.example05

import io.kotlintest.properties.Gen

fun mapGenerator(min: Char = 'a', max: Char = 'z'): Gen<Map<Char, Int>> =
    Gen.list(Gen.choose(min.toInt(), max.toInt())
                 .map(Int::toChar)).map(::makeMap)
