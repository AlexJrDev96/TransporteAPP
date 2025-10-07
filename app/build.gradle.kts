plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.fragmentos"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.fragmentos"
        minSdk = 26
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.circleimageview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    dependencies {
        val room_version = "2.8.0"

        implementation(libs.androidx.room.runtime)

        // If this project uses any Kotlin source, use Kotlin Symbol Processing (KSP)
        // See Add the KSP plugin to your project
        ksp(libs.androidx.room.room.compiler)

        // If this project only uses Java source, use the Java annotationProcessor
        // No additional plugins are necessary
        annotationProcessor(libs.androidx.room.room.compiler)

        // optional - Kotlin Extensions and Coroutines support for Room
        implementation(libs.androidx.room.ktx)

        // optional - RxJava2 support for Room
        implementation(libs.androidx.room.rxjava2)

        // optional - RxJava3 support for Room
        implementation(libs.androidx.room.rxjava3)

        // optional - Guava support for Room, including Optional and ListenableFuture
        implementation(libs.androidx.room.guava)

        // optional - Test helpers
        testImplementation(libs.androidx.room.testing)

        // optional - Paging 3 Integration
        implementation(libs.androidx.room.paging)
    }
}