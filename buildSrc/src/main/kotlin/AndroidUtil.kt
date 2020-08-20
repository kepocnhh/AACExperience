import com.android.build.gradle.BaseExtension
import com.android.build.gradle.internal.dsl.DefaultConfig

fun BaseExtension.defaultConfig(
    compileSdkVersion: Int,
    buildToolsVersion: String,
    minSdkVersion: Int,
    targetSdkVersion: Int,
    block: DefaultConfig.() -> Unit
) {
    compileSdkVersion(compileSdkVersion)
    buildToolsVersion(buildToolsVersion)
    defaultConfig {
        minSdkVersion(minSdkVersion)
        targetSdkVersion(targetSdkVersion)
        block()
    }
    sourceSets.all {
        java.srcDir("src/$name/kotlin")
    }
}

fun BaseExtension.commonConfig(
    block: DefaultConfig.() -> Unit = {}
) {
    defaultConfig(
        compileSdkVersion = Version.Android.compileSdk,
        buildToolsVersion = Version.Android.buildTools,
        minSdkVersion = Version.Android.minSdk,
        targetSdkVersion = Version.Android.targetSdk,
        block = block
    )
}
