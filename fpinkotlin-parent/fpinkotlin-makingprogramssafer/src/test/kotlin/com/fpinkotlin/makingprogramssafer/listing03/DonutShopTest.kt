package com.fpinkotlin.makingprogramssafer.listing03

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
}

class CreditCardGenerator : Gen<CreditCard> {
    override fun generate(): CreditCard = CreditCard()
}