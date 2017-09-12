package com.fpinkotlin.introduction.listing01_04

fun buyDonut(creditCard: CreditCard): Purchase = Purchase(Donut(), Payment(creditCard, Donut.price))

