import com.android.build.gradle.internal.tasks.AarMetadataReader.Companion.load
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    kotlin("kapt")
}

android {
    namespace = "com.nathaniel.carryapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.nathaniel.carryapp"
        minSdk = 24
        targetSdk = 36

        // Build version name and code
        val major = (project.findProperty("VERSION_MAJOR") ?: "1") as String
        val minor = (project.findProperty("VERSION_MINOR") ?: "0") as String
        val patch = (project.findProperty("VERSION_PATCH") ?: "0") as String

        versionName = "$major.$minor.$patch"
        versionCode = major.toInt() * 10000 + minor.toInt() * 100 + patch.toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}


/**
 * Bump PATCH: 1.0.0 -> 1.0.1
 */
tasks.register("bumpPatch") {
    group = "versioning"
    description = "Increase PATCH version by 1"

    doLast {
        val propsFile = rootProject.file("gradle.properties")
        val props = Properties().apply { load(propsFile.inputStream()) }

        val major = props["VERSION_MAJOR"].toString().toInt()
        val minor = props["VERSION_MINOR"].toString().toInt()
        val patch = props["VERSION_PATCH"].toString().toInt() + 1

        props["VERSION_PATCH"] = patch.toString()
        props.store(propsFile.outputStream(), null)

        println("✔ PATCH bumped → $major.$minor.$patch")
    }
}

/**
 * Optional: Bump MINOR (resets patch = 0)
 */
tasks.register("bumpMinor") {
    group = "versioning"
    description = "Increase MINOR version by 1 and reset PATCH to 0"

    doLast {
        val propsFile = rootProject.file("gradle.properties")
        val props = Properties().apply { load(propsFile.inputStream()) }

        val major = props["VERSION_MAJOR"].toString().toInt()
        val minor = props["VERSION_MINOR"].toString().toInt() + 1

        props["VERSION_MINOR"] = minor.toString()
        props["VERSION_PATCH"] = "0"
        props.store(propsFile.outputStream(), null)

        println("✔ MINOR bumped → $major.$minor.0")
    }
}

/**
 * Optional: Bump MAJOR (resets minor + patch)
 */
tasks.register("bumpMajor") {
    group = "versioning"
    description = "Increase MAJOR version by 1 and reset MINOR & PATCH to 0"

    doLast {
        val propsFile = rootProject.file("gradle.properties")
        val props = Properties().apply { load(propsFile.inputStream()) }

        val major = props["VERSION_MAJOR"].toString().toInt() + 1

        props["VERSION_MAJOR"] = major.toString()
        props["VERSION_MINOR"] = "0"
        props["VERSION_PATCH"] = "0"
        props.store(propsFile.outputStream(), null)

        println("✔ MAJOR bumped → $major.0.0")
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.play.services.location)
    //implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // customize dependencies
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.compose.ui)
    implementation(libs.material3)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.navigation.compose)
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicators)
    implementation(libs.compose.foundation)
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.datastore.preferences)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    kapt(libs.room.compiler)
    implementation(libs.timber)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.lottie.compose)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.coil.compose)
    implementation(libs.play.core.update)
    implementation(libs.play.core.update.ktx)
    implementation(libs.google.accompanist.navigation.animation)
    implementation(libs.google.maps.compose)
    implementation(libs.google.play.services.maps)
}