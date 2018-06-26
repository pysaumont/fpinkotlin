package com.fpinkotlin.workingwithlaziness.exercise03


class Lazy<out A>(function: () -> A): () -> A {

    private val value: A by lazy(function)

    override operator fun invoke(): A = value

    companion object {

        operator fun <A> invoke(function: () -> A): Lazy<A> = Lazy(function)
    }
}

val constructMessage: (Lazy<String>) ->  (Lazy<String>) -> Lazy<String> =
        { greetings ->
            { name ->
                Lazy { "${greetings()}, ${name()}!" }
            }
        }
