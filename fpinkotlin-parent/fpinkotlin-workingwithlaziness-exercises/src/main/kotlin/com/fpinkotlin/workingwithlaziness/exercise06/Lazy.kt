package com.fpinkotlin.workingwithlaziness.exercise06


class Lazy<out A>(function: () -> A): () -> A {

    private val value: A by lazy(function)

    operator override fun invoke(): A = value

    fun <B> map(f: (A) -> B): Lazy<B> = Lazy{ f(value) }

    companion object {

        operator fun <A> invoke(function: () -> A): Lazy<A> = Lazy(function)

    }
}

fun <A, B, C> lift2(f: (A) -> (B) -> C): (Lazy<A>) ->  (Lazy<B>) -> Lazy<C> =
        { ls1 ->
            { ls2 ->
                Lazy { f(ls1())(ls2()) }
            }
        }
