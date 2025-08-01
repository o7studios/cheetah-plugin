import studio.o7.remora.RemoraPlugin

plugins {
    id("studio.o7.remora") version "0.2.5"
}

allprojects {
    apply<RemoraPlugin>()

    group = "studio.o7"

    repositories {
        maven("https://repo.papermc.io/repository/maven-public/")
    }

    dependencies {
        compileOnly("io.papermc.paper:paper-api:1.21.8-R0.1-SNAPSHOT")
    }
}