import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("dev.hydraulic.conveyor") version "1.2"
}

group = "com.alphaboom"
version = "1.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm {
        jvmToolchain(11)
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(compose.material3)
                implementation("org.jetbrains.compose.material:material-icons-extended-desktop:${extra["compose.version"]}")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.7.2")
                implementation("com.github.kwhat:jnativehook:2.2.2")
                implementation("com.squareup.okhttp3:okhttp:4.11.0")
                implementation("com.squareup.okhttp3:okhttp-sse:4.11.0")
                implementation("com.squareup.retrofit2:retrofit:2.9.0")
                implementation("com.google.cloud:google-cloud-texttospeech:2.21.0")
                implementation("com.darkrockstudios:mpfilepicker:1.2.0")
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "compose-groceries"
            packageVersion = "1.0.3"
            windows{
                menu = true
            }
        }
        buildTypes.release {
            proguard {
                configurationFiles.from("proguard-rules.pro")
            }
        }
    }
}