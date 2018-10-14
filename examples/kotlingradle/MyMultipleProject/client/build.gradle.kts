
plugins {
    application
    kotlin("jvm")
}

application {
    mainClassName = "com.mydomain.mymultipleproject.client.main.ClientKt"
}

dependencies {
    compile(kotlin("stdlib"))
    compile(project(":common"))
}