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
               val sampleText = readFile("day${day.get()}/SampleInput")
               val input = readFile("day${day.get()}/Input")
            }
            
        """.trimIndent()
            )
        }

        // Open the created file in Idea
        ProcessBuilder("idea", "$source/Solution.kt").start()


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
