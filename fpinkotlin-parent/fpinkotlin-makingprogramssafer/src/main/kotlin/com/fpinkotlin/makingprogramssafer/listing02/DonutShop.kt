package com.fpinkotlin.makingprogramssafer.listing02

fun buyDonut(creditCard: CreditCard): Purchase = Purchase(Donut(), Payment(creditCard, Donut.price))

