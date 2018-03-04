package com.fpinkotlin.effects.exercise02

import com.fpinkotlin.common.Result


data class Person (val id: Int, val firstName: String, val lastName: String)

fun person(input: Input): Result<Pair<Person, Input>> = TODO("person")