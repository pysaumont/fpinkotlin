package com.fpinkotlin.commonproblems.xml.step2

import com.fpinkotlin.common.List
import com.fpinkotlin.common.Result
import org.jdom2.Element
import org.jdom2.JDOMException
import org.jdom2.input.SAXBuilder
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.StringReader


fun readFile2String(path: String): Result<String> =
    try {
        FileInputStream(File(path)).use {
            it.bufferedReader().use {
                Result(it.readText())
            }
        }
    } catch (e: IOException) {
        Result.failure("IOException while reading file $path: ${e.message}")
    } catch (e: Exception) {
        Result.failure("Unexpected error while reading file $path: ${e.message}")
    }

fun readDocument(rootElementName: String, stringDoc: String): Result<List<Element>> =
    SAXBuilder().let { builder ->
        try {
            val document = builder.build(StringReader(stringDoc))
            val rootElement = document.rootElement
            Result(List(*rootElement.getChildren(rootElementName).toTypedArray()))
        } catch (io: IOException) {
            Result.failure("Invalid root element name '$rootElementName' or XML data $stringDoc: ${io.message}")
        } catch (jde: JDOMException) {
            Result.failure("Invalid root element name '$rootElementName' or XML data $stringDoc: ${jde.message}")
        } catch (e: Exception) {
            Result.failure("Unexpected error while reading XML data $stringDoc: ${e.message}")
        }
    }

fun toStringList(list: List<Element>, format: String): List<String> =
        list.map { e -> processElement(e, format) }


fun processElement(element: Element, format: String): String =
        String.format(format, element.getChildText("firstName"),
                element.getChildText("lastName"),
                element.getChildText("email"),
                element.getChildText("salary"))

fun <A> processList(list: List<A>) = list.forEach(::println)

fun getRootElementName(): Result<String> = Result.of { "staff" } // Simulating a computation that may fail.

fun getXmlFilePath(): Result<String> = Result.of { "path/to/file.xml" } // <- adjust path

const val format = "First Name : %s\n" +
        "\tLast Name : %s\n" +
        "\tEmail : %s\n" +
        "\tSalary : %s"

fun main(args: Array<String>) {
    val path = getXmlFilePath()
    val rDoc = path.flatMap(::readFile2String)
    val rRoot = getRootElementName()
    val result = rDoc.flatMap { doc ->
        rRoot.flatMap { rootElementName ->
            readDocument(rootElementName, doc)
        }.map { list ->
            toStringList(list, format)
        }
    }
    result.forEach(onSuccess = { processList(it) }, onFailure = { it.printStackTrace() })
}
