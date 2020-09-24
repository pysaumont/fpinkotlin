package com.fpinkotlin.handlingerrors.exercise03

sealed class Either<E, out A> {

    abstract fun <B> map(f: (A) -> B): Either<E, B>

    abstract fun <B> flatMap(f: (A) -> Either<E, B>): Either<E, B>

    fun getOrElse(defaultValue: () -> @UnsafeVariance A): A = when(this) {
        is Left -> defaultValue()
        is Right -> value
    }

    fun orElse(defaultValue: () -> Either<E, @UnsafeVariance A>): Either<E, A> =
        map { this }.getOrElse(defaultValue)

    internal class Left<E, out A>(private val value: E): Either<E, A>() {

        override fun <B> flatMap(f: (A) -> Either<E, B>): Either<E, B> = Left(value)

        override fun <B> map(f: (A) -> B): Either<E, B> = Left(value)

        override fun toString(): String = "Left($value)"
    }

    internal class Right<E, out A>(internal val value: A) : Either<E, A>() {

        override fun <B> flatMap(f: (A) -> Either<E, B>): Either<E, B> = f(value)

        override fun <B> map(f: (A) -> B): Either<E, B> = Right(f(value))

        override fun toString(): String = "Right($value)"
    }

    companion object {

        fun <E, A> left(value: E): Either<E, A> = Left(value)

        fun <E, B> right(value: B): Either<E, B> = Right(value)
    }
}
