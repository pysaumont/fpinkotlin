package com.fpinkotlin.workingwithlaziness.exercise01


class Lazy<out A>(function: () -> A): () -> A {

    // Add implementation

    override fun invoke(): A = TODO("not implemented")


    companion object {

        // Add implementation
    }
}