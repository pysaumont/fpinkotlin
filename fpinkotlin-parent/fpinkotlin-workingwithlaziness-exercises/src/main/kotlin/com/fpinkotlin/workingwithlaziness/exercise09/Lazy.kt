package com.fpinkotlin.workingwithlaziness.exercise09

import com.fpinkotlin.common.List
import com.fpinkotlin.common.Result
import com.fpinkotlin.common.traverse

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
        Lazy { traverse(lst) { it: Lazy<A> -> val f: Lazy<A> = it
            val of: Result<A> = Result.of(f)
            of
        } }
