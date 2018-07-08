package com.fpinkotlin.optionaldata.exercise12

import com.fpinkotlin.common.List
import com.fpinkotlin.common.sum


sealed class Option<out A> {

    abstract fun isEmpty(): Boolean

    abstract fun <B> map(f: (A) -> B): Option<B>

    fun <B> flatMap(f: (A) -> Option<B>): Option<B> = map(f).getOrElse(None)

    fun filter(p: (A) -> Boolean): Option<A> =
            flatMap { x -> if (p(x)) this else None }

    fun orElse(default: () -> Option<@UnsafeVariance A>): Option<A> = map { this }.getOrElse(default)

    fun getOrElse(default: @UnsafeVariance A): A = when (this) {
        is None -> default
        is Some -> value
    }

    fun getOrElse(default: () -> @UnsafeVariance A): A = when (this) {
        is None -> default()
        is Some -> value
    }

    internal object None: Option<Nothing>() {

        override fun <B> map(f: (Nothing) -> B): Option<B> = None

        override fun isEmpty() = true

        override fun toString(): String = "None"

        override fun equals(other: Any?): Boolean = other is None

        override fun hashCode(): Int = 0
    }

    internal data class Some<out A>(internal val value: A) : Option<A>() {

        override fun <B> map(f: (A) -> B): Option<B> = Some(f(value))

        override fun isEmpty() = false
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

val mean: (List<Double>) -> Option<Double> = { list ->
    when {
        list.isEmpty() -> Option()
        else -> Option(list.sum() / list.length())
    }
}

val variance: (List<Double>) -> Option<Double> = { list ->
    mean(list).flatMap { m ->
        mean(list.map { x ->
            Math.pow((x - m), 2.0)
        })
    }
}

fun mean(list: List<Double>): Option<Double> =
    when {
        list.isEmpty() -> Option()
        else -> Option(list.sum() / list.length())
    }


fun variance(list: List<Double>): Option<Double> =
    mean(list).flatMap { m ->
        mean(list.map { x ->
            Math.pow((x - m), 2.0)
        })
    }


val abs: (Double) -> Double = { d -> if (d > 0) d else -d }

val absO: (Option<Double>) -> Option<Double>  = lift { abs(it) }

fun <A, B> lift(f: (A) -> B): (Option<A>) -> Option<B> = {
    try {
        it.map(f)
    } catch (e: Exception) {
        Option()
    }
}

fun <A, B> hLift(f: (A) -> B): (A) -> Option<B> = {
    try {
        Option(it).map(f)
    } catch (e: Exception) {
        Option()
    }
}

val parseWithRadix: (Int) -> (String) -> Int = { radix -> { string -> Integer.parseInt(string, radix) } }

val parseHex: (String) -> Int = parseWithRadix(16)

fun abs(d: Double): Double = if (d > 0) d else -d

fun abs0(od: Option<Double>): Option<Double> = lift(::abs)(od)

val upperOption: (Option<String>) -> Option<String> = lift { it.toUpperCase() }

val upperOption_: (Option<String>) -> Option<String> = lift(String::toUpperCase)

fun <A, B, C> map2(oa: Option<A>,
                   ob: Option<B>,
                   f: (A) -> (B) -> C): Option<C> =
        oa.flatMap { a -> ob.map { b -> f(a)(b) } }

fun <A, B, C, D> map3(oa: Option<A>,
                      ob: Option<B>,
                      oc: Option<C>,
                      f: (A) -> (B) -> (C) -> D): Option<D> =
        oa.flatMap { a -> ob.flatMap { b -> oc.map { c -> f(a)(b)(c) } } }

fun <A, B> traverse(list: List<A> , f: (A) -> Option<B>): Option<List<B>> =
        list.foldRight(Option(List())) { x ->
            { y: Option<List<B>> ->
                map2(f(x), y) { a ->
                    { b: List<B> ->
                        b.cons(a)
                    }
                }
            }
        }

fun <A> sequence(list: List<Option<A>>): Option<List<A>> =
                            traverse(list) { x -> x }