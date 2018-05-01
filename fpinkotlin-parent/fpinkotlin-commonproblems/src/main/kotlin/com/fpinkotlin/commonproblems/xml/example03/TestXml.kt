package com.fpinkotlin.commonproblems.xml.example03

import com.fpinkotlin.common.List
import com.fpinkotlin.common.Result

fun <A> processList(list: List<A>) = list.forEach(::println)

fun getRootElementName(): Result<String> = Result.of { "staff" } // Simulating a computation that may fail.

fun getXmlFilePath(): Result<String> = Result.of { "/run/media/pysaumont/KINGSTON2/fpinkotlin/fpinkotlin/fpinkotlin-parent/fpinkotlin-commonproblems/src/main/kotlin/com/fpinkotlin/commonproblems/xml/example02/file.xml" } // <- adjust path

private val format = Pair("First Name : %s\n" +
        "\tLast Name : %s\n" +
        "\tEmail : %s\n" +
        "\tSalary : %s", List("firstName", "lastName", "email", "salary"))

fun main(args: Array<String>) {
    val program = readXmlFile( { getXmlFilePath() }, { getRootElementName() }, format, { processList(it) })
    program()
}
