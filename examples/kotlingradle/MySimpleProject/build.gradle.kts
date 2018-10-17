val kotlintestVersion = "3.1.10"
val logbackVersion = "1.2.3"
val slf4jVersion = "1.7.25"

plugins {
    application
    kotlin("jvm") version "1.2.71"
}

application {
    mainClassName = "com.mydomain.mysimpleproject.MainKt"
}

repositories {
    jcenter()
}

dependencies {
    compile(kotlin("stdlib"))
    testCompile("io.kotlintest:kotlintest-runner-junit5:$kotlintestVersion")
    testRuntime("org.slf4j:slf4j-nop:$slf4jVersion")
}
