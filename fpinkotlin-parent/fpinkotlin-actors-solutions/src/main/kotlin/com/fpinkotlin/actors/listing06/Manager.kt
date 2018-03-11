package com.fpinkotlin.actors.listing06

//class Manager(id: String, list: List<Int>,
//              private val client: Actor<Result<List<Int>>>, private val workers: Int) : AbstractActor<Int>(id) {
//    private val initial: List<Pair<Int, Int>>
//    private val workList: List<Int>
//    private val resultList: List<Int>
//    private val managerFunction: Function<Manager, ???>
//
//    init {
//        val splitLists = list.splitAt(this.workers)
//        this.initial = splitLists.first.zipWithPosition()
//        this.workList = splitLists.second
//        this.resultList = List()
//
//        managerFunction = { manager ->
//            { behavior ->
//                { i ->
//                    val result = behavior.resultList.cons(i)
//                    if (result.length() === list.length()) {
//                        this.client.tell(Result.success(result.reverse()))
//                    } else {
//                        manager.getContext()
//                                .become(Behavior(behavior.workList
//                                        .tailOption()
//                                        .getOrElse(List.list()), result))
//                    }
//                }
//            }
//        }
//    }
//
//    fun start() {
//        onReceive(0, self())
//        initial.sequence(???({ this.initWorker(it) }))
//        .forEachOrFail(???({ this.initWorkers(it) }))
//        .forEach(???({ this.tellClientEmptyResult(it) }))
//    }
//
//    private fun initWorker(t: Tuple<Int, Int>): Result<Executable> {
//        return Result.success({ Worker("Worker " + t._2, Type.SERIAL).tell(t._1, self()) })
//    }
//
//    private fun initWorkers(lst: List<Executable>) {
//        lst.forEach(???({ Executable.exec() }))
//    }
//
//    private fun tellClientEmptyResult(string: String) {
//        client.tell(Result.failure(string + " caused by empty input list."))
//    }
//
//    fun onReceive(message: Int?, sender: Result<Actor<Int>>) {
//        getContext().become(Behavior(workList, resultList))
//    }
//
//    internal inner class Behavior private constructor(private val workList: List<Int>, private val resultList: List<Int>) : MessageProcessor<Int> {
//
//        fun process(i: Int?, sender: Result<Actor<Int>>) {
//            managerFunction.apply(this@Manager).apply(this@Behavior).apply(i)
//            sender.forEach { a ->
//                workList.headOption().forEachOrFail({ x -> a.tell(x, self()) })
//                        .forEach { x -> a.shutdown() }
//            }
//        }
//    }
//}
