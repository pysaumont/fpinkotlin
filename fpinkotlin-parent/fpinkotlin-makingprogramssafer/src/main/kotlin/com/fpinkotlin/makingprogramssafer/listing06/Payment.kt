package com.fpinkotlin.makingprogramssafer.listing06


data class Payment(val creditCard: CreditCard, val amount: Int) {
    fun combine(payment: Payment): Payment =
            if (creditCard == payment.creditCard)
                Payment(creditCard, amount + payment.amount)
            else
                throw IllegalStateException("Cards don't match.")

    companion object {
        fun groupByCard(payments: List<Payment>): List<Payment> =
                payments.groupBy { it.creditCard }
                        .values
                        .map { it.reduce(Payment::combine) }
    }
}
