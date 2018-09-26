package com.fpinkotlin.workingwithlaziness.exercise24

import com.fpinkotlin.common.List
import com.fpinkotlin.common.Result
import com.fpinkotlin.common.sequence

class Lazy<out A>(function: () -> A): () -> A {

    private val value: A by lazy(function)

    override operator fun invoke(): A = value

    fun <B> map(f: (A) -> B): Lazy<B> = Lazy { f(value) }

    fun <B> flatMap(f: (A) -> Lazy<B>): Lazy<B> = Lazy { f(value)() }

    fun forEach(condition: Boolean, ifTrue: (A) -> Unit, ifFalse: () -> Unit = {}) =
            if (condition)
                ifTrue(value)
            else
                ifFalse()

    fun forEach(condition: Boolean, ifTrue: () -> Unit = {}, ifFalse: (A) -> Unit) =
            if (condition)
                ifTrue()
            else
                ifFalse(value)

    fun forEach(condition: Boolean, ifTrue: (A) -> Unit, ifFalse: (A) -> Unit) =
            if (condition)
                ifTrue(value)
            else
                ifFalse(value)
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
