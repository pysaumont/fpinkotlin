package com.fpinkotlin.actors.coroutines.example29

//import com.fpinkotlin.common.*
//import com.fpinkotlin.common.List
//import kotlinx.coroutines.experimental.CompletableDeferred
//import kotlinx.coroutines.experimental.channels.ActorScope
//import kotlinx.coroutines.experimental.channels.SendChannel
//import kotlinx.coroutines.experimental.channels.actor
//import net.bytebuddy.implementation.FixedValue.self
//
//sealed class Message
//class Start(val value: List<Int>): Message()
//class Result(val value: Int, client: SendChannel<Int>): Message()
//class FinalResult(val value: List<Int>) : Message()
//
//private const val listLength = 2000
//private const val workers = 8
//private val rnd = java.util.Random(0)
//private val testList =
//        range(0, listLength).map { rnd.nextInt(35) }
//
//fun worker(client: SendChannel<Pair<Int, Int>>): SendChannel<Pair<Int, Int>> = actor {
//    for (msg in channel) { // iterate over incoming messages
//        client.send(Pair(fibonacci(msg.first), msg.second))
//    }
//}
//
//fun manager(client: SendChannel<List<Int>>, list: List<Int>, workers: List<SendChannel<Pair<Int, Int>>>): SendChannel<Message> = actor {
//    val splitLists = list.zipWithPosition().splitAt(this.workers)
//    val initial = splitLists.first
//    val workList = splitLists.second
//    val resultHeap = Heap(Comparator { p1: Pair<Int, Int>, p2: Pair<Int, Int> -> p1.second.compareTo(p2.second) })
//    val limit = list.length - 1
//    for (msg in channel) {
//        when (msg) {
//            is FinalResult -> client.send(msg.value)
//            is Result -> {
//            }
//        }
//    }
//}
//
//class Manager(id: String,
//              list: List<Int>,
//              private val client: SendChannel<List<Int>>,
//              private val workers: Int) : SendChannel<Message>  {
//
//    private val initial: List<Pair<Int, Int>>
//    private val workList: List<Pair<Int, Int>>
//    private val resultHeap: Heap<Pair<Int, Int>>
//    private val managerFunction: (Manager) -> (Behavior) -> (Pair<Int, Int>) -> Unit
//    private val limit: Int
//
//    init {
//        val splitLists = list.zipWithPosition().splitAt(this.workers)
//        this.initial = splitLists.first
//        this.workList = splitLists.second
//        this.resultHeap = Heap(Comparator { p1: Pair<Int, Int>, p2: Pair<Int, Int> -> p1.second.compareTo(p2.second) })
//        this.limit = list.length - 1
//
//        managerFunction = { manager ->
//            { behavior ->
//                { p ->
//                    val result = streamResult(behavior.resultHeap + p,
//                            behavior.expected, List()) // <1>
//                    result.third.forEach { client.tell(it) }
//                    if (result.second > limit) {
//                        this.client.tell(-1) // <2>
//                    } else {
//                        manager.actorContext
//                                .become(Behavior(behavior.workList
//                                        .tailSafe()
//                                        .getOrElse(List()), result.first, result.second))
//                    }
//                }
//            }
//        }
//    }
//
//    private fun streamResult(result: Heap<Pair<Int, Int>>,
//                             expected: Int, list: List<Int>):
//            Triple<Heap<Pair<Int, Int>>, Int, List<Int>> {
//        val triple = Triple(result, expected, list)
//        val temp = result.head
//                .flatMap({ head ->
//                    result.tail().map { tail ->
//                        if (head.second == expected)
//                            streamResult(tail, expected + 1, list.cons(head.first))
//                        else
//                            triple
//                    }
//                })
//        return temp.getOrElse(triple)
//    }
//
//    fun start() {
//        onReceive(Pair(0, 0), self())
//        sequence(initial.map { this.initWorker(it) })
//                .forEach({ this.initWorkers(it) },
//                        { client.tell(-1) })
//    }
//
//    private fun initWorker(t: Pair<Int, Int>): Result<() -> Unit> =
//            Result({ Worker("Worker " + t.second).tell(Pair(t.first, t.second), self()) })
//
//    private fun initWorkers(lst: List<() -> Unit>) {
//        lst.forEach { it() }
//    }
//
//    override fun onReceive(message: Pair<Int, Int>,
//                           sender: Result<Actor<Pair<Int, Int>>>) {
//        actorContext.become(Behavior(workList, resultHeap, 0))
//    }
//
//    internal inner class Behavior
//    internal constructor(internal val workList: List<Pair<Int, Int>>,
//                         internal val resultHeap: Heap<Pair<Int, Int>>,
//                         internal val expected: Int) :
//            MessageProcessor<Pair<Int, Int>> {
//
//        override fun process(message: Pair<Int, Int>,
//                             sender: Result<Actor<Pair<Int, Int>>>) {
//            managerFunction(this@Manager)(this@Behavior)(message)
//            sender.forEach({ a: Actor<Pair<Int, Int>> ->
//                workList.headSafe().forEach({ a.tell(it, self()) }) { a.shutdown() }
//            })
//        }
//    }
//}
//
//fun receiver(client: SendChannel<List<Int>>): SendChannel<Int> = actor {
//    var resultList: List<Int> = List()
//    for (msg in channel) {
//        when (msg) {
//            -1 -> client.send(resultList)
//            else -> resultList += msg
//        }
//    }
//}
//
//fun main(args: Array<String>) {
//    val startTime = System.currentTimeMillis()
//    val client = actor<List<Int>> {
//        for (msg in channel) {
//            println("Total time: " + (System.currentTimeMillis() - startTime))
//            println("Input: ${testList.splitAt(40).first}")
//            println("Result: ${msg.splitAt(40).first}")
//        }
//    }
//
//    val receiver = receiver(client)
//    val manager = manager(testList, receiver, workers)
//    manager.start()
//}
//
//private fun fibonacci(number: Int): Int {
//    tailrec fun fibonacci(acc1: Int, acc2: Int, x: Int): Int = when (x) {
//        0 -> 1
//        1 -> acc1 + acc2
//        else -> fibonacci(acc2, acc1 + acc2, x - 1)
//    }
//    return fibonacci(0, 1, number)
//}
//
//private fun slowFibonacci(number: Int): Int {
//    return when (number) {
//        0    -> 1
//        1    -> 1
//        else -> slowFibonacci(number - 1) + slowFibonacci(number - 2)
//    }
//}