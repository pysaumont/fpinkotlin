package com.mydomain.mymultipleproject.common.example06

import io.kotlintest.properties.Gen

//class CharGenerator(val p: (Char) -> Boolean) : Gen<Char> {
//
//    override fun constants(): Iterable<Char> = listOf()
//
//    override fun random(): Sequence<Char> =
//            Random().let { random ->
//                tailrec fun nextChar(): Char {
//                    val char = random.nextInt(256).toChar()
//                    return when {
//                        p(char) -> char
//                        else  -> nextChar()
//                    }
//                }
//                generateSequence {
//                    nextChar()
//                }
//            }
//}

fun charGenerator(p: (Char) -> Boolean): Gen<Char> = Gen.choose(0, 255).map(Int::toChar).filter(p)