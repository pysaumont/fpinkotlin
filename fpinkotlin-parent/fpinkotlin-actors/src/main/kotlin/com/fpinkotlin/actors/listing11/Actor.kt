package com.fpinkotlin.actors.listing11

import com.fpinkotlin.common.Result
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.RejectedExecutionException
import java.util.concurrent.ThreadFactory


interface ActorContext<T> {

    fun behavior(): MessageProcessor<T>

    fun become(behavior: MessageProcessor<T>)
}

interface MessageProcessor<T> {

    fun process(message: T, sender: Result<Actor<T>>)
}


interface Actor<T> {

    val context: ActorContext<T>

    fun self(): Result<Actor<T>> = Result(this)

    fun tell(message: T, sender: Result<Actor<T>> = self())

    fun shutdown()

    fun tell(message: T, sender: Actor<T>) = tell(message, Result(sender))

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

    override val context: ActorContext<T> = object: ActorContext<T> {

        var behavior: MessageProcessor<T> = object: MessageProcessor<T> {

            override fun process(message: T, sender: Result<Actor<T>>) {
                onReceive(message, sender)
            }
        }

        @Synchronized
        override fun become(behavior: MessageProcessor<T>) {
            this.behavior = behavior
        }

        override fun behavior() = behavior
    }

    private val executor: ExecutorService =
            Executors.newSingleThreadExecutor(DaemonThreadFactory())

    abstract fun onReceive(message: T, sender: Result<Actor<T>>)

    override fun self(): Result<Actor<T>> {
        return Result(this)
    }

    override fun shutdown() {
        this.executor.shutdown()
    }

    @Synchronized
    override fun tell(message: T, sender: Result<Actor<T>>) {
        executor.execute {
            try {
                context.behavior().process(message, sender)
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
