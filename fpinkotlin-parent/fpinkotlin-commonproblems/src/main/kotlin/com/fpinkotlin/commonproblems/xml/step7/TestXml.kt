package com.fpinkotlin.commonproblems.xml.step7

import com.fpinkotlin.common.List


fun main(args: Array<String>) {

    fun <A> processList(list: List<A>) = list.forEach(::println)

    fun getRootElementName(): ElementName = ElementName("staff") // Simulating a computation that may fail.

    fun getXmlFilePath(): FilePath = FilePath("/path/to/file.xml") // <- adjust path

    val format = "First Name : %s\n" +
            "\tLast Name : %s\n" +
            "\tEmail : %s\n" +
            "\tSalary : %s"

    val elementNames =
            List("firstName", "lastName", "email", "salary")

    val program = readXmlFile(::getXmlFilePath,
                              ::getRootElementName,
                              processElement(elementNames)(format),
                              ::processList)
    try {
        program()
    } catch (e: Exception) {
        println("An exception occurred: ${e.message}")
    }
}
