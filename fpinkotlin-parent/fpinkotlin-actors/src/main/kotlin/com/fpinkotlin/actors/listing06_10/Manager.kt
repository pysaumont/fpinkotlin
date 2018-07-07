package com.fpinkotlin.actors.listing06_10

import com.fpinkotlin.common.List
import com.fpinkotlin.common.Result
import com.fpinkotlin.common.sequence

class Manager(id: String, list: List<Int>,
              private val client: Actor<Result<List<Int>>>, // <1>
              private val workers: Int) : AbstractActor<Int>(id) { // <2>

    private val initial: List<Pair<Int, Int>> // <3>
    private val workList: List<Int> // <4>
    private val resultList: List<Int> // <5>
    private val managerFunction: (Manager) -> (Behavior) -> (Int) -> Unit // <6>

    init {
        val splitLists = list.splitAt(this.workers) // <7>
        this.initial = splitLists.first.zipWithPosition() // <8>
        this.workList = splitLists.second // <9>
        this.resultList = List() // <10>

        managerFunction = { manager -> // <11>
            { behavior ->
                { i ->
                    val result = behavior.resultList.cons(i) // <12>
                    if (result.length == list.length) {
                        this.client.tell(Result(result)) // <13>
                    } else {
                        manager.context
                            .become(Behavior(behavior.workList  // <14>
                                                 .tailSafe()
                                                 .getOrElse(List()), result))
                    }
                }
            }
        }
    }

    fun start() {
        onReceive(0, self()) // <1>
        sequence(initial.map { this.initWorker(it) })
            .forEach({ this.initWorkers(it) },
                     { this.tellClientEmptyResult(it.message ?: "Unknown error") }) // <2>
    }

    private fun initWorker(t: Pair<Int, Int>): Result<() -> Unit> = // <3>
        Result({ Worker("Worker " + t.second).tell(t.first, self()) })

    private fun initWorkers(lst: List<() -> Unit>) {
        lst.forEach { it() } // <4>
    }

    private fun tellClientEmptyResult(string: String) { // <5>
        client.tell(Result.failure("$string caused by empty input list."))
    }

    override fun onReceive(message: Int, sender: Result<Actor<Int>>) { // <6>
        context.become(Behavior(workList, resultList))
    }

    internal inner class Behavior
        internal constructor(internal val workList: List<Int>, // <1>
                             internal val resultList: List<Int>) : MessageProcessor<Int> {

        override fun process(message: Int, sender: Result<Actor<Int>>) { // <2>
            managerFunction(this@Manager)(this@Behavior)(message)
            sender.forEach({ a: Actor<Int> ->
                workList.headSafe().forEach({ a.tell(it, self()) }) { a.shutdown() }
            })
        }
    }
}
