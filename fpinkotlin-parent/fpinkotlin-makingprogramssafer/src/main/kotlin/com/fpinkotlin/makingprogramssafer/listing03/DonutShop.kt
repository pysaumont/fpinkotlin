package com.fpinkotlin.makingprogramssafer.listing03

fun buyDonut(creditCard: CreditCard): Purchase = Purchase(Donut(), Payment(creditCard, Donut.price))

