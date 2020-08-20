buildscript {
    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpathAll(
            Dependency.androidToolsBuildGradle,
            Dependency.kotlinGradlePlugin
        )
    }
}

task<Delete>("clean") {
    delete = setOf(rootProject.buildDir, File(rootDir, "buildSrc/build"))
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}
