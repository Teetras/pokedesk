@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id("org.jetbrains.kotlin.plugin.serialization")
}
android {
    namespace = "com.example.byprokhorenkopmis1"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.byprokhorenkopmis1"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }


    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}



dependencies{
    implementation("androidx.compose.material:material:1.0.7")
    implementation("androidx.compose.material3:material3:1.2.0-alpha02")
    implementation( "androidx.compose.material:material:1.0.4")
    implementation( "androidx.compose:compose-runtime:0.1.0-dev01")
    kapt ("androidx.compose:compose-compiler:0.1.0-dev01")
    implementation ("androidx.ui:ui-layout:0.1.0-dev01")
    implementation ("androidx.ui:ui-android-text:0.1.0-dev01")
    implementation( "androidx.ui:ui-text:0.1.0-dev01")
    implementation ("androidx.ui:ui-material:0.1.0-dev01")
    implementation("io.ktor:ktor-client-json:1.3.2-1.4-M2")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("io.ktor:ktor-client-serialization-jvm:2.3.4")
implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.6.0")
    implementation("androidx.navigation:navigation-compose:2.7.4")
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    implementation("androidx.datastore:datastore:1.0.0")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    implementation ("io.insert-koin:koin-androidx-compose:3.4.3")

    implementation ("androidx.work:work-runtime-ktx:2.8.1")

    implementation("androidx.room:room-runtime:2.5.2")
    implementation("androidx.room:room-ktx:2.5.2")
    kapt("androidx.room:room-compiler:2.5.2")

    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")

    implementation ("androidx.paging:paging-runtime-ktx:3.1.1")

    implementation ("io.ktor:ktor-client-core:2.3.4")
    implementation ("io.ktor:ktor-client-android:2.3.4")
}