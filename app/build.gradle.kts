plugins {
    id("com.android.application")
    id("dagger.hilt.android.plugin")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

android {
    compileSdkVersion(30)
    defaultConfig {
        applicationId = "com.sduduzog.slimlauncher"
        minSdkVersion(21)
        targetSdkVersion(30)
        versionName = "2.4.19"
        versionCode = 53
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables { useSupportLibrary = true }
        signingConfigs {
            register("release") {
                storeFile = file(project.extra["RELEASE_STORE_FILE"] as String)
                storePassword = project.extra["RELEASE_STORE_PASSWORD"] as String
                keyAlias = project.extra["RELEASE_KEY_ALIAS"] as String
                keyPassword = project.extra["RELEASE_KEY_PASSWORD"] as String
            }
        }
    }



    buildTypes {
        named("release").configure {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
        named("debug").configure {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // Kotlin Libraries
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.4.31")

    // Support Libraries
    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("androidx.recyclerview:recyclerview:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")

    // Arch Components
    implementation("androidx.core:core-ktx:1.6.0-beta01")
    implementation("androidx.fragment:fragment-ktx:1.3.4")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.3.5")
    implementation("androidx.room:room-runtime:2.3.0")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.3.1")
    kapt("androidx.room:room-compiler:2.3.0")

    //3rd party libs
    implementation("com.intuit.sdp:sdp-android:1.0.6")
    implementation("com.intuit.ssp:ssp-android:1.0.6")
    implementation("com.google.dagger:hilt-android:2.29-alpha")
    implementation("androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha02")
    kapt("androidx.hilt:hilt-compiler:1.0.0-alpha02")
    kapt("com.google.dagger:hilt-android-compiler:2.29-alpha")

    // Test libs

    testImplementation("androidx.test:core:1.3.0")

    testImplementation("androidx.test:runner:1.3.0")
    testImplementation("androidx.test:rules:1.3.0")

    testImplementation("androidx.test.ext:junit:1.1.2")
    testImplementation("androidx.test.ext:truth:1.3.0")
    testImplementation("com.google.truth:truth:1.0")


    testImplementation("androidx.arch.core:core-testing:2.1.0")
    testImplementation("androidx.fragment:fragment-testing:1.3.4")

    testImplementation("org.robolectric:robolectric:4.4")

    testImplementation("org.mockito:mockito-core:2.24.5")

    testImplementation("androidx.test.espresso:espresso-core:3.3.0")

    testImplementation("com.google.dagger:hilt-android-testing:2.29-alpha")
    kaptTest("com.google.dagger:hilt-android-compiler:2.29-alpha")

    androidTestImplementation("androidx.room:room-testing:2.3.0")
    androidTestImplementation("androidx.test:runner:1.3.0")
    androidTestImplementation("androidx.test:rules:1.3.0")
    androidTestImplementation("androidx.annotation:annotation:1.2.0")
    androidTestImplementation("androidx.test:runner:1.3.0")
    androidTestImplementation("androidx.test:rules:1.3.0")
    androidTestImplementation("androidx.arch.core:core-testing:2.1.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.3.0")
}
