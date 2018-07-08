package com.fpinkotlin.effects.listing07

import com.fpinkotlin.common.List
import com.fpinkotlin.common.Result
import com.fpinkotlin.effects.listing02.Input


class ScriptReader : Input {

    constructor(commands: List<String>) : super() {
        this.commands = commands
    }

    constructor(vararg commands: String) : super() {
        this.commands = List(*commands)
    }

    private val commands: List<String>


    override fun close() {
    }

    override fun readString(): Result<Pair<String, Input>> = when {
        commands.isEmpty() -> Result.failure("Not enough entries in script")
        else -> Result(Pair(commands.headSafe().getOrElse(""),
                ScriptReader(commands.drop(1))))
    }

    override fun readInt(): Result<Pair<Int, Input>> = try {
        when {
            commands.isEmpty() -> Result.failure("Not enough entries in script")
            Integer.parseInt(commands.headSafe().getOrElse("")) >= 0 -> Result(Pair(Integer.parseInt(
                    commands.headSafe().getOrElse("")),
                    ScriptReader(commands.drop(1))))
            else -> Result()
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
