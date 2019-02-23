
plugins {
    application
    kotlin("jvm")
}

dependencies {
    compile(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.0.1")
    testCompile("io.kotlintest:kotlintest-runner-junit5:${project.rootProject.ext["kotlintestVersion"]}")
    testRuntime("org.slf4j:slf4j-nop:${project.rootProject.ext["slf4jVersion"]}")
//    testRuntime("ch.qos.logback:logback-classic:${project.rootProject.ext["logbackVersion"]}")
}