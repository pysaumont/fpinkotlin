package com.fpinkotlin.introduction.listing01_06

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PaymentTest {

    @Test
    fun testGroupByCard() {
        val creditCard1 = CreditCard()
        val creditCard2 = CreditCard()
        val purchase1 = buyDonuts(5, creditCard1)
        val purchase2 = buyDonuts(3, creditCard2)
        val purchase3 = buyDonuts(2, creditCard1)
        val purchase4 = buyDonuts(1, creditCard1)
        val purchase5 = buyDonuts(4, creditCard2)
        val paymentList = Payment.groupByCard(listOf(purchase1.payment, purchase2.payment, purchase3.payment, purchase4.payment, purchase5.payment))
        assertEquals(2, paymentList.size)
        val payments: Map<CreditCard, Int> = paymentList.fold(mapOf()) { map, p -> map + (Pair(p.creditCard, p.amount)) }
        assertTrue(payments[creditCard1]?.equals(16) ?: false)
        // equivalent syntax
        assertTrue(payments[creditCard2]?.let { it == 14 } ?: false)
    }
}
