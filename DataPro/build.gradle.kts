import org.jetbrains.kotlin.gradle.tasks.CompileUsingKotlinDaemon

plugins {
    kotlin("jvm") version "1.8.20"
    application
    id("org.openjfx.javafxplugin") version "0.0.10"
}

javafx {
    modules("javafx.web")
    version = "11"
}

group = "com.github.ax-as"
version = "1.0-SNAPSHOT"
repositories {
    mavenCentral()
    maven("https://repo.kotlin.link")
    maven(url = "https://jitpack.io")
    repositories {
        maven("https://repository.apache.org/content/repositories/snapshots/")
    }


}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")

    dependencies {
        implementation("org.jetbrains.kotlinx:dataframe:0.12.0")
        implementation("technology.tabula:tabula:1.0.5")
        implementation("com.github.ajalt.clikt:clikt:4.2.0")
        implementation("org.jetbrains.lets-plot:lets-plot-kotlin-jvm:4.4.2")
        implementation("org.jetbrains.lets-plot:lets-plot-image-export:4.0.0")

        val pdfBox = "3.0.1"
        implementation("org.apache.pdfbox:pdfbox:$pdfBox"){
            exclude(group = "ch.qos.logback", module = "logback-classic")
        }
        implementation("org.apache.pdfbox:fontbox:$pdfBox")

        // https://mvnrepository.com/artifact/org.slf4j/slf4j-nop
        testImplementation("org.slf4j:slf4j-nop:2.0.9")




        // This dependency is used by the application.
        implementation("com.google.guava:guava:32.1.1-jre")



    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<Test>().configureEach {
    forkEvery = 100
}

kotlin {
    kotlinDaemonJvmArgs = listOf("-Xmx486m", "-Xms256m", "-XX:+UseParallelGC")
}

kotlin {
    jvmToolchain(11)
}


tasks.withType<CompileUsingKotlinDaemon>().configureEach {
    kotlinDaemonJvmArguments.set(
        listOf(
            "-Xmx486m",
            "-Xms256m",
            "-XX:+UseParallelGC"
        )
    )
}

application {
    mainClass.set("com.github.ax_as.datapro.MainKt")
}

tasks.jar {
    duplicatesStrategy =
        DuplicatesStrategy.EXCLUDE  // Estratégia para lidar com entradas duplicadas

    manifest {
        attributes["Main-Class"] =
            "com.github.ax_as.datapro.MainKt"  // Substitua pelo nome da sua classe principal
    }
    from(configurations.runtimeClasspath.get().map {
        if (it.isDirectory) it else zipTree(it)
    })


    exclude("META-INF/*.SF")
    exclude("META-INF/*.DSA")
    exclude("META-INF/*.RSA")
}