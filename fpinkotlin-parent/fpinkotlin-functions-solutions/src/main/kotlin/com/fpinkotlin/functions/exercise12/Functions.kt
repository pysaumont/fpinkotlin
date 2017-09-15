package com.fpinkotlin.functions.exercise12


class Functions

fun square(n: Int) = n * n

fun triple(n: Int) = n * 3

fun <T, U, V> compose(f: (U) -> V, g: (T) -> U): (T) -> V = { f(g(it)) }

val add: (Int) -> (Int) -> Int = { a -> { b -> a + b} }

val compose = { x: (Int) -> Int -> { y: (Int) -> Int -> { z: Int -> x(y(z)) } } }

fun <T, U, V> higherCompose(): ((U) -> V) -> ((T) -> U) -> (T) -> V =
    { f ->
        { g ->
            { x -> f(g(x)) }
        }
    }

fun <T, U, V> higherAndThen(): ((T) -> U) -> ((U) -> V) -> (T) -> V =
    { f: (T) -> U ->
        { g: (U) -> V ->
            { x: T -> g(f(x)) }
        }
    }

fun <A, B, C> partialA(a: A, f: (A) -> (B) -> C): (B) -> C =  f(a)

fun <A, B, C> partialB(b: B, f: (A) -> (B) -> C): (A) -> C = { a: A -> f(a)(b) }

fun <A,B,C,D> curried() =
    { a: A ->
        { b: B ->
            { c: C ->
                { d: D -> "$a, $b, $c, $d" }
            }
        }
    }

fun <A, B, C> curry(f: (A, B) -> C): (A) -> (B) -> C = { a -> { b ->  f(a, b) } }

fun <T, U, V> swapArgs(f: (T) -> (U) -> V): (U) -> (T) -> (V) = { u -> { t -> f(t)(u) } }