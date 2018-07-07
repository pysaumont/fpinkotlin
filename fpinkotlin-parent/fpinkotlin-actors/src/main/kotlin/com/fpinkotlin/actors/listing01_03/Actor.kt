package com.fpinkotlin.actors.listing01_03

import com.fpinkotlin.common.Result
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.RejectedExecutionException
import java.util.concurrent.ThreadFactory


interface ActorContext<T> {

    fun behavior(): MessageProcessor<T> // <2>

    fun become(behavior: MessageProcessor<T>) // <1>
}

interface MessageProcessor<T> {

    fun process(message: T, sender: Result<Actor<T>>) // <3>
}


interface Actor<T> {

    val context: ActorContext<T> // <3>

    fun self(): Result<Actor<T>> = Result(this) // <2>

    fun tell(message: T, sender: Result<Actor<T>> = self()) // <4>

    fun shutdown() // <5>

    fun tell(message: T, sender: Actor<T>) = tell(message, Result(sender)) // <6>

    companion object {

        fun <T> noSender(): Result<Actor<T>> = Result() // <1>
    }
}

class DaemonThreadFactory : ThreadFactory {

    override fun newThread(runnableTask: Runnable): Thread {
        val thread = Executors.defaultThreadFactory().newThread(runnableTask)
        thread.isDaemon = true
        return thread
    }
}

abstract class AbstractActor<T>(protected val id: String) : Actor<T> {

    override val context: ActorContext<T> = object: ActorContext<T> { // <2>

        var behavior: MessageProcessor<T> = object: MessageProcessor<T> { // <3>

            override fun process(message: T, sender: Result<Actor<T>>) {
                onReceive(message, sender)
            }
        }

        @Synchronized
        override fun become(behavior: MessageProcessor<T>) { // <4>
            this.behavior = behavior
        }

        override fun behavior() = behavior
    }

    private val executor: ExecutorService =
            Executors.newSingleThreadExecutor(DaemonThreadFactory()) // <1>

    abstract fun onReceive(message: T, sender: Result<Actor<T>>)  // <5>

    override fun self(): Result<Actor<T>> {
        return Result(this)
    }

    override fun shutdown() {
        this.executor.shutdown()
    }

    @Synchronized
    override fun tell(message: T, sender: Result<Actor<T>>) { // <6>
        executor.execute {
            try {
                context.behavior().process(message, sender) // <7>
            } catch (e: RejectedExecutionException) {
                /*
                 * This is probably normal and means all pending tasks
                 * were canceled because the actor was stopped.
                 */
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
    }
}
