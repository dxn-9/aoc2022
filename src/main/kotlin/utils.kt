package utils

import java.io.BufferedReader
import kotlin.time.measureTimedValue

fun timed(fn: () -> Any) {
    val elapsed = measureTimedValue { println(fn()) }
    println("Took: ${elapsed.duration.inWholeMilliseconds}ms")
}

fun readFile(path: String): String {
    val classLoader = Thread.currentThread().contextClassLoader
    val inputStream = classLoader.getResourceAsStream(path) ?: throw Exception("Invalid path!")
    return inputStream.use {
        it.bufferedReader().use(BufferedReader::readText)
    }
}

fun solveSample1(fn: () -> Any) {
    println("--- Sample Input 1 ---")
    timed(fn)
    println("--------------------")
}

fun solveSolution1(fn: () -> Any) {
    println("--- Solution 1 ---")
    timed(fn)
    println("--------------------")
}

fun solveSample2(fn: () -> Any) {
    println("--- Sample Input 2 ---")
    timed(fn)
    println("--------------------")
}


fun solveSolution2(fn: () -> Any) {
    println("--- Solution 2 ---")
    timed(fn)
    println("--------------------")
}
