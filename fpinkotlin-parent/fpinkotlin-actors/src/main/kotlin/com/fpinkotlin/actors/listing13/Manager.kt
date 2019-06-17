package com.fpinkotlin.actors.listing13

import com.fpinkotlin.common.Heap
import com.fpinkotlin.common.List
import com.fpinkotlin.common.Result
import com.fpinkotlin.common.sequence

class Manager(id: String, list: List<Int>,
              private val client: Actor<Result<List<Int>>>,
              private val workers: Int) : AbstractActor<Pair<Int, Int>>(id) {

    private val initial: List<Pair<Int, Int>>
    private val workList: List<Pair<Int, Int>>
    private val resultHeap: Heap<Pair<Int, Int>>
    private val managerFunction: (Manager) -> (Behavior) -> (Pair<Int, Int>) -> Unit

    init {
        val splitLists = list.zipWithPosition().splitAt(this.workers)
        this.initial = splitLists.first
        this.workList = splitLists.second
        this.resultHeap = Heap(Comparator { p1: Pair<Int, Int>, p2: Pair<Int, Int> -> p1.second.compareTo(p2.second) })

        managerFunction = { manager ->
            { behavior ->
                { p ->
                    val result = behavior.resultHeap + p
                    if (result.size == list.length) {
                        this.client.tell(Result(result.toList().map { it.first }))
                    } else {
                        manager.context
                            .become(Behavior(behavior.workList
                                                 .tailSafe()
                                                 .getOrElse(List()), result))
                    }
                }
            }
        }
    }

    fun start() {
        onReceive(Pair(0, 0), self())
        sequence(initial.map { this.initWorker(it) })
            .forEach({ this.initWorkers(it) },
                     { this.tellClientEmptyResult(it.message ?: "Unknown error") })
    }

    private fun initWorker(t: Pair<Int, Int>): Result<() -> Unit> =
        Result(a = { Worker("Worker " + t.second).tell(Pair(t.first, t.second), self()) })

    private fun initWorkers(lst: List<() -> Unit>) {
        lst.forEach { it() }
    }

    private fun tellClientEmptyResult(string: String) {
        client.tell(Result.failure("$string caused by empty input list."))
    }

    override fun onReceive(message: Pair<Int, Int>, sender: Result<Actor<Pair<Int, Int>>>) {
        context.become(Behavior(workList, resultHeap))
    }

    internal inner class Behavior
        internal constructor(internal val workList: List<Pair<Int, Int>>,
                             internal val resultHeap: Heap<Pair<Int, Int>>) :
                                                    MessageProcessor<Pair<Int, Int>> {

        override fun process(message: Pair<Int, Int>,
                             sender: Result<Actor<Pair<Int, Int>>>) {
            managerFunction(this@Manager)(this@Behavior)(message)
            sender.forEach(onSuccess = { a: Actor<Pair<Int, Int>> ->
                workList.headSafe().forEach({ a.tell(it, self()) }) { a.shutdown() }
            })
        }
    }
}
