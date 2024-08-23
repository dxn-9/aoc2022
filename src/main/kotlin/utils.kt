package utils

import java.io.BufferedReader

fun readFile(path: String): String {
    val classLoader = Thread.currentThread().contextClassLoader
    val inputStream = classLoader.getResourceAsStream(path) ?: throw Exception("Invalid path!")
    return inputStream.use {
        it.bufferedReader().use(BufferedReader::readText)
    }
}
