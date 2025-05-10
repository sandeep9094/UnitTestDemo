import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.developidea.unittestdemo"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.developidea.unittestdemo"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
        buildConfig = true
    }

    // Read properties from app_env.properties
    val properties = Properties()
    val propertiesFile = project.rootProject.file("app/app_env.properties") // Adjust path if needed
    if (propertiesFile.exists()) {
        propertiesFile.inputStream().use { input ->
            properties.load(input)
        }
    } else {
        // Handle the case where the file doesn't exist, e.g., log a warning or error
        println("Warning: app_env.properties not found at ${propertiesFile.absolutePath}")
    }

    // Define buildConfigField using the property
    buildTypes.all {
        buildConfigField("String", "API_KEY", "\"${properties.getProperty("API_KEY", "")}\"")
        // The second argument to getProperty is a default value if the key is not found
        // We wrap the value in double quotes and escape them to ensure it's treated as a String literal in BuildConfig
    }

    tasks.withType<Test> {
        jvmArgs("-XX:+EnableDynamicAgentLoading")
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
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Add these dependencies to your build.gradle (app) file
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:5.11.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // OkHttp
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.4.1")
    implementation("com.squareup.okhttp3:okhttp-urlconnection:4.4.1")
    testImplementation("app.cash.turbine:turbine:1.2.0")
}