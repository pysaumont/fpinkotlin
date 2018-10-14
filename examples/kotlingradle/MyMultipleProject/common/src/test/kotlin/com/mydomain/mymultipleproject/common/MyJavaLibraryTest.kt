package com.mydomain.mymultipleproject.common;

import com.mydomain.mymultipleproject.common.MyJavaLibrary.triple
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class MyJavaLibraryTest : StringSpec() {

    init {
        "triple" {
            forAll(10000) { n: Int -> n + n + n == triple(n) }
        }
    }
}
