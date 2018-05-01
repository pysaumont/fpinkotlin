package com.fpinkotlin.commonproblems.assertions.listing01

import com.fpinkotlin.common.Result


fun <T> assertCondition(value: T, f: (T) -> Boolean): Result<T> =
    assertCondition(value, f, "Assertion error: condition should evaluate to true")

fun <T> assertCondition(value: T, f: (T) -> Boolean, message: String): Result<T> =
        if (f(value))
            Result(value)
        else
            Result.failure(IllegalStateException(message))

fun assertTrue(condition: Boolean,
               message: String = "Assertion error: condition should be true"): Result<Boolean> =
        assertCondition(condition, { x -> x }, message)

fun assertFalse(condition: Boolean,
                message: String = "Assertion error: condition should be false"): Result<Boolean> =
        assertCondition(condition, { x -> !x }, message)

fun <T> assertNotNull(t: T): Result<T> =
        assertNotNull(t, "Assertion error: object should not be null")

fun <T> assertNotNull(t: T, message: String): Result<T> =
        assertCondition(t, { x -> x != null }, message)

fun assertPositive(value: Int,
                   message: String = "Assertion error: value $value must be positive"): Result<Int> =
        assertCondition(value, { x -> x > 0 }, message)

fun assertInRange(value: Int, min: Int, max: Int): Result<Int> =
        assertCondition(value, { x -> x in min..(max - 1) },
                "Assertion error: value $value should be between $min and $max (exclusive)")

fun assertPositiveOrZero(value: Int,
                         message: String = "Assertion error: value $value must not be negative"): Result<Int> =
        assertCondition(value, { x -> x >= 0 }, message)

fun <A: Any> assertType(element: A, clazz: Class<*>): Result<A> =
    assertType(element, clazz, "Wrong type: ${element.javaClass}, expected: ${clazz.name}")


fun <A: Any> assertType(element: A, clazz: Class<*>, message: String): Result<A> =
        assertCondition(element, { e -> e.javaClass == clazz }, message)

