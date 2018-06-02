package com.fpinkotlin.commonproblems.xml.step3

import com.fpinkotlin.common.List
import com.fpinkotlin.common.Result

fun <A> processList(list: List<A>) = list.forEach(::println)

fun getRootElementName(): Result<String> = Result.of { "staff" } // Simulating a computation that may fail.

fun getXmlFilePath(): Result<String> = Result.of { "/path/to/file.xml" } // <- adjust path

private val format = Pair("First Name : %s\n" +
        "\tLast Name : %s\n" +
        "\tEmail : %s\n" +
        "\tSalary : %s", List("firstName", "lastName", "email", "salary"))

fun main(args: Array<String>) {
    val program = readXmlFile(::getXmlFilePath,
                              ::getRootElementName,
                              format,
                              ::processList)
    program()
}
