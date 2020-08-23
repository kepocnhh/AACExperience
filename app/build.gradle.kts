plugins {
    applyAll(
        Plugin.androidApplication,
        Plugin.kotlinAndroid
    )
}

android {
    commonConfig {
        applicationId = Common.applicationId
        versionCode = Version.Application.code
        versionName = Version.Application.name
        // vectorDrawables.useSupportLibrary = true
        // multiDexEnabled = true
    }

    buildTypes {
        getByName(BuildType.debug) {
            isMinifyEnabled = false
            isShrinkResources = false
            applicationIdSuffix = ".$name"
            versionNameSuffix = "-$name"
        }
    }

//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_1_8
//        targetCompatibility = JavaVersion.VERSION_1_8
//    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(Dependency.kotlinStdlib)
    implementation(Dependency.okhttp)
    implementation("androidx.fragment:fragment:1.2.5")
//    implementation("androidx.fragment:fragment-ktx:1.2.5")
    implementation("androidx.appcompat:appcompat:1.2.0")
//    implementation("androidx.appcompat:appcompat:1.2.5")
    implementation("androidx.recyclerview:recyclerview:1.1.0")
//    implementation("androidx.recyclerview:recyclerview:1.2.5")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.11.2")
}
