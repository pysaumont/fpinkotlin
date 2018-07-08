package com.fpinkotlin.makingprogramssafer.listing02

class CreditCard {

    // total has a public getter (by default) and a private setter
    var total: Int = 0
        private set

    fun charge(price: Int) {
        this.total += price
    }
}
