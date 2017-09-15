package com.fpinkotlin.generators

import io.kotlintest.properties.Gen
import java.util.*


fun <T> list(gen: Gen<T>, minLength: Int = 0, maxLength: Int = 100): Gen<kotlin.collections.List<T>> =
  object : Gen<kotlin.collections.List<T>> {
    override fun generate(): kotlin.collections.List<T> {
      val length: Int = (maxLength - minLength).let {
        if (it <= 0)
          0
        else
          minLength + Random().nextInt(it)
      }
      val range: IntRange = 0 until length
      return range.map { gen.generate() }.toList()
    }
  }

fun <T>  forAll(generator: Gen<T>, fn: (a: T) -> Boolean, numTests: Int = 1_000) {
  for (k in 0..numTests) {
    val a = generator.generate()
    val passed = fn(a)
    if (!passed) {
      throw AssertionError("Property failed for\n$a")
    }
  }
}

class IntPairGenerator: Gen<Pair<Int, Int>> {
    override fun generate(): Pair<Int, Int> = Pair(Gen.int().generate(), Gen.int().generate())
}

class IntDoublePairGenerator: Gen<Pair<Int, Double>> {
    override fun generate(): Pair<Int, Double> = Pair(Gen.int().generate(), Gen.double().generate())
}

