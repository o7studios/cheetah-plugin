rootProject.name = "cheetah-plugin"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("paper", "1.21.8-R0.1-SNAPSHOT")
            version("floodgate", "2.2.4-SNAPSHOT")
            version("k8s", "24.0.0-legacy")
            version("octopus", "1.0.6")

            library("paper", "io.papermc.paper", "paper-api").versionRef("paper")

            library("floodgate", "org.geysermc.floodgate", "api").versionRef("floodgate")

            library("k8s", "io.kubernetes", "client-java").versionRef("k8s")

            library("octopus", "studio.o7", "octopus-plugin-api").versionRef("octopus")
        }
    }
}

include("api")
include("plugin")