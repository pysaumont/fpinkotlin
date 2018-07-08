package com.fpinkotlin.makingprogramssafer.listing04

fun buyDonut(creditCard: CreditCard): Purchase = Purchase(Donut(), Payment(creditCard, Donut.price))

