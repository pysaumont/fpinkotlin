package com.fpinkotlin.makingprogramssafer.listing01

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class DonutShopTest  {

    @Test
    fun testBuyCoffee() {
        val creditCard = CreditCard()
        buyDonut(creditCard)
        buyDonut(creditCard)
        assertEquals(Donut.price * 2, creditCard.total)
    }

}
