package com.fpinkotlin.makingprogramssafer.listing06

import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class PaymentTest: StringSpec() {

    init {

        /**
         * Generates 1000 lists of 1 to 100 purchases of 1 to 100 donuts with 2 different credit cards.
         *
         * Then checks that the amount withdraw from each credit card correspond to the total of the
         * corresponding purchases.
         */
        "buyDonut" {
            forAll(Gen.list(PurchaseGenerator()), { purchases: List<Purchase> ->
                val paymentList = purchases.map { it.payment}
                val groupedPaymentList = Payment.groupByCard(paymentList)
                val paymentMap: Map<CreditCard, Int> = groupedPaymentList.fold(mapOf()) { map, (creditCard, amount) -> map + (Pair(creditCard, amount)) }
                val creditCardSet: Set<CreditCard> = purchases.fold(setOf()) { set, purchase -> set + purchase.payment.creditCard }
                val charges: List<Pair<CreditCard, List<Payment>>> = creditCardSet.map { creditCard -> Pair(creditCard, paymentList.filter { it.creditCard == creditCard}) }
                charges.fold(true) { acc, p -> acc && p.second.sumBy { it.amount } == paymentMap[p.first]}
            })
        }
    }
}

class PurchaseGenerator : Gen<Purchase> {
    private val creditCard1 = CreditCard()
    private val creditCard2 = CreditCard()
    private val chooser = Gen.bool()
    override fun generate(): Purchase = Gen.choose(0, 100).generate().let { if (chooser.generate()) buyDonuts(it, creditCard1) else buyDonuts(it, creditCard2) }
}