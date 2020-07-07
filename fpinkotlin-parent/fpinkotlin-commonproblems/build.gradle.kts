plugins {
    kotlin("jvm")
}

dependencies {
    compile(kotlin("stdlib"))
    compile(project(":fpinkotlin-common"))
    compile("org.jdom:jdom2:2.0.6")
    testCompile(project(":fpinkotlin-common-test"))
}