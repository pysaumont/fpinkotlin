package com.fpinkotlin.makingprogramssafer.listing05


class Payment(val creditCard: CreditCard, val amount: Int) {

    fun combine(payment: Payment): Payment {
        if (creditCard == payment.creditCard) {
            return Payment(creditCard, amount + payment.amount)
        } else {
            throw IllegalStateException("Cards don't match.")
        }
    }
}
