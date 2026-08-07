plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.harish.floatiq"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.harish.floatiq"
        minSdk = 26
        targetSdk = 36
        versionCode = 22
        versionName = "1.16"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
//        release {
//            isMinifyEnabled = false
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
//            )
//        }
//        getByName("debug") {
//            // Google's official Test ID for local development
//            buildConfigField("String", "AD_MOB_NATIVE_ID", "\"ca-app-pub-3940256099942544/2247696110\"")
//        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"

            )
            // Your real production AdMob ID for the Play Store version
//            buildConfigField("String", "AD_MOB_NATIVE_ID", "\"ca-app-pub-6162249995378244/7659026994\"")
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
        // CRITICAL STEP: Tells Gradle to generate the BuildConfig file so AdConstants can read it
//        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)

    ksp(libs.androidx.room.compiler)
    implementation("com.google.android.play:review-ktx:2.0.2")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("com.airbnb.android:lottie-compose:6.4.0")
//    implementation("com.google.mlkit:translate:17.0.2")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
//    implementation("com.google.android.gms:play-services-ads:24.4.0")
    implementation("com.google.guava:guava:33.2.1-android")
    // CameraX
    implementation("androidx.camera:camera-core:1.4.2")
    implementation("androidx.camera:camera-camera2:1.4.2")
    implementation("androidx.camera:camera-lifecycle:1.4.2")
    implementation("androidx.camera:camera-view:1.4.2")
// ML Kit OCR

    implementation("com.google.mlkit:text-recognition:16.0.1")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.google.accompanist:accompanist-permissions:0.37.3")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}