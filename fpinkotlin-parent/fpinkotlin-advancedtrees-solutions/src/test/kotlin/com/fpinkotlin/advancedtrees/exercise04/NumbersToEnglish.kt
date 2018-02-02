package com.fpinkotlin.advancedtrees.exercise04

import com.fpinkotlin.common.List
import com.fpinkotlin.common.Option
import com.fpinkotlin.common.unfold
import com.fpinkotlin.common.zipWith

object NumbersToEnglish {

    private const val SPACE = " "

    private const val DASH = "-"

    private val numNames = arrayOf("", "one", "two", "three", "four", "five", "six", "seven",
            "eight", "nine", "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen",
            "sixteen", "seventeen", "eighteen", "nineteen")

    private val tensNames = arrayOf("", "ten", "twenty", "thirty", "forty", "fifty", "sixty",
            "seventy", "eighty", "ninety")

    private val thousandNames = List("", "thousand", "million", "billion")

    /**
     * Concat two string using an additional separator string. The separator is used only if both strings
     * are not empty. If one of the string is empty, the other is returned, without using the separator.
     *
     *
     * Example:
     * concatWithSeparator(" and ")("Sam")("Dave") -> Sam and Dave
     * concatWithSeparator(" and ")("")("Dave") -> Dave
     *
     */
    private val concatWithSeparator = { separator: String ->
        { s1: String ->
            { s2: String ->
                when {
                    s1.isEmpty() -> s2
                    s2.isEmpty() -> s1
                    else -> "$s1$separator$s2"
                }
            }
        }
    }

    /**
     * Concat strings using a space as a separator if needed, i.e if both strings are not empty.
     */
    internal val concatWithSpace = concatWithSeparator(SPACE)

    /**
     * Concat strings using the word "and" as a separator if needed, i.e if both strings are not empty.
     */
    internal val concatWithAnd = concatWithSeparator(" and ")

    /**
     * Decompose a number into a list of factors. Those factors would allow recomputing the number
     * by multiplying them by 10 ^ (3 * index) and summing the results, where index is the position of
     * each factor in the list.
     *
     * Example: 1 234 567 890 = 890 * (10 ^ (3 * 0)) + 567 * (10 ^ (3 * 1)) + 234 * (10 ^ (3 * 2)) + 1 * (10 ^ (3 * 3))
     */
    internal val decompose: (Int) -> List<Int> = { n ->
        unfold(n) { n2 ->
            when {
                n2 > 0 -> Option(Pair(n2 % 1000, n2 / 1000))
                else -> Option()
            }
        }
    }

    /**
     * Convert an integer between 0 and 999 into a string representation using English number names.
     * This method does not check its argument, which is converted modulo 1000. This method is
     * intended to be used in larger conversion, in which the result is to be followed by the name
     * of a factor of the form (10 ^ (3 * n)) such a "thousand", "million" or "billion". As there is
     * no name for (10 ^ (3 * 0)), the conversion of 0 returns an empty string. All other results are
     * prefixed by a space. Conversion to GB English includes " and " after "hundred", like in
     * "hundred and twenty-one", whether US English conversion doe not.
     */
    internal val convertUnder1000: (Locale) -> (Int) -> String = { locale ->
        { n ->
            val hundred = n % 1000 / 100
            val tens = n % 100
            val units = tens % 10
            val h = if (hundred == 0) "" else numNames[hundred] + " hundred"
            val t = when {
                tens == 0 -> when (units) {
                    0 -> ""
                    else -> numNames[units]
                }
                tens < 20 -> numNames[tens]
                else -> tensNames[(tens - units) / 10] + if (units == 0) "" else DASH + numNames[units]
            }
            when (locale) {
                Locale.US -> concatWithSpace(h)(t)
                else -> concatWithAnd(h)(t)
            }
        }
    }

    /**
     * Convert a tuple (number, string) into a string, concatenating the string representation of the
     * number with the string parameter, adding a space separator if the string is not empty.
     */
    internal val thousands2String: (Locale) -> (Pair<Int, String>) -> String = { locale ->
        { pair ->
            when (pair.first) {
                0 -> ""
                else -> concatWithSpace(convertUnder1000(locale)(pair.first))(pair.second)
            }
        }
    }

    /**
     * Convert an integer to its string representation using a Locale (American English or British English).
     */
    internal val convert: (Locale) -> (Int) -> String = { locale ->
        { n ->
            val result = when (n) {
                0 -> "zero"
                else -> zipWith(decompose(Math.abs(n)), thousandNames) { i ->
                    { s: String -> Pair(i, s) }
                }.reverse().map(thousands2String(locale)).foldLeft("", concatWithSpace)
            }
            when {
                n < 0 -> "minus " + result
                else -> result
            }
        }
    }

    /**
     * Convert an integer to its string representation British English.
     */
    val convertGB = convert(Locale.GB)

    /**
     * Convert an integer to its string representation American English.
     */
    val convertUS = convert(Locale.US)

    /**
     * The possible locale language variants.
     */
    enum class Locale {
        US, GB
    }
}
