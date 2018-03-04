package com.fpinkotlin.effects.exercise09

import com.fpinkotlin.common.Lazy
import com.fpinkotlin.common.List
import com.fpinkotlin.common.List.Companion.cons
import com.fpinkotlin.common.Stream
import com.fpinkotlin.common.getOrElse


class IO<out A>(private val f: () -> A) {

    operator fun invoke() = f()

    fun <B> map (g: (A) -> B): IO<B> = IO {
        g(this())
    }

    fun <B> flatMap (g: (A) -> IO<B>): IO<B> = IO {
        g(this())()
    }

    companion object {

        val empty: IO<Unit> = IO { }

        operator fun <A> invoke(a: A): IO<A> = IO { a }

        fun <A> repeat(n: Int, io: IO<A> ): IO<List<A>> =
                Stream.fill(n, Lazy { io })
                        .foldRight( Lazy { IO { List<A>() } }, { ioa ->
                            { sioLa ->
                                map2(ioa, sioLa()) { a ->
                                    { la: List<A> -> cons(a, la) }
                                }
                            }
                        })

        fun <A, B, C> map2(ioa: IO<A>, iob: IO<B>, f: (A) ->  (B) -> C): IO<C> =
                ioa.flatMap { a ->
                    iob.map { b ->
                        f(a)(b)
                    }
                }

        fun <A, B> changeTo(a: IO<A>, b: B): IO<B> = a.map { b }

        fun <A> condition(b: Boolean, sIoa: Lazy<IO<A>>): IO<Boolean> = when {
            b -> changeTo(sIoa(), true)
            else -> IO(false)
        }

        fun <A> doWhile(ioa: IO<A>, f: (A) -> IO<Boolean>): IO<Unit> {
            return ioa.flatMap { f(it) }
                    .flatMap({ ok ->
                        when {
                            ok -> doWhile(ioa, f)
                            else -> empty
                        }
                    })
        }

        fun <A> sequence(stream: Stream<IO<A>>): IO<Unit> {
            return forEach(stream) { skip(it) }
        }

        fun <A> skip(a: IO<A>): IO<Unit> {
            return changeTo(a, Unit)
        }

        fun <A, B> foldM_(s: Stream<A>, z: B, f:(B) -> (A) -> IO<B>): IO<Unit> {
            return skip(foldM(s, z, f))
        }

        fun <A, B> foldM(s: Stream<A>, z: B, f: (B) -> (A) -> IO<B>): IO<B> {
            return if (s.isEmpty())
                IO(z)
            else
                f(z)(s.head().getOrElse { throw IllegalStateException() })
                        .flatMap({ zz -> foldM(s.tail()
                                .getOrElse { throw IllegalStateException() }, zz, f) })
        }

        fun <A> forEach(s: Stream<A>, f: (A) -> IO<Unit>): IO<Unit> {
            return foldM_(s, Unit, { { t: A -> skip(f(t)) } })
        }

        fun <A> sequence(vararg array: IO<A>): IO<Unit> {
            return sequence(Stream(array))
        }

        fun <A, B> forever(ioa: IO<A>): IO<B> = TODO("forever")

    }
}

fun main(args: Array<String>) {
    IO.forever<Unit, String>(Console.println("Hi again!"))()
}


fun <A, B> forever(ioa: IO<A>): IO<B> {
    return ioa.flatMap { { ioa.flatMap { { forever<A, B>(ioa) }() } }() }
}


