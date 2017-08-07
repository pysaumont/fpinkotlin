package com.fpinkotlin.introduction.listing01_03

import org.junit.Assert.assertEquals
import org.junit.Test


class DonutShopTest {

    @Test
    fun testBuyDonut() {
        val creditCard = CreditCard()
        val purchase = buyDonut(creditCard)
        assertEquals(Donut.price, purchase.payment.amount)
        assertEquals(creditCard, purchase.payment.creditCard)
    }

}
