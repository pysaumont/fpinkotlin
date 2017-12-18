package com.fpinkotlin.recursion.exercise13


class Unfold {
}

fun <T> List<T>.head(): T =
    if (this.isEmpty())
        throw IllegalArgumentException("head called on empty list")
    else
        this[0]

fun <T> List<T>.tail(): List<T> =
    if (this.isEmpty())
        throw IllegalArgumentException("tail called on empty list")
    else
        this.subList(1, this.size)

fun <T, U> foldLeft(list: List<T>, z: U, f: (U, T) -> U): U {
    tailrec fun foldLeft_(list: List<T>, acc: U, f: (U, T) -> U): U =
        if (list.isEmpty())
            acc
        else
            foldLeft_(list.tail(), f(acc, list.head()), f)
    return foldLeft_(list, z, f)
}

fun <T> prepend(list: List<T>, elem: T): List<T> = foldLeft(list, listOf(elem)) { lst, elm -> lst + elm }

fun <T> unfold(seed: T, f: (T) -> T, p: (T) -> Boolean): List<T> =
    if (p(seed))
        prepend(unfold(f(seed), f, p), seed)
    else
        listOf()
