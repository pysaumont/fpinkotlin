package com.fpinkotlin.handlingerrors.exercise02

sealed class Either<E, out A> {

    abstract fun <B> map(f: (A) -> B): Either<E, B>

    abstract fun <B> flatMap(f: (A) -> Either<E, B>): Either<E, B>

    internal class Left<E, out A>(private val value: E): Either<E, A>() {

        override fun <B> flatMap(f: (A) -> Either<E, B>): Either<E, B> = TODO("Implement this function")

        override fun <B> map(f: (A) -> B): Either<E, B> = Left(value)

        override fun toString(): String = "Left($value)"
    }

    internal class Right<E, out A>(private val value: A) : Either<E, A>() {

        override fun <B> flatMap(f: (A) -> Either<E, B>): Either<E, B> = TODO("Implement this function")

        override fun <B> map(f: (A) -> B): Either<E, B> = Right(f(value))

        override fun toString(): String = "Right($value)"
    }

    companion object {

        fun <E, A> left(value: E): Either<E, A> = Left(value)

        fun <E, B> right(value: B): Either<E, B> = Right(value)
    }
}
