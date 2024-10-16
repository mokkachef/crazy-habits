plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.androidx.navigation.safeargs.kotlin)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.ksp)
}

android {
    namespace = "com.example.app"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.example.crazy_habits"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

//        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunner = "com.example.crazy_habits.CustomTestRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
    dataBinding {
        enable = true
        enableForTests = true
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
    packaging {
        resources {
            excludes.addAll(listOf("META-INF/LICENSE.md", "META-INF/LICENSE-notice.md"))
        }
    }
    sourceSets {
        getByName("debug") {
            java.srcDirs("src/debug/java")
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            testProguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguardTest-rules.pro"
            )
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            testProguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguardTest-rules.pro"
            )
        }
    }

}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.android.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.bundles.navigation)
    implementation(libs.bundles.lifecycle)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.coil)
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)

    androidTestImplementation(libs.bundles.junit)
    androidTestImplementation(libs.coroutines.test)
    androidTestImplementation(libs.androidx.arch.core.testing)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.androidx.appcompat)
    androidTestImplementation(libs.androidx.navigation.testing)
    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.compiler)
    androidTestImplementation(libs.bundles.hamcrest)
    androidTestImplementation(libs.kaspresso)
    androidTestUtil(libs.androidx.test.orchestrator)

    /*
    The fragment-testing-manifest artifact separates out the manifest entries from the rest of the fragment-testing components.
    This avoids conflicts due to version skew between fragment-testing and androidx.test
    docs https://developer.android.com/jetpack/androidx/releases/fragment#1.6.0
    */
    debugImplementation(libs.androidx.fragment.testing.manifest)

    implementation(project(":presentation"))
    implementation(project(":data"))
    implementation(project(":domain"))
}

