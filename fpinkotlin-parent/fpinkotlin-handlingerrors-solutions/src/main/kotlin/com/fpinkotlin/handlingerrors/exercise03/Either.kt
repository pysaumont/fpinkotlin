package com.fpinkotlin.handlingerrors.exercise03

sealed class Either<E, out A> {

    abstract fun <B> map(f: (A) -> B): Either<E, B>

    abstract fun <B> flatMap(f: (A) -> Either<E, B>): Either<E, B>

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

fun <E, A> Either<E, A>.getOrElse(defaultValue: () -> A): A = when (this) {
    is Either.Right -> this.value
    is Either.Left  -> defaultValue()
}

fun <E, A> Either<E, A>.orElse(defaultValue: () -> Either<E, A>): Either<E, A> =
        map { this }.getOrElse(defaultValue)

fun <E, A> getOrElse_(that: Either<E, A>, defaultValue: () -> A): A = when (that) {
    is Either.Right -> that.value
    is Either.Left  -> defaultValue()
}

fun <E, A> orElse_(that: Either<E, A>, defaultValue: () -> Either<E, A>): Either<E, A> =
        that.map { that }.getOrElse(defaultValue)

