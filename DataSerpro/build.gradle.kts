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

}


var kandy_version = "0.4.4"



dependencies {
    testImplementation(kotlin("test"))

    dependencies {
        implementation("org.jetbrains.kotlinx:dataframe:0.11.0")
        implementation("technology.tabula:tabula:1.0.5")
        implementation("com.github.ajalt.clikt:clikt:4.2.0")
        implementation("org.jetbrains.lets-plot:lets-plot-kotlin-jvm:4.4.2")
        implementation("org.jetbrains.lets-plot:lets-plot-image-export:4.0.0")
        implementation("io.github.microutils:kotlin-logging:2.0.11")
        implementation("ch.qos.logback:logback-classic:1.2.3")

        val luceneV = "8.11.2"
        implementation("org.apache.lucene:lucene-core:$luceneV")  // Verifique a versão mais recente
        implementation("org.apache.lucene:lucene-analyzers-common:8.11.2")
        implementation("org.apache.lucene:lucene-queryparser:$luceneV")
        implementation("org.apache.lucene:lucene-queries:$luceneV")
        implementation("org.apache.lucene:lucene-misc:$luceneV")
        implementation("org.apache.lucene:lucene-memory:$luceneV")
//// https://mvnrepository.com/artifact/net.sf.cssbox/pdf2dom
//        implementation("net.sf.cssbox:pdf2dom:2.0.3")

        // https://mvnrepository.com/artifact/net.sf.cssbox/pdf2dom-lite
        implementation("net.sf.cssbox:pdf2dom-lite:2.0.3")



//        implementation("org.apache.pdfbox:pdfbox:2.0.29")
//        // https://mvnrepository.com/artifact/org.jetbrains.lets-plot/lets-plot-common
//        implementation("org.jetbrains.lets-plot:lets-plot-common:4.0.0")


//        // https://mvnrepository.com/artifact/org.jetbrains.lets-plot/lets-plot-kotlin-api
//    implementation("org.apache.commons:commons-text:1.11.1")
//        implementation("org.jetbrains.lets-plot:lets-plot-kotlin-api:2.0.1")

//        implementation("org.jetbrains.kotlinx:kandy-lets-plot:$kandy_version")
//        implementation("org.jetbrains.kotlinx:kandy-api:$kandy_version")

// https://mvnrepository.com/artifact/jetbrains.datalore.notebook/datalore-model
//        implementation("jetbrains.datalore.notebook:datalore-model:326038")

//        implementation("org.jetbrains.lets-plot:lets-plot-jfx:4.0.0")
//        implementation ("org.jetbrains.lets-plot:lets-plot-batik:4.0.0")

//        implementation("space.kscience:plotlykt-server:0.5.3")
//        implementation("no.tornado:tornadofx:1.7.20")


    }
}

tasks.test {
    useJUnitPlatform()
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
    mainClass.set("com.github.ax_as.dataserpro.MainKt")
}

tasks.jar {
    duplicatesStrategy =
        DuplicatesStrategy.EXCLUDE  // Estratégia para lidar com entradas duplicadas

    manifest {
        attributes["Main-Class"] =
            "com.github.ax_as.dataserpro.MainKt"  // Substitua pelo nome da sua classe principal
    }
    from(configurations.runtimeClasspath.get().map {
        if (it.isDirectory) it else zipTree(it)
    })

    exclude("META-INF/*.SF")
    exclude("META-INF/*.DSA")
    exclude("META-INF/*.RSA")
}