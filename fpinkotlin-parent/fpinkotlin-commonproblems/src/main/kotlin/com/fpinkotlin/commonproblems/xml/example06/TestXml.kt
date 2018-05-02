package com.fpinkotlin.commonproblems.xml.example06

import com.fpinkotlin.common.List
import com.fpinkotlin.common.Result
import org.jdom2.Element


fun <A> processList(list: List<A>) = list.forEach(::println)

fun getRootElementName(): ElementName = ElementName("staff") // Simulating a computation that may fail.

fun getXmlFilePath(): FilePath = FilePath("/path/to/file.xml") // <- adjust path

const val format = "First Name : %s\n" +
    "\tLast Name : %s\n" +
    "\tEmail : %s\n" +
    "\tSalary : %s"

val elementNames =
    List("firstName", "lastName", "email", "salary")

fun processElement(element: Element): Result<String> =
    try {
        Result(String.format(format, *elementNames.map { getChildText(element, it) }
            .toArrayList()
            .toArray())) // <2>
    } catch (e: Exception) {
        Result.failure("Exception while formatting element. " +
                           "Probable cause is a missing element name in element list $elementNames")
    }

fun getChildText(element: Element, name: String): String =
    element.getChildText(name) ?: "Element $name is not a child of ${element.name}"

fun main(args: Array<String>) {
    val program = readXmlFile(::getXmlFilePath,
                              ::getRootElementName,
                              ::processElement,
                              ::processList)
    try {
        program()
    } catch (e: Exception) {
        println("An exception occurred: ${e.message}")
    }
}
