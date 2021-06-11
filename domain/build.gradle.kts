plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    compileSdkVersion(AppConfig.compileSdkVersion)
    buildToolsVersion(AppConfig.buildToolsVersion)

    defaultConfig {
        minSdkVersion(AppConfig.minSdkVersion)
        targetSdkVersion(AppConfig.targetSdkVersion)
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}
dependencies {
    implementation(project(":core_entities"))

    // dagger
    kapt("com.google.dagger:dagger-android-processor:${Versions.dagger}")
    kapt("com.google.dagger:dagger-compiler:${Versions.dagger}")
    implementation("com.google.dagger:dagger:${Versions.dagger}")
    implementation("com.google.dagger:dagger-android-support:${Versions.dagger}")
    implementation("com.google.dagger:dagger-android:${Versions.dagger}")

    //Coroutines
    implementation(Dependencies.kotlinCoroutinesAndroid)
    implementation(Dependencies.kotlinCoroutinesCore)
}

