plugins {
    base
    kotlin("jvm") version "1.3.61"
}

allprojects {

    group = "com.fpinkotlin"

    version = "1.0-SNAPSHOT"

    repositories {
        jcenter()
        mavenCentral()
    }
    
    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

subprojects {
    val kotlinVersion: String by project
    val projectGroup: String by project
    val projectVersion: String by project

    group = projectGroup
    version = projectVersion

    buildscript {

        repositories {
            jcenter()
            mavenCentral()
        }

        dependencies {
            classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        }
    }
}