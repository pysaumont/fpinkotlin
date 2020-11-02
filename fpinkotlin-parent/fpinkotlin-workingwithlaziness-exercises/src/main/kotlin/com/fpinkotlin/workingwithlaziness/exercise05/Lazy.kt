package com.fpinkotlin.workingwithlaziness.exercise05


class Lazy<out A>(function: () -> A): () -> A {

    private val value: A by lazy(function)

    override operator fun invoke(): A = value
}

fun <A, B, C> lift2(f: (A) -> (B) -> C): (Lazy<A>) -> (Lazy<B>) -> Lazy<C> =
        { a: Lazy<A> ->
            { b: Lazy<B> ->
                Lazy { f(a())(b()) }
            }
        }

val consMessage: (String) -> (String) -> String =
    { greetings ->
        { name ->
            "$greetings, $name!"
        }
    }
