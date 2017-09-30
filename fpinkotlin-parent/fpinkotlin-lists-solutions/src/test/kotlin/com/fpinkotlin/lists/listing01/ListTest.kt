package com.fpinkotlin.lists.listing01

import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class ListTest: StringSpec() {

    init {

        "empty" {
            forAll(EmptyListGenerator(), { list: List<Int> ->
                list.isEmpty()
            })
        }

        "not empty" {
            forAll(IntListGenerator(), { pair ->
                !pair.second.isEmpty()
            })
        }

        "toString" {
            forAll(IntListGenerator(), { (first, second) ->
                second.toString() == first.joinToString(", ", "[", ", NIL]")
            })
        }
    }
}

class EmptyListGenerator<A> : Gen<List<A>> {
    override fun generate(): List<A> = List()
}

class IntListGenerator : Gen<Pair<Array<Int>, List<Int>>> {
    private val array: Array<Int> = Gen.set(Gen.int()).generate().toTypedArray()
    override fun generate(): Pair<Array<Int>, List<Int>> = Pair(array, List(*array))
}

//class ListGenerator<A>(clazz: Class<A>, gen: Gen<A>) : Gen<Pair<Array<A>, List<A>>> {
//    private val set: Set<A> = Gen.set(gen).generate()
//    private val array = toArray(set)
//    override fun generate(): Pair<Array<A>, List<A>> = Pair(array, List(*array))
//}
//
//inline fun <reified A> toArray(set: Set<A>): Array<A> = Array(set.size) { set.elementAt(it) }
