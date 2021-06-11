plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
}

android {
    compileSdkVersion(AppConfig.compileSdkVersion)
    buildToolsVersion(AppConfig.buildToolsVersion)

    signingConfigs {
        getByName("debug") {
            keyAlias(DebugKeystoreConfig.keyAlias)
            keyPassword(DebugKeystoreConfig.keyPassword)
            storePassword(DebugKeystoreConfig.storePassword)
            storeFile(file(DebugKeystoreConfig.storeFilePath))
        }
    }

    defaultConfig {
        applicationId(AppConfig.applicationId)
        minSdkVersion(AppConfig.minSdkVersion)
        targetSdkVersion(AppConfig.targetSdkVersion)
        versionCode(AppConfig.versionCode)
        versionName(AppConfig.versionName)
        testInstrumentationRunner(AppConfig.androidTestInstrumentation)
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
    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xinline-classes")
    }
}


dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}")
    implementation("androidx.appcompat:appcompat:${Versions.appcompat}")
    implementation("androidx.core:core-ktx:${Versions.coreKtx}")
    implementation("androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}")
    testImplementation("junit:junit:${Versions.junit}")
    implementation("androidx.recyclerview:recyclerview:${Versions.recyclerView}")
    implementation("com.google.android.material:material:${Versions.material}")

    // Module dependencies
    implementation(project(":core_entities"))
    implementation(project(":domain"))
    implementation(project(":data"))

    // ViewBinding delegate
    implementation("com.github.kirich1409:viewbindingpropertydelegate-noreflection:${Versions.viewBindingDelegate}")

    // Skeleton shimmer
    implementation("com.facebook.shimmer:shimmer:${Versions.shimmer}")
    implementation("com.ethanhua:skeleton:${Versions.skeleton}")
    implementation("io.supercharge:shimmerlayout:${Versions.shimmerLayout}")

    //Moxy
    implementation("com.github.moxy-community:moxy:${Versions.moxy}")
    implementation("com.github.moxy-community:moxy-androidx:${Versions.moxy}")
    implementation("com.github.moxy-community:moxy-ktx:${Versions.moxy}")
    kapt("com.github.moxy-community:moxy-compiler:${Versions.moxy}")

    // Dagger
    kapt("com.google.dagger:dagger-android-processor:${Versions.dagger}")
    kapt("com.google.dagger:dagger-compiler:${Versions.dagger}")
    implementation("com.google.dagger:dagger:${Versions.dagger}")
    implementation("com.google.dagger:dagger-android-support:${Versions.dagger}")
    implementation("com.google.dagger:dagger-android:${Versions.dagger}")

    //Cicerone Navigation
    implementation("com.github.terrakok:cicerone:${Versions.cicerone}")

    //Glide
    implementation("com.github.bumptech.glide:glide:${Versions.glide}")
    kapt("com.github.bumptech.glide:compiler:${Versions.glide}")

    //Coroutines
    implementation(Dependencies.kotlinCoroutinesAndroid)
    implementation(Dependencies.kotlinCoroutinesCore)

    // Timber logging
    implementation("com.jakewharton.timber:timber:${Versions.timber}")

}
