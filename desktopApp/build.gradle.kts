import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(project(":shared"))
    // Brings in the Skiko native library for the host operating system.
    implementation(compose.desktop.currentOs)
}

compose.desktop {
    application {
        mainClass = "com.aether.calculator.desktop.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Msi, TargetFormat.Dmg, TargetFormat.Deb)
            packageName = "AetherCalc"
            packageVersion = "1.0.0"
        }
    }
}
