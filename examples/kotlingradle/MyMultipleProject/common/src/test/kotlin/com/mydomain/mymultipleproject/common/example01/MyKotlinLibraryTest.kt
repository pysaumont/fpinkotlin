package com.mydomain.mymultipleproject.common.example01

import com.mydomain.mymultipleproject.common.isMaxMultiple
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class MyKotlinLibraryTest: StringSpec() {

    init { // Should fail
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
