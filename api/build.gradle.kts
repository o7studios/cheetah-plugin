dependencies {
    compileOnlyApi("studio.o7:octopus-plugin-api:0.1.1")
}

information {
    artifactId = "cheetah-plugin-api"
    description = "Cheetah Paper plugin api"
    url = "https://o7.studio"

    developers {
        developer {
            id = "julian-siebert"
            name = "Julian Siebert"
            email = "julian.siebert@o7.studio"
        }
        developer {
            id = "raphael-goetz"
            name = "Raphael Goetz"
            email = "raphael.goetz@o7.studio"
        }
    }

    scm {
        connection = "scm:git:git://github.com/o7studios/cheetah-plugin.git"
        developerConnection = "scm:git:git@https://github.com/o7studios/cheetah-plugin.git"
        url = "https://github.com/o7studios/cheetah-plugin"
        tag = "HEAD"
    }

    ciManagement {
        system = "GitHub Actions"
        url = "https://github.com/o7studios/cheetah-plugin/actions"
    }

    licenses {
        license {
            name = "GNU General Public License, Version 3"
            url = "https://www.gnu.org/licenses/gpl-3.0.txt"
        }
    }
}