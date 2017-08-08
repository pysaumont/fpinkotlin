package com.fpinkotlin.introduction.listing01_04

import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec


class DonutShopTest: StringSpec() {

    init {

        "buyDonut" {
            forAll(CreditCardGenerator(), { creditCard: CreditCard ->
                val purchase1 = buyDonut(creditCard)
                val purchase2 = buyDonut(creditCard)
                val combinedPayments = purchase1.payment.combine(purchase2.payment)
                combinedPayments.amount == Donut.price * 2
            })
            forAll(CreditCardGenerator(), { creditCard: CreditCard ->
                val purchase1 = buyDonut(creditCard)
                val purchase2 = buyDonut(creditCard)
                val combinedPayments = purchase1.payment.combine(purchase2.payment)
                combinedPayments.creditCard == creditCard
            })
        }
    }

//    @Test
//    fun testBuyDonuts() {
//        val creditCard = CreditCard()
//        val purchase1 = buyDonut(creditCard)
//        val purchase2 = buyDonut(creditCard)
//        val combinedPayments = purchase1.payment.combine(purchase2.payment)
//        assertEquals(Donut.price * 2, combinedPayments.amount)
//        assertEquals(creditCard, combinedPayments.creditCard)
//    }
}

class CreditCardGenerator : Gen<CreditCard> {
    override fun generate(): CreditCard = CreditCard()
}