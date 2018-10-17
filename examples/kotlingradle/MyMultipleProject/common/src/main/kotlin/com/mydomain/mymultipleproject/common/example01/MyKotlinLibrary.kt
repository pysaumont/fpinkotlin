package com.mydomain.mymultipleproject.common.example01

fun isMaxMultiple(multiple: Int) =
        { max: Int, value: Int ->
            when {
                value / multiple * multiple == value && value > max -> value
                else                                                -> max
            }
        }
