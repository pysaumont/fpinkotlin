package com.fpinkotlin.handlingerrors.exercise04

import java.io.Serializable


sealed class Result<out A>: Serializable {

    abstract fun <B> map(f: (A) -> B): Result<B>

    abstract fun <B> flatMap(f: (A) ->  Result<B>): Result<B>

    internal class Failure<out A>(private val exception: RuntimeException): Result<A>() {

        override fun <B> map(f: (A) -> B): Result<B> = TODO("Implement this function")

        override fun <B> flatMap(f: (A) -> Result<B>): Result<B> = TODO("Implement this function")

        override fun toString(): String = "Failure(${exception.message})"
    }

    internal class Success<out A>(internal val value: A) : Result<A>() {

        override fun <B> map(f: (A) -> B): Result<B> = TODO("Implement this function")

        override fun <B> flatMap(f: (A) -> Result<B>): Result<B> = TODO("Implement this function")

        override fun toString(): String = "Success($value)"
    }

    companion object {

        operator fun <A> invoke(a: A? = null): Result<A> = when (a) {
            null -> Failure(NullPointerException())
            else -> Success(a)
        }

        fun <A> failure(message: String): Result<A> = Failure(IllegalStateException(message))

        fun <A> failure(exception: RuntimeException): Result<A> = Failure(exception)

        fun <A> failure(exception: Exception): Result<A> = Failure(IllegalStateException(exception))
    }
}

fun <A> Result<A>.getOrElse(defaultValue: A): A = TODO("Implement this function")

fun <A> Result<A>.getOrElse(defaultValue: () -> A): A = TODO("Implement this function")

fun <A> Result<A>.orElse(defaultValue: () -> Result<A>): Result<A> = TODO("Implement this function")
