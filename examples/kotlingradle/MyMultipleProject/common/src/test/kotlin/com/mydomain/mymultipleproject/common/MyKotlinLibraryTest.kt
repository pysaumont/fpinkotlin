package com.mydomain.mymultipleproject.common

import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class MyKotlinLibraryTest: StringSpec() {

    class MyKotlinLibraryTest: StringSpec() {

        init {
            "isMaxMultiple" {
                forAll { multiple: Int, max:Int, value: Int ->
                    isMaxMultiple(multiple)(max, value).let { result ->
                        result >= value
                                && result % multiple == 0 || result == max
                                && ((result % multiple == 0 && result >= value) || result % multiple != 0)
                    }
                }
            }
        }
    }
}

