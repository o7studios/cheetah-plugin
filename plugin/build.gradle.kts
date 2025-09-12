dependencies {
    implementation(project(":api"))

    implementation(libs.k8s)

    compileOnly(libs.paper)
    compileOnly(libs.octopus)
    compileOnly(libs.floodgate)
}