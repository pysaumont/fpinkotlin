package com.fpinkotlin.effects.listing08

import com.fpinkotlin.common.Lazy
import com.fpinkotlin.common.Stream
import com.fpinkotlin.common.Stream.Companion.fill


sealed class IO<out A> {

    operator fun invoke(): A = invoke(this)

    operator fun invoke(io: IO<@UnsafeVariance A>): A {
        tailrec fun invokeHelper(io: IO<A>): A = when (io) { // <1>
            is Return  -> io.value // <2>
            is Suspend -> io.resume() // <3>
            else       -> {
                val ct = io as Continue<A, A> // <4>
                val sub = ct.sub
                val f = ct.f
                when (sub) {
                    is Return  -> invokeHelper(f(sub.value)) // <5>
                    is Suspend -> invokeHelper(f(sub.resume())) // <6>
                    else       -> {
                        val ct2 = sub as Continue<A, A> // <7>
                        val sub2 = ct2.sub
                        val f2 = ct2.f
                        invokeHelper(sub2.flatMap { f2(it).flatMap(f) })
                    }
                }
            }
        }
        return invokeHelper(io)
    }

    fun <B> map(f: (A) -> B): IO<B> = flatMap { Return(f(it)) } // <6>

    fun <B> flatMap(f: (A) -> IO<B>): IO<B> = Continue(this, f) as IO<B> // <7>

    class IORef<A>(private var value: A) {

        fun set(a: A): IO<A> {
            value = a
            return unit(a)
        }

        fun get(): IO<A> = unit(value)

        fun modify(f: (A) -> A): IO<A> = get().flatMap { a -> set(f(a)) }
    }

    internal class Return<out A>(val value: A): IO<A>()

    internal class Suspend<out A>(val resume: () -> A): IO<A>()

    internal class Continue<A, out B>(val sub: IO<A>,
                                      val f: (A) ->  IO<B>): IO<A>()

    companion object {

        val empty: IO<Unit> = IO.Suspend { Unit } // <2>

        internal fun <A> unit(a: A): IO<A> = IO.Suspend { a } // <8>

        fun <A, B, C> map2(ioa: IO<A>, iob: IO<B>, f: (A) -> (B) -> C): IO<C> = ioa.flatMap { t -> iob.map { u -> f(t)(u) } }

        fun <A> doWhile(iot: IO<A>, f: (A) -> IO<Boolean>): IO<Unit> = iot.flatMap { f(it) }
            .flatMap({ ok ->
                         when {
                             ok   -> doWhile(iot, f)
                             else -> empty
                         }
                     })

        fun <A> repeat(n: Int, io: IO<A>): IO<Unit> = forEach(fill(n, Lazy { io }), { skip(it) })

        fun <A, B> forever(ioa: IO<A>): IO<B> =
            { forever<A, B>(ioa) }.let { ioa.flatMap { it() } }

        fun <A, B> fold(s: Stream<A>, z: B, f: (B) -> (A) -> IO<B>): IO<B> = when {
            s.isEmpty() -> unit(z)
            else        -> f(z)(s.head().getOrElse { throw IllegalStateException() })
                .flatMap({ zz -> fold(s.tail().getOrElse { throw IllegalStateException() }, zz, f) })
        }

        fun <A, B> changeTo(a: IO<A>, b: B): IO<B> = a.map { b }

        fun <A> skip(a: IO<A>): IO<Unit> = changeTo(a, Unit)

        private fun <A, B> fold2(s: Stream<A>, z: B, f: (B) -> (A) -> IO<B>): IO<Unit> = skip(fold(s, z, f))

        internal fun <A> forEach(s: Stream<A>, f: (A) -> IO<Unit>): IO<Unit> = fold2(s, Unit, {  { a -> skip(f(a)) } })

        fun <A> sequence(stream: Stream<IO<A>>): IO<Unit> = forEach(stream) { skip(it) }

        @SafeVarargs
        internal fun <A> sequence(vararg array: IO<A>): IO<Unit> = sequence(Stream(array))

        internal fun <A> condition(b: Boolean, sIoa: () -> IO<A>): IO<Boolean> = when {
            b    -> changeTo(sIoa(), true)
            else -> unit(false)
        }

        internal fun <A> ref(a: A): IO<IORef<A>> = unit(IORef(a))
    }
}
