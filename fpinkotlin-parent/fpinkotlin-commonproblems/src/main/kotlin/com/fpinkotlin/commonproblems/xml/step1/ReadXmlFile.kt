package com.fpinkotlin.commonproblems.xml.step1

import org.jdom2.JDOMException
import org.jdom2.input.SAXBuilder
import java.io.File
import java.io.IOException


/**
 * Not testable, throws exceptions.
 */
fun main(args: Array<String>) {

    val builder = SAXBuilder()
    val xmlFile = File("/path/to/file.xml") // adjust path

    try {
        val document = builder.build(xmlFile)
        val rootNode = document.rootElement
        val list = rootNode.getChildren("staff")

        list.forEach {
            println("First Name: ${it.getChildText("firstName")}")
            println("\tLast Name: ${it.getChildText("lastName")}")
            println("\tEmail: ${it.getChildText("email")}")
            println("\tSalary: ${it.getChildText("salary")}")
        }
    } catch (io: IOException) {
        println(io.message)
    } catch (e: JDOMException) {
        println(e.message)
    }
}
