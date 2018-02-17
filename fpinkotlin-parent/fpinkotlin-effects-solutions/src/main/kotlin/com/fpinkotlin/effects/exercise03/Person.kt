package com.fpinkotlin.effects.exercise03

import com.fpinkotlin.common.Result


data class Person (val id: Int, val firstName: String, val lastName: String)

fun person(input: Input): Result<Pair<Person, Input>> = input.readInt("Enter ID:")
    .flatMap { id ->
        id.second.readString("Enter first name:").flatMap { firstName ->
            firstName.second.readString("Enter last name:").map { lastName ->
                Pair(Person(id.first, firstName.first, lastName.first), lastName.second)
            }
        }
    }
