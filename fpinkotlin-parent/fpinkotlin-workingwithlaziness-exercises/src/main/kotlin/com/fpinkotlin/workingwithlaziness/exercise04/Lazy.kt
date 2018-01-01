package com.fpinkotlin.workingwithlaziness.exercise04


class Lazy<out A>(function: () -> A): () -> A {

    private val value: A by lazy(function)

    operator override fun invoke(): A = value

    companion object {

        operator fun <A> invoke(function: () -> A): Lazy<A> = Lazy(function)

        val lift2: ((String) -> (String) -> String) -> (Lazy<String>) ->  (Lazy<String>) -> Lazy<String> =
                { f ->
                    { ls1 ->
                        { ls2 ->
                            Lazy { f(ls1())(ls2()) }
                        }
                    }
                }
    }
}

val consMessage: (String) -> (String) -> String =
    { greetings ->
        { name ->
            "$greetings, $name!"
        }
    }
