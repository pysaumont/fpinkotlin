package com.fpinkotlin.introduction.listing01_03

fun buyDonut(creditCard: CreditCard): Purchase = Purchase(Donut(), Payment(creditCard, Donut.price))

