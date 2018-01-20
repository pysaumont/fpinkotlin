package com.fpinkotlin.advancedlisthandling.exercise23


sealed class Option<out A> {

    abstract fun isEmpty(): Boolean

    abstract fun <B> map(f: (A) -> B): Option<B>

    fun <B> flatMap(f: (A) -> Option<B>): Option<B> = map(f).getOrElse(
        None)

    fun filter(p: (A) -> Boolean): Option<A> =
            flatMap { x -> if (p(x)) this else None }

    internal object None: Option<Nothing>() {

        override fun <B> map(f: (Nothing) -> B): Option<B> = None

        override fun isEmpty() = true

        override fun toString(): String = "None"
    }

    internal class Some<out A>(internal val value: A) : Option<A>() {

        override fun <B> map(f: (A) -> B): Option<B> = Some(
            f(value))

        override fun isEmpty() = false

        override fun toString(): String = "Some($value)"

        override fun equals(other: Any?): Boolean = when (other) {
            is Some<*> -> value == other.value
            else                                                             -> false
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

fun <A> Option<A>.getOrElse(default: A): A = Option.getOrElse(
    this, default)

fun <A> Option<A>.getOrElse(default: () -> A): A = Option.getOrElse(
    this, default)

fun <A> Option<A>.orElse(default: () -> Option<A>): Option<A> = map { this }.getOrElse(default)

val abs: (Double) -> Double = { d -> if (d > 0) d else -d }

val absO: (Option<Double>) -> Option<Double> = lift {
    abs(it)
}

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

fun abs0(od: Option<Double>): Option<Double> = lift(
    ::abs)(od)

val upperOption: (Option<String>) -> Option<String> = lift { it.toUpperCase() }

val upperOption_: (Option<String>) -> Option<String> = lift(
    String::toUpperCase)

fun <A, B, C> map2(oa: Option<A>,
                   ob: Option<B>,
                   f: (A) -> (B) -> C): Option<C> =
        oa.flatMap { a -> ob.map { b -> f(a)(b) } }

fun <A, B, C, D> map3(oa: Option<A>,
                      ob: Option<B>,
                      oc: Option<C>,
                      f: (A) -> (B) -> (C) -> D): Option<D> =
        oa.flatMap { a -> ob.flatMap { b -> oc.map { c -> f(a)(b)(c) } } }

fun <A, B> traverse_(list: List<A> , f: (A) -> Option<B>): Option<List<B>> =
        list.foldRight(Option(List())) { x ->
            { y: Option<List<B>> ->
                map2(f(x), y) { a ->
                    { b: List<B> ->
                        b.cons(a)
                    }
                }
            }
        }

fun <A> sequence(list: List<Option<A>>): Option<List<A>> = traverse_(list, { x: Option<A> -> x })