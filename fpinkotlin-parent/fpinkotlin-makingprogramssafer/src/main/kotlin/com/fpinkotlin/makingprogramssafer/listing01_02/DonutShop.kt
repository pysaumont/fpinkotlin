package com.fpinkotlin.makingprogramssafer.listing01_02

fun buyDonut(creditCard: CreditCard): Purchase = Purchase(Donut(), Payment(creditCard, Donut.price))

