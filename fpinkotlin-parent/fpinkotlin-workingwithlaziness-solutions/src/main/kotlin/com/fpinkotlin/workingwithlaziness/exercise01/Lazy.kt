package com.fpinkotlin.workingwithlaziness.exercise01


class Lazy<out A>(function: () -> A): () -> A {

    private val value: A by lazy(function)

    override operator fun invoke(): A = value
}
