// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    val kotlin_version by extra("1.4.31")
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(GradlePlugins.android)
        classpath(GradlePlugins.kotlin)
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://oss.jfrog.org/libs-snapshot") }
        maven { setUrl("https://jitpack.io") }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
