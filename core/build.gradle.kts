import java.util.Properties
import kotlin.apply

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    jacoco
}

val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        load(localPropertiesFile.inputStream())
    }
}

val baseUrl = localProperties["BASE_URL"]?.toString() ?: ""
val apiKey = localProperties["API_KEY"]?.toString() ?: ""
val authKey = localProperties["AUTH_KEY"]?.toString() ?: ""

android {
    namespace = "com.example.core"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
        buildConfigField("String", "API_KEY", "\"$apiKey\"")
        buildConfigField("String", "AUTH_KEY", "\"$authKey\"")
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
        buildConfig = true
    }

    val jacocoTestReport = tasks.create("jacocoTestReport")

    androidComponents.onVariants { variant ->
        val variantCap = variant.name.replaceFirstChar { it.uppercase() }
        val testTaskName = "test${variantCap}UnitTest"

        val reportTask = tasks.register("jacoco${testTaskName}Report", JacocoReport::class) {
            dependsOn(testTaskName)

            reports {
                html.required.set(true)
                xml.required.set(true) // For SonarQube or CI
            }

            val classesDir = layout.buildDirectory
                .dir("tmp/kotlin-classes/${variant.name}")
                .get()
                .asFileTree
                .matching {
                    exclude(coverageExclusions)
                }

            val execFile = layout.buildDirectory
                .file("jacoco/$testTaskName.exec")
                .get()
                .asFile

            classDirectories.setFrom(classesDir)
            sourceDirectories.setFrom(files("src/main/java", "src/main/kotlin"))
            executionData.setFrom(execFile)
        }

        jacocoTestReport.dependsOn(reportTask)
    }
}

val coverageExclusions = listOf(
    "**/R.class",
    "**/R$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*",
    "**/*Test*.*"
)

configure<JacocoPluginExtension> {
    toolVersion = "0.8.10"
}

tasks.withType<Test>().configureEach {
    configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
    }
}

tasks.register("cleanJacocoReport") {
    doLast {
        val reportDir = layout.buildDirectory
            .dir("reports/jacoco")
            .get()
            .asFile

        if (reportDir.exists()) {
            reportDir.deleteRecursively()
            println("✅ JaCoCo reports deleted from ${reportDir.path}")
        } else {
            println("ℹ️ No JaCoCo reports found in ${reportDir.path}")
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //datastore
    implementation(libs.datastore.preferences)
    implementation(libs.datastore.core)

    //hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    //retrofit
    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.converter.gson)
    implementation(libs.gson)

    //paging
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.common.android)
    implementation(libs.androidx.paging.compose)

    //room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    testImplementation(libs.room.testing)

    //firebase
    implementation(libs.firebase.config)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.messaging)

    //test
    testImplementation(libs.truth)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.mockwebserver)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.datastore.core)
    testImplementation(libs.mockk)
    androidTestImplementation(libs.mockk.android)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.androidx.paging.testing)
    testImplementation(libs.androidx.recyclerview)


    //robolectric
    testImplementation(libs.robolectric)
}