package com.fpinkotlin.makingprogramssafer.listing01_04

fun buyDonut(creditCard: CreditCard): Purchase = Purchase(Donut(), Payment(creditCard, Donut.price))

