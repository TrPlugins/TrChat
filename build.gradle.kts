import io.izzel.taboolib.gradle.*
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    id("io.izzel.taboolib") version "2.0.38"
    id("org.jetbrains.kotlin.jvm") version "2.4.10"
}

subprojects {
    apply<JavaPlugin>()
    apply(plugin = "io.izzel.taboolib")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    taboolib {
        env {
            install("basic-configuration")
            install("incision")
            install(
                BukkitHook,
                BukkitNMSUtil,
                BukkitNMSItemTag,
                BukkitUI
            )
            install(
                "database",
                "database-alkaid-redis",
                "database-player",
//                "database-postgresql"
            )
            install(
                "minecraft-chat",
                "minecraft-command-helper",
                "minecraft-i18n",
                "minecraft-kether",
                "minecraft-metrics"
            )
            install(JavaScript)
            install(Bukkit, BungeeCord, Velocity)
            modules.remove("minecraft-chat")
            disableOnSkippedVersion = false
//            disableOnUnsupportedVersion = false
        }
        version {
            taboolib = "6.3.0-e78afcf"
            coroutines = null
        }
    }

    // 全局仓库
    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.xenondevs.xyz/releases")
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
        maven("https://repo.codemc.io/repository/maven-public/")
    }

    // 全局依赖
    dependencies {
        compileOnly(kotlin("stdlib"))
        compileOnly("com.google.code.gson:gson:2.8.5")
        compileOnly("com.google.guava:guava:21.0")
        compileOnly("net.kyori:adventure-api:4.26.1")
    }

    // 编译配置
    java {
        withSourcesJar()
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    tasks.withType<KotlinCompile> {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_1_8
            freeCompilerArgs = listOf("-Xjvm-default=all")
        }
    }
}

gradle.buildFinished {
    buildDir.deleteRecursively()
}
