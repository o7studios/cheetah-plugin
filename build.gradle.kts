import com.github.spotbugs.snom.SpotBugsPlugin
import studio.o7.remora.RemoraPlugin

plugins {
    id("studio.o7.remora") version "0.3.6"
    id("com.github.spotbugs") version "6.4.2"
}

allprojects {
    apply<RemoraPlugin>()
    apply<SpotBugsPlugin>()

    group = "studio.o7"

    repositories {
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.opencollab.dev/main/")
    }

    spotbugs {
        toolVersion.set("4.9.0")
        effort.set(com.github.spotbugs.snom.Effort.MAX)
        reportLevel.set(com.github.spotbugs.snom.Confidence.DEFAULT)
    }
}