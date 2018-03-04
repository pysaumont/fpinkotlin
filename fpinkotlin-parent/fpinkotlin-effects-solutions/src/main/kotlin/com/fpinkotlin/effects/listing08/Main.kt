package com.fpinkotlin.effects.listing08


fun main(args: Array<String>) {

//    IO.forever<Unit, Unit>(Console.printLine("Hi..."))()

    /*
    val program = IO.forever<Unit, Unit>(IO.unit("Hi again!")
                                 .flatMap { Console.printLine(it) } )
    program()
    */

    IO.repeat(100_000, Console.printLine("Hi..."))()

//    IO.doWhile(Console.printLine("Hi...")) { IO.unit(true) }()
}
