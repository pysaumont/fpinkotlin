package com.fpinkotlin.introduction.listing01_05

import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class PaymentTest: StringSpec() {

    init {

        "buyDonut" {
            forAll(CreditCardGenerator(), Gen.choose(1, 100), { creditCard: CreditCard, n: Int ->
                val purchase = buyDonuts(n, creditCard)
                purchase.payment.amount == Donut.price * n
            })
        }
    }
//
//    @Test
//    fun testBuyDonuts() {
//        val creditCard = CreditCard()
//        val purchase = buyDonuts(5, creditCard)
//        assertEquals(Donut.price * 5, purchase.payment.amount)
//    }
}

class CreditCardGenerator : Gen<CreditCard> {
    override fun generate(): CreditCard = CreditCard()
}