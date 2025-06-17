import io.izzel.taboolib.gradle.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    id("io.izzel.taboolib") version "2.0.23"
    id("org.jetbrains.kotlin.jvm") version "1.8.22"
}

subprojects {
    apply<JavaPlugin>()
    apply(plugin = "io.izzel.taboolib")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    taboolib {
        env {
            install("basic-configuration")
            install(
                BukkitHook,
                BukkitNMSUtil,
                BukkitNMSItemTag,
                BukkitUI
            )
            install(
                "database",
                "database-alkaid-redis",
                "database-player"
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
        }
        version {
            taboolib = "6.2.3-b217935"
            coroutines = null
//            isSkipKotlin = true
//            isSkipKotlinRelocate = true
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
        compileOnly("net.kyori:adventure-api:4.17.0")
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
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xjvm-default=all", "-Xextended-compiler-checks")
        }
    }
}

gradle.buildFinished {
    buildDir.deleteRecursively()
}
