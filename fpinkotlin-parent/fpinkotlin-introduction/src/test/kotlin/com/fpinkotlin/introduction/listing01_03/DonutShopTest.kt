package com.fpinkotlin.introduction.listing01_03

import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec


class DonutShopTest: StringSpec() {

    init {

        "buyDonut" {
            forAll(CreditCardGenerator(), { creditCard: CreditCard ->
                buyDonut(creditCard).payment.creditCard == creditCard
            })
            forAll(CreditCardGenerator(), { creditCard: CreditCard ->
                buyDonut(creditCard).payment.amount == Donut.price
            })
        }
    }

//    @Test
//    fun testBuyDonut() {
//        val creditCard = CreditCard()
//        val purchase = buyDonut(creditCard)
//        assertEquals(Donut.price, purchase.payment.amount)
//        assertEquals(creditCard, purchase.payment.creditCard)
//    }

}

class CreditCardGenerator : Gen<CreditCard> {
    override fun generate(): CreditCard = CreditCard()
}