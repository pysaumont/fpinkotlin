package com.mydomain.mymultipleproject.client.main

import com.mydomain.mymultipleproject.common.MyJavaLibrary
import com.mydomain.mymultipleproject.common.quadruple

fun main(args: Array<String>) {
    println("triple(4) = " + MyJavaLibrary.triple(4))
    println("quadruple(3) = " + quadruple(3))
}