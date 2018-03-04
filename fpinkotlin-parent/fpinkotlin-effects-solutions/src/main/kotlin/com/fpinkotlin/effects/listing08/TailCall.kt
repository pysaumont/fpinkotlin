package com.fpinkotlin.effects.listing08


//abstract class TailCall<T> internal constructor() {
//
//    abstract val isSuspend: Boolean
//
//    abstract fun resume(): TailCall<T>
//
//    abstract fun eval(): T
//
//    internal class Return<T> internal constructor(private val t: T): TailCall<T>() {
//
//        override val isSuspend: Boolean
//            get() = false
//
//        override fun eval(): T {
//            return t
//        }
//
//        override fun resume(): TailCall<T> {
//            throw IllegalStateException("Return has no resume")
//        }
//    }
//
//    internal class Suspend<T> internal constructor(private val resume: () -> TailCall<T>): TailCall<T>() {
//
//        override val isSuspend: Boolean
//            get() = true
//
//        override fun eval(): T {
//            var tailRec: TailCall<T> = this
//            while (tailRec.isSuspend) {
//                tailRec = tailRec.resume()
//            }
//            return tailRec.eval()
//        }
//
//        override fun resume(): TailCall<T> {
//            return resume.invoke()
//        }
//    }
//
//    companion object {
//
//        internal fun <T> ret(t: T): Return<T> {
//            return Return(t)
//        }
//
//        internal fun <T> sus(s: () -> TailCall<T>): Suspend<T> {
//            return Suspend(s)
//        }
//    }
//}
