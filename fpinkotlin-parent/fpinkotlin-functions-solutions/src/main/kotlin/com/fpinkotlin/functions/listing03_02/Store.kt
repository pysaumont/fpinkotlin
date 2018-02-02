package com.fpinkotlin.functions.listing03_02

import com.fpinkotlin.functions.listing03_03.OrderLine

data class Product(val name: String, val price: Double, val weight: Double)

data class OrderLine(val product: Product, val count: Int) {

    fun weight() = product.weight * count

    fun amount() = product.price * count
}

object Store {

    @JvmStatic
    fun main(args: Array<String>) {
        val toothPaste = Product("Tooth paste", 1.5, 0.5)
        val toothBrush = Product("Tooth brush", 3.5, 0.3)
        val orderLines = listOf(
            OrderLine(toothPaste, 2),
            OrderLine(toothBrush, 3))
        val weight = orderLines.sumByDouble { it.amount() }
        val price = orderLines.sumByDouble { it.weight() }
        println("Total price: $price")
        println("Total weight: $weight")
    }
}
