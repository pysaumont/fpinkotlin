package com.fpinkotlin.handlingerrors.exercise04

import java.io.Serializable


sealed class Result<out A> : Serializable {

    abstract fun <B> map(f: (A) -> B): Result<B>

    abstract fun <B> flatMap(f: (A) -> Result<B>): Result<B>

    fun getOrElse(defaultValue: @UnsafeVariance A): A = when (this) {
        is Failure -> defaultValue
        is Success -> value
    }

    fun getOrElse(defaultValue: () -> @UnsafeVariance A): A = when (this) {
        is Failure -> defaultValue()
        is Success -> value
    }

    fun orElse(defaultValue: () -> Result<@UnsafeVariance A>): Result<A> = when (this) {
        is Failure -> try {
            defaultValue()
        } catch (ex: RuntimeException) {
            failure<A>(ex)
        } catch (ex: Exception) {
            failure<A>(ex)
        }
        is Success -> Success(value)
    }

    internal class Failure<out A>(private val exception: RuntimeException) : Result<A>() {

        override fun <B> map(f: (A) -> B): Result<B> = Failure(exception)

        override fun <B> flatMap(f: (A) -> Result<B>): Result<B> = Failure(exception)

        override fun toString(): String = "Failure(${exception.message})"
    }

    internal class Success<out A>(internal val value: A) : Result<A>() {

        override fun <B> map(f: (A) -> B): Result<B> = try {
            Success(f(value))
        } catch (ex: RuntimeException) {
            failure(ex)
        } catch (ex: Exception) {
            failure(ex)
        }

        override fun <B> flatMap(f: (A) -> Result<B>): Result<B> = try {
            f(value)
        } catch (ex: RuntimeException) {
            failure(ex)
        } catch (ex: Exception) {
            failure(ex)
        }

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

