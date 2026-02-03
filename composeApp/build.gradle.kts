import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)

            implementation(libs.koin.android)

            implementation(libs.navigation3.runtime)
            implementation(libs.navigation3.ui)
            implementation(libs.navigation3.viewmodel)
            implementation(libs.navigation3.adaptive)

            implementation(libs.ktor.client.okhttp)

            implementation(libs.androidx.security.crypto)

            implementation("io.ktor:ktor-client-android:2.3.7")
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(libs.kotlinx.serialization)
            implementation(libs.kotlinx.date.time)

            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.navigation3)

            implementation(libs.bundles.ktor)

            implementation("io.github.jan-tennert.supabase:supabase-kt:0.9.4")
            implementation("io.github.jan-tennert.supabase:gotrue-kt:0.9.4")
            implementation("io.github.jan-tennert.supabase:postgrest-kt:0.9.4")
            implementation(project.dependencies.platform("io.github.jan-tennert.supabase:bom:2.5.0"))
            implementation("io.ktor:ktor-client-core:2.3.7")
//            implementation("io.ktor:ktor-client-js:2.3.12")
//
//            implementation(libs.supabase.core)
//            implementation(project.dependencies.platform(libs.supabase.bom))
//            implementation(libs.supabase.postgrest)
//            implementation(libs.supabase.auth)
//            implementation(libs.supabase.storage)s
        }
        nativeMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
            implementation("io.ktor:ktor-client-darwin:2.3.7")
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.brankogeorgiev"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.brankogeorgiev"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        val apiKey = project.findProperty("VITE_SUPABASE_PUBLISHABLE_KEY") as String? ?: ""
        val baseUrl = project.findProperty("VITE_SUPABASE_URL") as String? ?: ""

        buildConfigField("String", "API_KEY", "\"$apiKey\"")
        buildConfigField("String", "API_BASE_URL", "\"$baseUrl\"")
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    debugImplementation(libs.compose.uiTooling)
}
