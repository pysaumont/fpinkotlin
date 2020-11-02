package com.fpinkotlin.workingwithlaziness.exercise02


class Lazy<out A>(function: () -> A): () -> A {

    private val value: A by lazy(function)

    override operator fun invoke(): A = value
}

fun constructMessage(greetings: Lazy<String>, name: Lazy<String>): Lazy<String> =
        Lazy { "${greetings()}, ${name()}!" }
