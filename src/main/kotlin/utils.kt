package utils

import java.io.BufferedReader

fun readFile(path: String): String {
    val classLoader = Thread.currentThread().contextClassLoader
    val inputStream = classLoader.getResourceAsStream(path) ?: throw Exception("Invalid path!")
    return inputStream.use {
        it.bufferedReader().use(BufferedReader::readText)
    }
}

fun solveSample1(fn: () -> Any) {
    println("--- Sample Input 1 ---")
    println(fn())
    println("--------------------")
}

fun solveSolution1(fn: () -> Any) {
    println("--- Solution 1 ---")
    println(fn())
    println("--------------------")
}

fun solveSample2(fn: () -> Any) {
    println("--- Sample Input 2 ---")
    println(fn())
    println("--------------------")
}


fun solveSolution2(fn: () -> Any) {
    println("--- Solution 2 ---")
    println(fn())
    println("--------------------")
}
