package com.fpinkotlin.makingprogramssafer.listing05

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
}

class CreditCardGenerator : Gen<CreditCard> {
    override fun generate(): CreditCard = CreditCard()
}