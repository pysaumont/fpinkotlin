package com.fpinkotlin.actors.listing14

import com.fpinkotlin.common.List
import com.fpinkotlin.common.Result


class Receiver(id: String,
               private val client: Actor<List<Int>>) : // <2>
                                AbstractActor<Int>(id) {  // <1>

    private val receiverFunction: (Receiver) -> (Behavior) -> (Int) -> Unit

    init {
        receiverFunction = { receiver ->  // <3>
            { behavior ->
                { i ->
                    if (i == -1) {
                        this.client.tell(behavior.resultList.reverse())
                        shutdown()
                    } else {
                        receiver.actorContext
                            .become(Behavior(behavior.resultList.cons(i))) // <4>
                    }
                }
            }
        }
    }

    override fun onReceive(i: Int, sender: Result<Actor<Int>>) {
        actorContext.become(Behavior(List(i))) // <5>
    }

    internal inner class Behavior internal constructor(
        internal val resultList: List<Int>) : // <6>
                          MessageProcessor<Int> {

        override fun process(i: Int, sender: Result<Actor<Int>>) {
            receiverFunction(this@Receiver)(this@Behavior)(i)
        }
    }
}

