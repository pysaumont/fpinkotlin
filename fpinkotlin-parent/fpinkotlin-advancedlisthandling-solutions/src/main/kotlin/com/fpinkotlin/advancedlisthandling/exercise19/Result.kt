package com.fpinkotlin.advancedlisthandling.exercise19

import java.io.Serializable


sealed class Result<out A>: Serializable {

    abstract fun <B> map(f: (A) -> B): Result<B>

    abstract fun <B> flatMap(f: (A) -> Result<B>): Result<B>

    abstract fun isEmpty(): Boolean

    fun filter(p: (A) -> Boolean): Result<A> = flatMap {
        if (p(it))
            this
        else
            failure("Condition not matched")
    }

    fun filter(message: String, p: (A) -> Boolean): Result<A> = flatMap {
        if (p(it))
            this
        else
            failure(message)
    }

    fun exists(p: (A) -> Boolean): Boolean = map(p).getOrElse(false)

    abstract fun mapFailure(message: String): Result<A>

    abstract fun forEach(onSuccess: (A) -> Unit = {},
                         onFailure: (RuntimeException) -> Unit = {},
                         onEmpty: () -> Unit = {})

    internal object Empty: Result<Nothing>() {

        override fun isEmpty(): Boolean = true

        override fun forEach(onSuccess: (Nothing) -> Unit,
                             onFailure: (RuntimeException) -> Unit,
                             onEmpty: () -> Unit) {
            onEmpty()
        }

        override fun <B> map(f: (Nothing) -> B): Result<B> = Empty

        override fun <B> flatMap(f: (Nothing) -> Result<B>): Result<B> = Empty

        override fun mapFailure(message: String): Result<Nothing> = this

        override fun toString(): String = "Empty"
    }

    internal class Failure<out A>(internal val exception: RuntimeException): Result<A>() {

        override fun isEmpty(): Boolean = false

        override fun forEach(onSuccess: (A) -> Unit,
                             onFailure: (RuntimeException) -> Unit,
                             onEmpty: () -> Unit) {
            onFailure(exception)
        }

        override fun <B> map(f: (A) -> B): Result<B> = Failure(
            exception)

        override fun <B> flatMap(f: (A) -> Result<B>): Result<B> = Failure(
            exception)

        override fun mapFailure(message: String): Result<A> = Failure(
            RuntimeException(message, exception))

        override fun toString(): String = "Failure(${exception.message})"
    }

    internal class Success<out A>(internal val value: A): Result<A>() {

        override fun isEmpty(): Boolean = false

        override fun forEach(onSuccess: (A) -> Unit,
                             onFailure: (RuntimeException) -> Unit,
                             onEmpty: () -> Unit) {
            onSuccess(value)
        }

        override fun <B> map(f: (A) -> B): Result<B> = try {
            Success(f(value))
        } catch (e: RuntimeException) {
            Failure(e)
        } catch (e: Exception) {
            Failure(RuntimeException(e))
        }

        override fun <B> flatMap(f: (A) -> Result<B>): Result<B> = try {
            f(value)
        } catch (e: RuntimeException) {
            Failure(e)
        } catch (e: Exception) {
            Failure(RuntimeException(e))
        }

        override fun mapFailure(message: String): Result<A> = this

        override fun toString(): String = "Success($value)"
    }

    companion object {

        operator fun <A> invoke(a: A? = null): Result<A> = when (a) {
            null -> Failure(NullPointerException())
            else -> Success(a)
        }

        operator fun <A> invoke(): Result<A> = Empty

        fun <A> failure(message: String): Result<A> = Failure(
            IllegalStateException(message))

        fun <A> failure(exception: RuntimeException): Result<A> = Failure(
            exception)

        fun <A> failure(exception: Exception): Result<A> = Failure(
            IllegalStateException(exception))

        operator fun <A> invoke(a: A? = null, message: String): Result<A> = when (a) {
            null -> Failure(NullPointerException(message))
            else -> Success(a)
        }

        operator fun <A> invoke(a: A? = null, p: (A) -> Boolean): Result<A> = when (a) {
            null -> Failure(NullPointerException())
            else -> when {
                p(a) -> Success(a)
                else -> Empty
            }
        }

        operator fun <A> invoke(a: A? = null, message: String, p: (A) -> Boolean): Result<A> = when (a) {
            null -> Failure(NullPointerException())
            else -> when {
                p(a) -> Success(a)
                else -> Failure(
                    IllegalArgumentException("Argument $a does not match condition: $message"))
            }
        }

        fun <A> of(f: () -> A): Result<A> =
            try {
                Result(f())
            } catch (e: RuntimeException) {
                failure(e)
            } catch (e: Exception) {
                failure(e)
            }
    }
}

fun <A> Result<A>.getOrElse(defaultValue: A): A = when (this) {
    is Result.Success -> this.value
    else              -> defaultValue
}

fun <A> Result<A>.getOrElse(defaultValue: () -> A): A = when (this) {
    is Result.Success -> this.value
    else              -> defaultValue()
}

fun <A> Result<A>.orElse(defaultValue: () -> Result<A>): Result<A> = map { this }.let {
    when (it) {
        is Result.Success -> it.value
        else              -> try {
            defaultValue()
        } catch (e: RuntimeException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(RuntimeException(e))
        }
    }
}

fun <A, B> lift1(f: (A) -> B): (Result<A>) -> Result<B> =
    { a ->
        try {
            a.map(f)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

fun <A, B, C> lift2(f: (A) -> (B) -> C): (Result<A>) -> (Result<B>) -> Result<C> =
    { a ->
        { b ->
            try {
                a.map(f).flatMap { b.map(it) }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

fun <A, B, C, D> lift3(f: (A) -> (B) -> (C) -> D): (Result<A>) -> (Result<B>) -> (Result<C>) -> Result<D> =
    { a ->
        { b ->
            { c ->
                try {
                    a.map(f).flatMap { b.map(it) }.flatMap { c.map(it) }
                } catch (e: Exception) {
                    Result.failure(e)
                }

            }
        }
    }

fun <A, B, C> map2(a: Result<A>,
                   b: Result<B>,
                   f: (A) -> (B) -> C): Result<C> = lift2(f)(a)(b)
