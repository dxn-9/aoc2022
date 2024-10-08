import groovy.lang.Closure
import java.io.BufferedWriter

plugins {
    kotlin("jvm") version "2.0.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

abstract class Scaffold : DefaultTask() {
    @get:Input
    abstract val day: Property<Int>

    @get:Input
    abstract val projectDir: Property<String>

    @TaskAction
    fun action() {

        val resource = "${projectDir.get()}/src/main/resources/day${day.get()}"
        val source = "${projectDir.get()}/src/main/kotlin/day${day.get()}"
        createDir(resource)
        createDir(source)
        createFile("$resource/SampleInput")
        createFile("$resource/Input")
        createFile("$source/Solution.kt")
        val f = File("$source/Solution.kt")
        f.writer().use {
            it.write(
                """
            package day${day.get()}
            import utils.*
            
            fun main() {
               val sample = readFile("day${day.get()}/SampleInput")
               val input = readFile("day${day.get()}/Input")
            }
            
        """.trimIndent()
            )
        }

        try {
            // Open the created files in Idea (linux/osx)
            ProcessBuilder("idea", "$resource/SampleInput").start()
            ProcessBuilder("idea", "$resource/Input").start()
            ProcessBuilder("idea", "$source/Solution.kt").start()
        } catch (e: Exception) {
            // If i'm on windows
            ProcessBuilder("idea.cmd", "$resource/SampleInput").start()
            ProcessBuilder("idea.cmd", "$resource/Input").start()
            ProcessBuilder("idea.cmd", "$source/Solution.kt").start()
        }


    }

    private fun createFile(str: String) = File(str).also {
        if (it.exists()) {
            throw Exception("File $str already exists!")
        }
    }.createNewFile()

    private fun createDir(str: String) = File(str).also {
        if (it.exists()) {
            throw Exception("Folder $str already exists!")
        }
    }.mkdirs()


}

tasks.register<Scaffold>("scaffold") {
    val day = properties["day"]
        ?: throw Exception("Please provide -Pday=<number> to indicate the day for which to scaffold for.")

    this.projectDir = project.projectDir.toString()
    this.day = try {
        Integer.parseInt(day.toString())
    } catch (e: Exception) {
        throw Exception("Please provide a valid number as the -Pday=<number> argument.")
    }
}
