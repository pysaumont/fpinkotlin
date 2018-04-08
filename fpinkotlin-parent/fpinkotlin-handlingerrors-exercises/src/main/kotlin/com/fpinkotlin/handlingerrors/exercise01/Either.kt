package com.fpinkotlin.handlingerrors.exercise01

sealed class Either<out E, out A> {

    abstract fun <B> map(f: (A) -> B): Either<E, B>

    internal class Left<out E, out A>(private val value: E): Either<E, A>() {

        override fun <B> map(f: (A) -> B): Either<E, B> = TODO("map")

        override fun toString(): String = "Left($value)"
    }

    internal class Right<out E, out A>(private val value: A) : Either<E, A>() {

        override fun <B> map(f: (A) -> B): Either<E, B> = TODO("map")

        override fun toString(): String = "Right($value)"
    }

    companion object {

        fun <E, A> left(value: E): Either<E, A> = Left(value)

        fun <E, B> right(value: B): Either<E, B> = Right(value)
    }
}
