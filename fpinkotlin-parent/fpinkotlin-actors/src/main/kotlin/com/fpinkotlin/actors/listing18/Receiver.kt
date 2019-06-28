package com.fpinkotlin.actors.listing18


import com.fpinkotlin.common.Result

class Receiver(id: String, private val client: Actor<List<Int>>) : AbstractActor<Int>(id) {

    private val receiverFunction: (Receiver) -> (Behavior) -> (Int) -> Unit

    init {
        receiverFunction = { receiver ->
            { behavior ->
                { i ->
                    if (i == -1) {
                        this.client.tell(behavior.resultList.reversed())
                        shutdown()
                    } else {
                        receiver.context.become(Behavior(behavior.resultList + i))
                    }
                }
            }
        }
    }

    override fun onReceive(i: Int, sender: Result<Actor<Int>>) {
        context.become(Behavior(listOf(i)))
    }

    internal inner class Behavior internal constructor(
        internal val resultList: List<Int>) : MessageProcessor<Int> {

        override fun process(i: Int, sender: Result<Actor<Int>>) {
            receiverFunction(this@Receiver)(this@Behavior)(i)
        }
    }
}
