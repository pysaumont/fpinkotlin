package com.fpinkotlin.handlingerrors.exercise12


import com.fpinkotlin.generators.forAll
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec

class ResultTest: StringSpec() {

    init {

        "lift" {
            val f: (Int) -> Int = { if (it % 5 == 0) throw RuntimeException("Should not be seen") else it }
            forAll(Gen.int(), { z ->
                lift(f)(Result(z)).toString() ==
                    if (z % 5 != 0) Result(f(z)).toString()
                    else Result.failure<Int>("Should not be seen").toString()
            })
        }
    }
}