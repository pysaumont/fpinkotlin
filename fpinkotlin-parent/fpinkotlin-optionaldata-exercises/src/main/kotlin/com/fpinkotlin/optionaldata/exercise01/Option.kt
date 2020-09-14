package com.fpinkotlin.optionaldata.exercise01


sealed class Option<out A> {

    abstract fun isEmpty(): Boolean

    fun getOrElse(default: @UnsafeVariance A): A = when (this) {
        is None -> default
        is Some -> value
    }

    internal object None: Option<Nothing>() {

        override fun isEmpty() = true

        override fun toString(): String = "None"

        override fun equals(other: Any?): Boolean = other is None

        override fun hashCode(): Int = 0
    }

    internal data class Some<out A>(internal val value: A) : Option<A>() {

        override fun isEmpty() = false
    }

    companion object {

        operator fun <A> invoke(a: A? = null): Option<A> = when (a) {
            null -> None
            else -> Some(a)
        }
    }
}

