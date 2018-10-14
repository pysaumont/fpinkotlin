plugins {
    application
    kotlin("jvm")
}

application {
    mainClassName = "com.mydomain.mymultipleproject.server.main.Server"
}

dependencies {
    compile(kotlin("stdlib"))
    compile(project(":common"))
}