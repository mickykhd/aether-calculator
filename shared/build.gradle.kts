import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    jvmToolchain(17)

    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    jvm("desktop")

    // Apple targets are only buildable from a macOS host; on other hosts they stay unconfigured
    // so the project still configures and builds for Android/Desktop.
    if (System.getProperty("os.name").startsWith("Mac")) {
        iosX64()
        iosArm64()
        iosSimulatorArm64()
    }

    sourceSets {
        commonMain.dependencies {
            // Exposed as `api` so the Android and Desktop apps compile against the same
            // Compose runtime and can call the shared composables directly.
            api(compose.runtime)
            api(compose.foundation)
            api(compose.animation)
            api(compose.material3)
            api(compose.ui)
            api(compose.components.resources)
            api(compose.materialIconsExtended)
            // Gives the shared UI a lifecycle to observe, so the calculation log can be
            // scoped to the session the user is in.
            implementation(libs.androidx.lifecycle.runtime.compose)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

android {
    namespace = "com.aether.calculator.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.aether.calculator.resources"
}
