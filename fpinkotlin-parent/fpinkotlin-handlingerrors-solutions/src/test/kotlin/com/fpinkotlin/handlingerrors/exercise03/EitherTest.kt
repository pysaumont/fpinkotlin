package com.fpinkotlin.handlingerrors.exercise03

import com.fpinkotlin.generators.forAll
import io.kotlintest.properties.Gen
import io.kotlintest.specs.StringSpec
import java.util.*

class EitherTest: StringSpec() {

    private val random = Random()

    init {
        "getOrElse" {
            val x = random.nextInt()
            forAll(Gen.int(), { z ->
                Either.right<String, Int>(z).getOrElse { x } == z &&
                        Either.left<String, Int>("Error").getOrElse { x } == x
            })
        }

        "orElse" {
            val x: Either<String, Int> = Either.right(random.nextInt())
            forAll(Gen.int(), { z ->
                Either.right<String, Int>(z).orElse { x }.toString() == Either.right<String, Int>(z).toString() &&
                        Either.left<String, Int>("Error").orElse { x } == x
            })
        }
    }
}
