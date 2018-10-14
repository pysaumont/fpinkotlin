package com.fpinkotlin.makingprogramssafer.listing03

import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.properties.generateInfiniteSequence
import io.kotlintest.specs.StringSpec


class DonutShopTest: StringSpec() {

    init {

        "buyDonut" {
            forAll(CreditCardGenerator()) { creditCard: CreditCard ->
                buyDonut(creditCard).payment.creditCard == creditCard
            }
            forAll(CreditCardGenerator()) { creditCard: CreditCard ->
                buyDonut(creditCard).payment.amount == Donut.price
            }
        }
    }
}

class CreditCardGenerator : Gen<CreditCard> {

    override fun constants(): Iterable<CreditCard> = listOf()

    override fun random(): Sequence<CreditCard> = generateInfiniteSequence { CreditCard() }
}
