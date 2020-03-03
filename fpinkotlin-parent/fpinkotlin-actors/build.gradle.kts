plugins {
    kotlin("jvm")
}

dependencies {
    compile(kotlin("stdlib"))
    compile(project(":fpinkotlin-common"))
    compile("org.jetbrains.kotlinx:kotlinx-coroutines-core:${project.rootProject.ext["kotlinCoroutinesVersion"]}")
    testCompile(project(":fpinkotlin-common-test"))
}