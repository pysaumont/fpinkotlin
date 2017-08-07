package com.fpinkotlin.introduction.listing01_05

import org.junit.Assert.assertEquals
import org.junit.Test

class PaymentTest {

    @Test
    fun testBuyDonuts() {
        val creditCard = CreditCard()
        val purchase = buyDonuts(5, creditCard)
        assertEquals(Donut.price * 5, purchase.payment.amount)
    }
}
