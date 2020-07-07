ext["kotlintestVersion"] = "3.1.10"
ext["logbackVersion"] = "1.2.3"
ext["slf4jVersion"] = "1.7.25"

plugins {
    base
    kotlin("jvm") version "1.3.10" //apply false
}

allprojects {

    group = "com.mydomain.mymultipleproject"

    version = "1.0-SNAPSHOT"

    repositories {
        jcenter()
        mavenCentral()
    }
}
