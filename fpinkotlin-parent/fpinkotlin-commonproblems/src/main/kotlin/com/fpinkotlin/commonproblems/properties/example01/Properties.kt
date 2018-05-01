package com.fpinkotlin.commonproblems.properties.example01

import com.fpinkotlin.common.Result
import java.lang.invoke.MethodHandles
import java.util.*


class PropertyReader(configFileName: String) {

    internal val properties: Result<Properties> = // <1>
        Result.of { // <2>
            MethodHandles.lookup().lookupClass() // <3>
                .getResourceAsStream(configFileName) // <4>
                .use { inputStream -> // <5>
                    Properties().let {
                        it.load(inputStream) // <6>
                        it
                    }
                }
        }
}

fun main(args: Array<String>) {
    val propertyReader = PropertyReader("/config.properties") // <7>
    propertyReader.properties.forEach(onSuccess = { println(it) }, onFailure = { println(it) })
}