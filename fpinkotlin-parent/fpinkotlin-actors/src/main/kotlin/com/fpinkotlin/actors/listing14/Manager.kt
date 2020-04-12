package com.fpinkotlin.actors.listing14

import com.fpinkotlin.common.Heap
import com.fpinkotlin.common.List
import com.fpinkotlin.common.Result
import com.fpinkotlin.common.sequence

class Manager(id: String, list: List<Int>,
              private val client: Actor<Int>,
              private val workers: Int) : AbstractActor<Pair<Int, Int>>(id) {

    private val initial: List<Pair<Int, Int>>
    private val workList: List<Pair<Int, Int>>
    private val resultHeap: Heap<Pair<Int, Int>>
    private val managerFunction: (Manager) -> (Behavior) -> (Pair<Int, Int>) -> Unit
    private val limit: Int

    init {
        val splitLists = list.zipWithPosition().splitAt(this.workers)
        this.initial = splitLists.first
        this.workList = splitLists.second
        this.resultHeap = Heap(Comparator { p1: Pair<Int, Int>, p2: Pair<Int, Int> -> p1.second.compareTo(p2.second) })
        this.limit = list.length - 1

        managerFunction = { manager ->
            { behavior ->
                { p ->
                    val result =
                        streamResult(behavior.resultHeap + p, behavior.expected, List())
                    result.third.reverse().forEach { client.tell(it) }
                    if (result.second > limit) {
                        this.client.tell(-1)
                    } else {
                        manager.context
                            .become(Behavior(behavior.workList
                                                 .tailSafe()
                                                 .getOrElse(List()), result.first, result.second))
                    }
                }
            }
        }
    }

    private fun streamResult(result: Heap<Pair<Int, Int>>,
                             expected: Int,
                             list: List<Int>): Triple<Heap<Pair<Int, Int>>, Int, List<Int>> {
        val triple = Triple(result, expected, list)
        val temp =
            result.head.flatMap { head ->
                result.tail().map { tail ->
                    if (head.second == expected) {
                        val newList = list.cons(head.first)
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
