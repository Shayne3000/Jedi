@file:Suppress("UnstableApiUsage")

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlinAndroid)
    kotlin("kapt")
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.senijoshua.jedi"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.senijoshua.jedi"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.senijoshua.jedi.util.HiltTestRunner"

        vectorDrawables {
            useSupportLibrary = true
        }

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true"
                )
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        getByName("debug") {
            isDebuggable = true
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        // TODO Update the Kotlin version and Use the compose compiler
        kotlinCompilerExtensionVersion = "1.4.6"
    }

    hilt {
        enableAggregatingTask = true
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(libs.core.ktx)
    implementation(platform(libs.kotlin.bom))
    implementation(libs.activity.compose)
    // Lifecycle compiler
    kapt(libs.lifecycle.compiler)
    // navigation
    implementation(libs.navigation)
    // Hilt
    implementation(libs.hilt)
    implementation(libs.hilt.navigation.compose)
    kapt(libs.hilt.compiler)
    // Lifecycle utilities for Compose
    implementation(libs.lifecycle.compose)
    // Compose UI
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.compose.constraintlayout)
    // Room
    implementation(libs.room)
    kapt(libs.room.compiler)
    // Kotlin Extensions and Coroutines support for Room
    implementation(libs.room.ktx)
    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi.converter)
    // Moshi
    implementation(libs.moshi.kotlin)
    kapt(libs.moshi.codegen)
    // OkHttp3
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    // Kotlin Coroutines (might contain Flow)
    implementation(libs.kotlin.coroutines)
    // Local test dependencies
    testImplementation(libs.room.testing)
    testImplementation(libs.junit)
    testImplementation(libs.kotlin.coroutines.test)
    // Instrumented test dependencies
    androidTestImplementation(libs.navigation.testing)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.test.runner)
    kaptAndroidTest(libs.hilt.compiler)
    androidTestImplementation(libs.compose.ui.test.junit4)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)
}
