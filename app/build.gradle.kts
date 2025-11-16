import org.gradle.api.tasks.testing.Test

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.projeto.sistema_fila"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.projeto.sistema_fila"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

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

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)

    // -------- JUNIT 5 --------
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    testImplementation("org.junit.platform:junit-platform-launcher:1.10.0")

    // Testes Android
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

// -------- ATIVAR JUNIT 5 NO ANDROID --------
tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}




