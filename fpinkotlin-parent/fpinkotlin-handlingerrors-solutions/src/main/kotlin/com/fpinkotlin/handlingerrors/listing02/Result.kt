package com.fpinkotlin.handlingerrors.listing02

import java.io.Serializable


sealed class Result<out A>: Serializable { // <1>

    internal class Failure<out A>(internal val exception: RuntimeException): Result<A>() { // <2>

        override fun toString(): String = "Failure(${exception.message})"
    }

    internal class Success<out A>(internal val value: A) : Result<A>() {

        override fun toString(): String = "Success($value)"
    }

    companion object {

        operator fun <A> invoke(a: A? = null): Result<A> = when (a) { // <3>
            null -> Failure(NullPointerException())
            else -> Success(a)
        }

        fun <A> failure(message: String): Result<A> = Failure(IllegalStateException(message)) // <4>

        fun <A> failure(exception: RuntimeException): Result<A> = Failure(exception) // <5>

        fun <A> failure(exception: Exception): Result<A> = Failure(IllegalStateException(exception)) // <6>
    }
}
