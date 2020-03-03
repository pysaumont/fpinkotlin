package  com.fpinkotlin.actors.listing15

import java.io.Serializable


sealed class Result<out A>: Serializable {

    abstract fun mapEmpty(): Result<Any>

    abstract fun <B> map(f: (A) -> B): Result<B>

    abstract fun <B> flatMap(f: (A) -> Result<B>): Result<B>

    abstract fun isEmpty(): Boolean

    fun getOrElse(defaultValue: @UnsafeVariance A): A = when (this) {
        is Success -> this.value
        else                                                            -> defaultValue
    }

    fun getOrElse(defaultValue: () -> @UnsafeVariance A): A = when (this) {
        is Success -> this.value
        else                                                            -> defaultValue()
    }

    fun orElse(defaultValue: () -> Result<@UnsafeVariance A>): Result<A> =
            when (this) {
                is Success -> this
                else                                                            -> try {
                    defaultValue()
                } catch (e: RuntimeException) {
                    failure<A>(e)
                } catch (e: Exception) {
                    failure<A>(RuntimeException(e))
                }
            }

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

        override fun mapEmpty(): Result<Any> = Result(Any())

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

        override fun mapEmpty(): Result<Any> = failure(
            this.exception)

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

        override fun mapEmpty(): Result<Nothing> = failure(
            "Not empty")

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

        fun <T> of(predicate: (T) -> Boolean,
                   value: T,
                   message: String): Result<T> =
             try {
                if (predicate(value))
                    Result(value)
                else
                    failure("Assertion failed for value $value with message: $message")
            } catch (e: Exception) {
                 failure(IllegalStateException("Exception while validating $value", e))
            }
    }
}

fun <A, B> lift1(f: (A) -> B): (Result<A>) -> Result<B> = { it.map(f) }

fun <A, B, C> lift2(f: (A) -> (B) -> C): (Result<A>) -> (Result<B>) -> Result<C> =
    { a ->
        { b ->
            a.map(f).flatMap { b.map(it) }
        }
    }

fun <A, B, C, D> lift3(f: (A) -> (B) -> (C) -> D): (Result<A>) -> (Result<B>) -> (Result<C>) -> Result<D> =
    { a ->
        { b ->
            { c ->
                a.map(f).flatMap { b.map(it) }.flatMap { c.map(it) }
            }
        }
    }

fun <A, B, C> map2(a: Result<A>,
                   b: Result<B>,
                   f: (A) -> (B) -> C): Result<C> = lift2(f)(a)(b)
