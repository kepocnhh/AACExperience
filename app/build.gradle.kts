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
}

dependencies {
    implementation(Dependency.kotlinStdlib)
}
