package com.fpinkotlin.actors.listing08to12

import com.fpinkotlin.common.List
import com.fpinkotlin.common.Result
import com.fpinkotlin.common.sequence

class Manager(id: String, list: List<Int>,
              private val client: Actor<Result<List<Int>>>,
              private val workers: Int) : AbstractActor<Int>(id) {

    private val initial: List<Pair<Int, Int>>
    private val workList: List<Int>
    private val resultList: List<Int>
    private val managerFunction: (Manager) -> (Behavior) -> (Int) -> Unit

    init {
        val splitLists = list.splitAt(this.workers)
        this.initial = splitLists.first.zipWithPosition()
        this.workList = splitLists.second
        this.resultList = List()

        managerFunction = { manager ->
            { behavior ->
                { i ->
                    val result = behavior.resultList.cons(i)
                    if (result.length == list.length) {
                        this.client.tell(Result(result))
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
        onReceive(0, self())
        sequence(initial.map { this.initWorker(it) })
            .forEach(onSuccess = { this.initWorkers(it) },
                     onFailure = { this.tellClientEmptyResult(it.message ?: "Unknown error") })
    }

    private fun initWorker(t: Pair<Int, Int>): Result<() -> Unit> =
        Result({ Worker("Worker " + t.second).tell(t.first, self()) })

    private fun initWorkers(lst: List<() -> Unit>) {
        lst.forEach { it() }
    }

    private fun tellClientEmptyResult(string: String) {
        client.tell(Result.failure("$string caused by empty input list."))
    }

    override fun onReceive(message: Int, sender: Result<Actor<Int>>) {
        context.become(Behavior(workList, resultList))
    }

    internal inner class Behavior
    internal constructor(internal val workList: List<Int>,
                         internal val resultList: List<Int>) : MessageProcessor<Int> {

        override fun process(message: Int, sender: Result<Actor<Int>>) {
            managerFunction(this@Manager)(this@Behavior)(message)
            sender.forEach(onSuccess = { a: Actor<Int> ->
                workList.headSafe().forEach({ a.tell(it, self()) }) { a.shutdown() }
            })
        }
    }}
