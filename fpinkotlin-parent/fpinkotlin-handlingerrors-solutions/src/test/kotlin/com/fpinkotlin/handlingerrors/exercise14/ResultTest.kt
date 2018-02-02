package com.fpinkotlin.handlingerrors.exercise14


import com.fpinkotlin.generators.IntPairGenerator
import com.fpinkotlin.generators.forAll
import com.fpinkotlin.handlingerrors.exercise14.Result.Companion.failure
import io.kotlintest.specs.StringSpec

class ResultTest: StringSpec() {

    init {

        "map2" {
            val p: (Int) -> (Int) -> Int = { a -> { b -> if (a > b) throw RuntimeException("a is too big") else b - a } }
            forAll(IntPairGenerator(), { (x,y) ->
                map2(Result(x), Result(y), p).toString() ==
                    if (x <= y) Result(p(x)(y)).toString() else failure<Int>("a is too big").toString()
            })
        }
    }
}
