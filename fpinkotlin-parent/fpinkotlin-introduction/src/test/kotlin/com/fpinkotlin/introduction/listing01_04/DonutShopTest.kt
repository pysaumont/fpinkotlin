package com.fpinkotlin.introduction.listing01_04

import org.junit.Assert.assertEquals
import org.junit.Test


class DonutShopTest {

    @Test
    fun testBuyDonuts() {
        val creditCard = CreditCard()
        val purchase1 = buyDonut(creditCard)
        val purchase2 = buyDonut(creditCard)
        val combinedPayments = purchase1.payment.combine(purchase2.payment)
        assertEquals(Donut.price * 2, combinedPayments.amount)
        assertEquals(creditCard, combinedPayments.creditCard)
    }
}
