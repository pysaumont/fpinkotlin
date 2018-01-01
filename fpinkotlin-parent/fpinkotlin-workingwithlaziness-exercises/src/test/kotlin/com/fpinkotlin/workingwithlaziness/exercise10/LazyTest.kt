package com.fpinkotlin.workingwithlaziness.exercise10

import com.fpinkotlin.common.List
import com.fpinkotlin.common.Result
import com.fpinkotlin.generators.IntGenerator
import com.fpinkotlin.generators.forAll
import io.kotlintest.specs.StringSpec

class LazyTest: StringSpec() {

    init {

        "Lazy" {
            forAll(IntGenerator(), { a ->
                var name1Calls = 0
                val name1: Lazy<String> = Lazy {
                    name1Calls++
                    "Mickey"
                }
                var name2Calls = 0
                val name2: Lazy<String> = Lazy {
                    name2Calls++
                    "Donald"
                }
                var name3Calls = 0
                val name3: Lazy<String> = Lazy {
                    name3Calls++
                    "Goofy"
                }
                var name4Calls = 0
                val name4 = Lazy {
                    name4Calls++
                    throw IllegalStateException("Exception while evaluating name4")
                }

                val list1 = sequenceResult(List(name1, name2, name3))
                val list2 = sequenceResult(List(name1, name2, name3, name4))
                val condition = a % 2 == 0
                var result1 = 0
                var result2 = 0
                val effect1: (Result<List<String>>) -> Unit = { result1++ }
                val effect2: () -> Unit = { result2++ }
                list1.forEach(condition, effect1, effect2)
                list1.forEach(condition, effect1, effect2)
                list2.forEach(condition, effect1, effect2)
                list2.forEach(condition, effect1, effect2)
                list1.forEach(condition, effect1)
                list1.forEach(condition, ifFalse = effect1)
                list1.forEach(condition, effect2, effect1)
                list2.forEach(condition, effect1)
                list2.forEach(condition, ifFalse = effect1)
                list2.forEach(condition, effect2, effect1)
                list1.forEach(condition, effect1, effect1)
                list1.forEach(condition, effect1, effect1)
                list2.forEach(condition, effect1, effect1)
                list2.forEach(condition, effect1, effect1)
                (condition &&
                        result1 == 10 &&
                        result2 == 2) ||
                        (!condition &&
                                result1 == 8 &&
                                result2 == 4)
            })
        }
    }
}