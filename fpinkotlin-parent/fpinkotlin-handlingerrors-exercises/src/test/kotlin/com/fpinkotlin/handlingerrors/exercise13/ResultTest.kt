package com.fpinkotlin.handlingerrors.exercise13


import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class ResultTest: StringSpec() {

    init {

        "lift2" {
            val f: (Int) -> (Int) -> Int = { a ->
                { b ->
                    if (a > b) throw RuntimeException("A is to big") else b - a
                }
            }
            forAll { x: Int, y: Int ->
                lift2(f)(Result(x))(Result(y)).toString() ==
                    if (x <= y) Result(f(x)(y)).toString()
                    else Result.failure<Int>("A is to big").toString()
            }
        }

        "lift3" {
            val f: (Int) -> (Int) -> (Int) -> Int = { a ->
                { b ->
                    { c ->
                        if (a > b) throw RuntimeException("A is to big") else b - a + c
                    }
                }
            }
            forAll { x: Int, y: Int, z: Int ->
                lift3(f)(Result(x))(Result(y))(Result(z)).toString() ==
                    if (x <= y) Result(f(x)(y)(z)).toString()
                    else Result.failure<Int>("A is to big").toString()
            }
        }
    }
}