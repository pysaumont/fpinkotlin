package com.fpinkotlin.functions.listing03_03

data class Product(val name: String, val price: Price, val weight: Weight)

data class OrderLine(val product: Product, val count: Int) {

    fun weight() = product.weight * count

    fun amount() = product.price * count
}

data class Price private constructor (private val value: Double) {

    override fun toString() = value.toString()

    operator fun plus(price: Price) = Price(this.value + price.value)

    operator fun times(num: Int) = Price(this.value * num)

    companion object {
        val identity = Price(0.0)

        operator fun invoke(value: Double) =
            if (value > 0)
                Price(value)
            else
                throw IllegalArgumentException("Price must be positive or null")
    }
}

data class Weight private constructor (private val value: Double) {

    override fun toString() = value.toString()

    operator fun plus(price: Weight) = Weight(this.value + price.value)

    operator fun times(num: Int) = Weight(this.value * num)

    companion object {
        val identity = Weight(0.0)

        operator fun invoke(value: Double) =
            if (value > 0)
                Weight(value)
            else
                throw IllegalArgumentException("Weight must be positive or null")
    }
}


object Store {

    @JvmStatic
    fun main(args: Array<String>) {
        val toothPaste = Product("Tooth paste", Price(1.5), Weight(0.5))
        val toothBrush = Product("Tooth brush", Price(3.5), Weight(0.3))
        val orderLines = listOf(
            OrderLine(toothPaste, 2),
            OrderLine(toothBrush, 3))
        val weight: Weight = orderLines.fold(Weight.identity) { a, b -> a + b.weight() }
        val price: Price = orderLines.fold(Price.identity) { a, b -> a + b.amount() }
        println("Total price: $price")
        println("Total weight: $weight")
    }
}
