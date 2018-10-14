package com.fpinkotlin.functions.exercise09


//import io.kotlintest.properties.Gen
import com.fpinkotlin.generators.IntDoublePairGenerator
import com.fpinkotlin.generators.forAll
import io.kotlintest.specs.StringSpec

class FunctionsTest: StringSpec() {

    init {

        "curried" {
            forAll(IntDoublePairGenerator(), { (a, b) ->
                TODO("Implement function currying, then remove this line an uncomment the following lines as well as the Gen import")
                // val c = Gen.string().random().first()
                // val d = Gen.bool().random().first()
                // curried<Int, Double, String, Boolean>()(a)(b)(c)(d) == "$a, $b, $c, $d"
            })
        }
    }
}
