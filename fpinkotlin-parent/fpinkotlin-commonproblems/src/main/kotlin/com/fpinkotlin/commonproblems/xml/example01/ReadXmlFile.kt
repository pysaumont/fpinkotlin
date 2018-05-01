package com.fpinkotlin.commonproblems.xml.example01

import org.jdom2.JDOMException
import org.jdom2.input.SAXBuilder
import java.io.File
import java.io.IOException


/**
 * Not testable, throws exceptions.
 */
fun main(args: Array<String>) {

    val builder = SAXBuilder()
//    val xmlFile = File("/path/to/file.xml") // Fix the path
    val xmlFile = File("/media/KINGSTON2/fpinkotlin/fpinkotlin/fpinkotlin-parent/fpinkotlin-commonproblems/src/main/kotlin/com/fpinkotlin/commonproblems/xml/example01/file.xml") // Fix the path

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
