package com.fpinkotlin.optionaldata.exercise03


sealed class Option<out A> {

    abstract fun isEmpty(): Boolean

    fun <B> map(f: (A) -> B): Option<B> = TODO("map")

    internal object None: Option<Nothing>() {

        override fun isEmpty() = true

        override fun toString(): String = "None"

        override fun equals(other: Any?): Boolean = other is None

        override fun hashCode(): Int = 0
    }

    internal class Some<out A>(internal val value: A) : Option<A>() {

        override fun isEmpty() = false

        override fun toString(): String = "Some($value)"

        override fun equals(other: Any?): Boolean = when (other) {
            is Some<*> -> value == other.value
            else -> false
        }

        override fun hashCode(): Int = value?.hashCode() ?: 0
    }

    companion object {

        fun <A> getOrElse(option: Option<A>, default: A): A = when (option) {
            is None -> default
            is Some -> option.value
        }

        fun <A> getOrElse(option: Option<A>, default: () -> A): A = when (option) {
            is None -> default()
            is Some -> option.value
        }

        operator fun <A> invoke(a: A?): Option<A> = when (a) {
            null -> None
            else -> Some(a)
        }
    }
}

fun <A> Option<A>.getOrElse(default: A): A = Option.getOrElse(this, default)

fun <A> Option<A>.getOrElse(default: () -> A): A = Option.getOrElse(this, default)

