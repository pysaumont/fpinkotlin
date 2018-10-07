package com.fpinkotlin.workingwithlaziness.exercise09

import com.fpinkotlin.common.*
import com.fpinkotlin.common.List

class Lazy<out A>(function: () -> A): () -> A {

    private val value: A by lazy(function)

    override operator fun invoke(): A = value

    fun <B> map(f: (A) -> B): Lazy<B> = Lazy { f(value) }

    fun <B> flatMap(f: (A) -> Lazy<B>): Lazy<B> = Lazy { f(value)() }
}

fun <A, B, C> lift2(f: (A) -> (B) -> C): (Lazy<A>) ->  (Lazy<B>) -> Lazy<C> =
        { ls1 ->
            { ls2 ->
                Lazy { f(ls1())(ls2()) }
            }
        }

fun <A> sequence(lst: List<Lazy<A>>): Lazy<List<A>> = Lazy { lst.map { it() } }

fun <A> sequenceResult(lst: List<Lazy<A>>): Lazy<Result<List<A>>> =
        Lazy { sequence(lst.map { Result.of(it) }) }

fun <A> sequenceResult2(lst: List<Lazy<A>>): Lazy<Result<List<A>>> =
        Lazy { traverse(lst) { Result.of(it) } }

fun <A> sequenceResult3(list: List<Lazy<A>>): Lazy<Result<List<A>>> =
        Lazy {
            val p = { r: Result<List<A>> -> r.map{false}.getOrElse(true) }
            list.foldLeft(Result(List()), p) { y: Result<List<A>> ->
                { x: Lazy<A> ->
                    map2(Result.of(x), y) { a: A -> { b: List<A> -> b.cons(a) } }
                }
            }
        }