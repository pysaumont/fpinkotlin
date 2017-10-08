package com.fpinkotlin.optionaldata.listing01

sealed class Option<out A> {

    abstract fun isEmpty(): Boolean

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

        operator fun <A> invoke(a: A? = null): Option<A> = when (a) {
            null -> None
            else -> Some(a)
        }
    }
}
