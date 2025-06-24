import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
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
    namespace = "com.example.ecommerceapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.ecommerceapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
        compose = true
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
                    include("**/*ViewModel.class") // ⬅️ Tambahkan baris ini
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

tasks.register<JacocoReport>("jacocoMergedReport") {
    dependsOn(
        ":app:testDebugUnitTest",
        ":core:testDebugUnitTest"
    )

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    val excludes = listOf(
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "**/di/**",
        "**/*_Factory*.*",
        "**/*_HiltModules*.*",
        "**/*_Impl*.*",
        "**/databinding/**"
    )

    // Menggunakan layout.buildDirectory
    val appClasses = layout.buildDirectory.dir("../app/build/tmp/kotlin-classes/debug").get().asFile
    val coreClasses = layout.buildDirectory.dir("../core/build/tmp/kotlin-classes/debug").get().asFile

    classDirectories.setFrom(
        fileTree(appClasses) { exclude(excludes) },
        fileTree(coreClasses) { exclude(excludes) }
    )

    sourceDirectories.setFrom(
        files(
            "${project(":app").projectDir}/src/main/java",
            "${project(":app").projectDir}/src/main/kotlin",
            "${project(":core").projectDir}/src/main/java",
            "${project(":core").projectDir}/src/main/kotlin"
        )
    )

    executionData.setFrom(
        layout.buildDirectory.file("../app/build/jacoco/testDebugUnitTest.exec"),
        layout.buildDirectory.file("../core/build/jacoco/testDebugUnitTest.exec")
    )
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
    implementation(libs.androidx.paging.runtime)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //splash
    implementation(libs.androidx.core.splashscreen)

    //navigation
    implementation(libs.androidx.navigation.runtime)
    implementation(libs.androidx.navigation.compose)

    //foundation
    implementation(libs.androidx.foundation)

    //accompanist
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicators)

    //icon extended
    implementation(libs.androidx.material.icons.extended)

    //lottie
    implementation(libs.lottie.compose)

    //compat
    implementation(libs.appcompat)

    //datastore
    implementation(libs.datastore.preferences)

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

    //chucker
    debugImplementation(libs.chucker)
    releaseImplementation(libs.chucker.no.op)

    //coil
    implementation(libs.coil.compose)

    //paging
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
    implementation(libs.firebase.crashlytics)

    //permission`1
    implementation(libs.accompanist.permissions)
    implementation(libs.androidx.compose.runtime.livedata)

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

    //robolectric
    testImplementation(libs.robolectric)

    implementation(project(":screen"))
    implementation(project(":core"))
    testImplementation(kotlin("test"))
}