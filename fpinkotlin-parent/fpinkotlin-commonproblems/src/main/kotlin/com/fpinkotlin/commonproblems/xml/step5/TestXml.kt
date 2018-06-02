package com.fpinkotlin.commonproblems.xml.step5

import com.fpinkotlin.common.List
import org.jdom2.Element


fun <A> processList(list: List<A>) = list.forEach(::println)

fun getRootElementName(): ElementName = ElementName("staff") // Simulating a computation that may fail.

fun getXmlFilePath(): FilePath = FilePath("/path/to/file.xml") // <- adjust path

const val format = "First Name : %s\n" +
    "\tLast Name : %s\n" +
    "\tEmail : %s\n" +
    "\tSalary : %s"

private val elementNames =
    List("firstName", "lastName", "email", "salary")

private fun processElement(element: Element): String =
    String.format(format, *elementNames.map { element.getChildText(it) }
        .toArrayList()
        .toArray())

fun main(args: Array<String>) {
    val program = readXmlFile(::getXmlFilePath,
                              ::getRootElementName,
                              ::processElement,
                              ::processList)
    program()
}
