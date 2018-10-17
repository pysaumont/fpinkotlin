package com.fpinkotlin.advancedtrees.exercise02


import com.fpinkotlin.advancedtrees.exercise02.NumbersToEnglish.convertGB
import com.fpinkotlin.advancedtrees.exercise02.NumbersToEnglish.convertUS
import com.fpinkotlin.advancedtrees.exercise02.NumbersToEnglish.convertUnder1000
import com.fpinkotlin.advancedtrees.exercise02.NumbersToEnglish.decompose
import com.fpinkotlin.advancedtrees.exercise02.NumbersToEnglish.thousands2String
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class NumbersToEnglishTest: StringSpec() {

    init {

        "testBillions" {
            thousands2String(NumbersToEnglish.Locale.US)(Pair(0, "thousand")) shouldBe ""
            thousands2String(NumbersToEnglish.Locale.US)(Pair(1, "thousand")) shouldBe "one thousand"
            thousands2String(NumbersToEnglish.Locale.US)(Pair(3, "thousand")) shouldBe "three thousand"
            thousands2String(NumbersToEnglish.Locale.US)(Pair(10, "thousand")) shouldBe "ten thousand"
            thousands2String(NumbersToEnglish.Locale.US)(Pair(15, "thousand")) shouldBe "fifteen thousand"
            thousands2String(NumbersToEnglish.Locale.US)(Pair(18, "thousand")) shouldBe "eighteen thousand"
            thousands2String(NumbersToEnglish.Locale.US)(Pair(20, "million")) shouldBe "twenty million"
            thousands2String(NumbersToEnglish.Locale.US)(Pair(21, "million")) shouldBe "twenty-one million"
            thousands2String(NumbersToEnglish.Locale.US)(Pair(35, "million")) shouldBe "thirty-five million"
            thousands2String(NumbersToEnglish.Locale.US)(Pair(59, "million")) shouldBe "fifty-nine million"
            thousands2String(NumbersToEnglish.Locale.US)(Pair(93, "")) shouldBe "ninety-three"
            thousands2String(NumbersToEnglish.Locale.US)(Pair(100, "")) shouldBe "one hundred"
            thousands2String(NumbersToEnglish.Locale.US)(Pair(101, "")) shouldBe "one hundred one"
            thousands2String(NumbersToEnglish.Locale.GB)(Pair(101, "")) shouldBe "one hundred and one"
            thousands2String(NumbersToEnglish.Locale.US)(Pair(203, "billion")) shouldBe "two hundred three billion"
            thousands2String(NumbersToEnglish.Locale.GB)(Pair(203, "billion")) shouldBe "two hundred and three billion"
            thousands2String(NumbersToEnglish.Locale.US)(Pair(410, "billion")) shouldBe "four hundred ten billion"
            thousands2String(NumbersToEnglish.Locale.GB)(Pair(410, "billion")) shouldBe "four hundred and ten billion"
            thousands2String(NumbersToEnglish.Locale.US)(Pair(515, "billion")) shouldBe "five hundred fifteen billion"
            thousands2String(NumbersToEnglish.Locale.GB)(Pair(515, "billion")) shouldBe "five hundred and fifteen billion"
            thousands2String(NumbersToEnglish.Locale.US)(Pair(765, "billion")) shouldBe "seven hundred sixty-five billion"
            thousands2String(NumbersToEnglish.Locale.GB)(Pair(765, "billion")) shouldBe "seven hundred and sixty-five billion"
        }

        "testDecompose" {
            decompose(0).toString() shouldBe "[NIL]"
            decompose(16).toString() shouldBe "[16, NIL]"
            decompose(80).toString() shouldBe "[80, NIL]"
            decompose(100).toString() shouldBe "[100, NIL]"
            decompose(101).toString() shouldBe "[101, NIL]"
            decompose(118).toString() shouldBe "[118, NIL]"
            decompose(315).toString() shouldBe "[315, NIL]"
            decompose(1000).toString() shouldBe "[0, 1, NIL]"
            decompose(1001).toString() shouldBe "[1, 1, NIL]"
            decompose(1010).toString() shouldBe "[10, 1, NIL]"
            decompose(1101).toString() shouldBe "[101, 1, NIL]"
            decompose(2018).toString() shouldBe "[18, 2, NIL]"
            decompose(1000000000).toString() shouldBe "[0, 0, 0, 1, NIL]"
            decompose(1000000016).toString() shouldBe "[16, 0, 0, 1, NIL]"
            decompose(2000002080).toString() shouldBe "[80, 2, 0, 2, NIL]"
            decompose(2000054100).toString() shouldBe "[100, 54, 0, 2, NIL]"
            decompose(2001327101).toString() shouldBe "[101, 327, 1, 2, NIL]"
            decompose(1183842468).toString() shouldBe "[468, 842, 183, 1, NIL]"
        }

        "testConvertUnder1000" {
            convertUnder1000(NumbersToEnglish.Locale.US)(0) shouldBe ""
            convertUnder1000(NumbersToEnglish.Locale.US)(16) shouldBe "sixteen"
            convertUnder1000(NumbersToEnglish.Locale.US)(80) shouldBe "eighty"
            convertUnder1000(NumbersToEnglish.Locale.US)(100) shouldBe "one hundred"
            convertUnder1000(NumbersToEnglish.Locale.US)(101) shouldBe "one hundred one"
            convertUnder1000(NumbersToEnglish.Locale.GB)(101) shouldBe "one hundred and one"
            convertUnder1000(NumbersToEnglish.Locale.US)(118) shouldBe "one hundred eighteen"
            convertUnder1000(NumbersToEnglish.Locale.GB)(118) shouldBe "one hundred and eighteen"
            convertUnder1000(NumbersToEnglish.Locale.US)(315) shouldBe "three hundred fifteen"
            convertUnder1000(NumbersToEnglish.Locale.GB)(315) shouldBe "three hundred and fifteen"
            convertUnder1000(NumbersToEnglish.Locale.US)(1000) shouldBe ""
            convertUnder1000(NumbersToEnglish.Locale.US)(1001) shouldBe "one"
            convertUnder1000(NumbersToEnglish.Locale.US)(1010) shouldBe "ten"
            convertUnder1000(NumbersToEnglish.Locale.US)(1101) shouldBe "one hundred one"
            convertUnder1000(NumbersToEnglish.Locale.GB)(1101) shouldBe "one hundred and one"
            convertUnder1000(NumbersToEnglish.Locale.US)(2018) shouldBe "eighteen"
            convertUnder1000(NumbersToEnglish.Locale.US)(1000000000) shouldBe ""
            convertUnder1000(NumbersToEnglish.Locale.US)(1000000014) shouldBe "fourteen"
            convertUnder1000(NumbersToEnglish.Locale.US)(2000002096) shouldBe "ninety-six"
            convertUnder1000(NumbersToEnglish.Locale.US)(2000054700) shouldBe "seven hundred"
            convertUnder1000(NumbersToEnglish.Locale.US)(2001327117) shouldBe "one hundred seventeen"
            convertUnder1000(NumbersToEnglish.Locale.GB)(2001327117) shouldBe "one hundred and seventeen"
            convertUnder1000(NumbersToEnglish.Locale.US)(1183842468) shouldBe "four hundred sixty-eight"
            convertUnder1000(NumbersToEnglish.Locale.GB)(1183842468) shouldBe "four hundred and sixty-eight"
        }

        "testConvert" {
            convertUS(0) shouldBe "zero"
            convertUS(16) shouldBe "sixteen"
            convertUS(80) shouldBe "eighty"
            convertUS(100) shouldBe "one hundred"
            convertUS(101) shouldBe "one hundred one"
            convertGB(101) shouldBe "one hundred and one"
            convertUS(118) shouldBe "one hundred eighteen"
            convertGB(118) shouldBe "one hundred and eighteen"
            convertUS(315) shouldBe "three hundred fifteen"
            convertGB(315) shouldBe "three hundred and fifteen"
            convertUS(1000) shouldBe "one thousand"
            convertUS(1001) shouldBe "one thousand one"
            convertUS(1010) shouldBe "one thousand ten"
            convertUS(1101) shouldBe "one thousand one hundred one"
            convertGB(1101) shouldBe "one thousand one hundred and one"
            convertUS(2018) shouldBe "two thousand eighteen"
            convertUS(1000000000) shouldBe "one billion"
            convertUS(1000000016) shouldBe "one billion sixteen"
            convertUS(2000000080) shouldBe "two billion eighty"
            convertUS(2000054100) shouldBe "two billion fifty-four thousand one hundred"
            convertUS(2001327101) shouldBe "two billion one million three hundred twenty-seven thousand one hundred one"
            convertGB(2001327101) shouldBe "two billion one million three hundred and twenty-seven thousand one hundred and one"
            convertUS(1183842468) shouldBe "one billion one hundred eighty-three million eight hundred forty-two thousand four hundred sixty-eight"
            convertGB(1183842468) shouldBe "one billion one hundred and eighty-three million eight hundred and forty-two thousand four hundred and sixty-eight"
            convertUS(-1183842468) shouldBe "minus one billion one hundred eighty-three million eight hundred forty-two thousand four hundred sixty-eight"
            convertGB(-1183842468) shouldBe "minus one billion one hundred and eighty-three million eight hundred and forty-two thousand four hundred and sixty-eight"
        }

    }
}
