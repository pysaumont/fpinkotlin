plugins {
    kotlin("jvm")
}

dependencies {
    compile(kotlin("stdlib"))
    compile(project(":fpinkotlin-common"))
    compile("io.kotlintest:kotlintest-runner-junit5:${project.rootProject.ext["kotlintestVersion"]}")
    testRuntime("org.slf4j:slf4j-nop:${project.rootProject.ext["slf4jVersion"]}")
}