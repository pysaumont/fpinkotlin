package com.fpinkotlin.introduction.listing01_02

fun buyDonut(creditCard: CreditCard): Purchase = Purchase(Donut(), Payment(creditCard, Donut.price))

