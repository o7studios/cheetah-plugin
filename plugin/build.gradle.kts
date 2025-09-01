dependencies {
    implementation(project(":api"))

    compileOnly(libs.paper)
    compileOnly(libs.octopus)
    compileOnly(libs.floodgate)
}