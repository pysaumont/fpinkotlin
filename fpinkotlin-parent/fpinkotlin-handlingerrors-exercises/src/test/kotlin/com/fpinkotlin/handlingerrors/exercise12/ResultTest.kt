package com.fpinkotlin.handlingerrors.exercise12


import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class ResultTest: StringSpec() {

    init {

        "lift" {
            val f: (Int) -> Int = { if (it % 5 == 0) throw RuntimeException("Should not be seen") else it }
            forAll { z: Int ->
                lift(f)(Result(z)).toString() ==
                    if (z % 5 != 0) Result(f(z)).toString()
                    else Result.failure<Int>("Should not be seen").toString()
            }
        }
    }
}