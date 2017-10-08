package com.fpinkotlin.optionaldata.exercise06


sealed class Option<out A> {

    abstract fun isEmpty(): Boolean

    abstract fun <B> map(f: (A) -> B): Option<B>

    fun <B> flatMap(f: (A) -> Option<B>): Option<B> = map(f).getOrElse(None)

    fun filter(p: (A) -> Boolean): Option<A> =
            flatMap { x -> if (p(x)) this else None }

    internal object None: Option<Nothing>() {

        override fun <B> map(f: (Nothing) -> B): Option<B> = None

        override fun isEmpty() = true

        override fun toString(): String = "None"

        override fun equals(other: Any?): Boolean = other is None

        override fun hashCode(): Int = 0
    }

    internal class Some<out A>(internal val value: A) : Option<A>() {

        override fun <B> map(f: (A) -> B): Option<B> = Some(f(value))

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

        operator fun <A> invoke(a: A? = null): Option<A> = when (a) {
            null -> None
            else -> Some(a)
        }
    }
}

fun <A> Option<A>.getOrElse(default: A): A = Option.getOrElse(this, default)

fun <A> Option<A>.getOrElse(default: () -> A): A = Option.getOrElse(this, default)

fun <A> Option<A>.orElse(default: () -> Option<A>): Option<A> = map { this }.getOrElse(default)
