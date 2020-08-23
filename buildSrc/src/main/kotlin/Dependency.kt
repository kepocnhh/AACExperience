private object Group {
    const val android = "com.android"
    const val jetbrains = "org.jetbrains"
    const val kotlin = "$jetbrains.kotlin"
}

data class Dependency(
    val group: String,
    val name: String,
    val version: String
) {
    companion object {
        val androidToolsBuildGradle = Dependency(
            group = Group.android + ".tools.build",
            name = "gradle",
            version = Version.Android.toolsBuildGradle
        )

        val kotlinGradlePlugin = Dependency(
            group = Group.kotlin,
            name = "kotlin-gradle-plugin",
            version = Version.kotlin
        )
        val kotlinStdlib = Dependency(
            group = Group.kotlin,
            name = "kotlin-stdlib",
            version = Version.kotlin
        )
        val okhttp = Dependency(
            group = "com.squareup.okhttp3",
            name = "okhttp",
            version = Version.okhttp
        )
    }
}

data class Plugin(
    val name: String,
    val version: String
) {
    companion object {
        val androidApplication = Plugin(
            name = Group.android + ".application",
            version = ""
        )

        val kotlinAndroid = Plugin(
            name = "kotlin-android",
            version = Version.kotlin
        )
    }
}
