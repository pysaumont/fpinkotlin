package com.mydomain.mysimpleproject

fun main(args: Array<String>) {
    println(MyClass.getMessage(Lang.GERMAN))
}

enum class Lang { GERMAN, FRENCH, ENGLISH, SPANISH }
