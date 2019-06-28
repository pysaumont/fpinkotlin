package com.fpinkotlin.actors.listing18

import com.asn.pmdatabase.checker.actors01.listing18.Heap
import com.fpinkotlin.common.Result


class Manager(id: String, list: List<Int>,
              private val client: Actor<Int>,
              workers: Int) : AbstractActor<Pair<Int, Int>>(id) {

    private val initial: List<Pair<Int, Int>>
    private val workList: List<Pair<Int, Int>>
    private val resultHeap: Heap<Pair<Int, Int>>
    private val managerFunction: (Manager) -> (Behavior) -> (Pair<Int, Int>) -> Unit
    private val limit: Int

    init {
        val splitLists = list.mapIndexed{ index, n -> Pair(n, index) }
        this.initial = splitLists.take(workers)
        this.workList = splitLists.drop(workers)
        this.resultHeap = Heap(Comparator { p1: Pair<Int, Int>, p2: Pair<Int, Int> -> p1.second.compareTo(p2.second) })
        this.limit = list.size - 1

        managerFunction = { manager ->
            { behavior ->
                { p ->
                    val result = streamResult(behavior.resultHeap + p,
                                              behavior.expected, listOf())
                    result.third.reversed().forEach { client.tell(it) }
                    if (result.second > limit) {
                        this.client.tell(-1)
                    } else {
                        manager.context
                            .become(Behavior(behavior.workList
                                                 .tailSafe()
                                                 .getOrElse(listOf()), result.first, result.second))
                    }
                }
            }
        }
    }

    private fun streamResult(result: Heap<Pair<Int, Int>>,
                             expected: Int,
                             list: List<Int>): Triple<Heap<Pair<Int, Int>>, Int, List<Int>> {
        val triple = Triple(result, expected, list)
        val temp = result.head()
            .flatMap { head ->
                result.tail().map { tail ->
                    if (head.second == expected) {
                        val newList = list + head.first
                        streamResult(tail, expected + 1, newList)
                    }
                    else {
                        triple
                    }
                }
            }
        return temp.getOrElse(triple)
    }

    fun start() {
        onReceive(Pair(0, 0), self())
        sequence(initial.map { this.initWorker(it) })
            .forEach({ this.initWorkers(it) },
                     { client.tell(-1) })
    }

    private fun initWorker(t: Pair<Int, Int>): Result<() -> Unit> =
        Result(a = { Worker("Worker " + t.second).tell(Pair(t.first, t.second), self()) })

    private fun initWorkers(lst: List<() -> Unit>) {
        lst.forEach { it() }
    }

    override fun onReceive(message: Pair<Int, Int>,
                           sender: Result<Actor<Pair<Int, Int>>>) {
        context.become(Behavior(workList, resultHeap, 0))
    }

    internal inner class Behavior
        internal constructor(internal val workList: List<Pair<Int, Int>>,
                             internal val resultHeap: Heap<Pair<Int, Int>>,
                             internal val expected: Int) : MessageProcessor<Pair<Int, Int>> {

        override fun process(message: Pair<Int, Int>,
                             sender: Result<Actor<Pair<Int, Int>>>) {
            managerFunction(this@Manager)(this@Behavior)(message)
            sender.forEach(onSuccess = { a: Actor<Pair<Int, Int>> ->
                workList.headSafe().forEach({ a.tell(it, self()) }) { a.shutdown() }
            })
        }
    }
}

fun <T> List<T>.headSafe() = Result.of {
    this[0]
}

fun <T> List<T>.tailSafe() = Result.of {
    this.drop(1)
}

fun <A, B> traverse(list: List<A>, f: (A) -> Result<B>): Result<List<B>> =
    list.foldRight(Result(listOf())) { x, y: Result<List<B>> ->
            map2(f(x), y) { a -> { b: List<B> -> b + a } }
        }

fun <A> sequence(list: List<Result<A>>): Result<List<A>> =
    traverse(list) { x: Result<A> -> x }

fun <A, B, C> lift2(f: (A) -> (B) -> C): (Result<A>) -> (Result<B>) -> Result<C> =
    { a ->
        { b ->
            a.map(f).flatMap { b.map(it) }
        }
    }

fun <A, B, C> map2(a: Result<A>,
                   b: Result<B>,
                   f: (A) -> (B) -> C): Result<C> = lift2(f)(a)(b)
