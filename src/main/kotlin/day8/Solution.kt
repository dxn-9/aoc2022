package day8

import utils.*
import kotlin.math.max

fun main() {
    val sample = readFile("day8/SampleInput")
    val input = readFile("day8/Input")

    solveSample1 { solve1(sample) }
    solveSolution1 { solve1(input) }
    solveSample2 { solve2(sample) }
    solveSolution2 { solve2(input) }
}


typealias Matrix = MutableList<MutableList<Int>>

fun solve2(input: String): Int {
    val matrix: Matrix = mutableListOf()

    for (line in input.lines()) {
        matrix.add(line.split("").mapNotNull(String::toIntOrNull).toMutableList())
    }

    var result = 0
    for (row in 0..matrix.lastIndex) {
        for (col in 0..matrix[row].lastIndex) {
            result = max(result, calcScenicScore(row, col, matrix))
        }
    }

    return result
}

fun solve1(input: String): Int {
    val matrix: Matrix = mutableListOf()

    for (line in input.lines()) {
        matrix.add(line.split("").mapNotNull(String::toIntOrNull).toMutableList())
    }

    var result = 0
    for (row in 0..matrix.lastIndex) {
        for (col in 0..matrix[row].lastIndex) {
            result += visibleFrom(row, col, matrix)
        }
    }

    return result
}

fun visibleFrom(row: Int, col: Int, matrix: Matrix): Int {
    val value = matrix[row][col]
    // Check left
    var highest = 0
    for (i in 0 until col) {
        highest = max(highest, matrix[row][i])
    }
    if (highest < value || col == 0) return 1

    // Check top
    highest = 0
    for (i in 0 until row) {
        highest = max(highest, matrix[i][col])
    }
    if (highest < value || row == 0) return 1

    // Check right
    highest = 0
    for (i in matrix[row].lastIndex downTo col + 1) {
        highest = max(highest, matrix[row][i])
    }
    if (highest < value || col == matrix[row].lastIndex) return 1

    // Check down
    highest = 0
    for (i in matrix.lastIndex downTo row + 1) {
        highest = max(highest, matrix[i][col])
    }
    if (highest < value || row == matrix.lastIndex) return 1

    return 0

}

fun calcScenicScore(row: Int, col: Int, matrix: Matrix): Int {
    val value = matrix[row][col]
    var score = 1

    val isEdge = row == 0 || row == matrix.lastIndex || col == 0 || col == matrix[row].lastIndex
    if (isEdge) return score

    // Check left
    var i = col - 1
    while (i > 0) {
        if (matrix[row][i] >= value) break
        i--
    }
    score *= col - i

    // Check top
    i = row - 1
    while (i > 0) {
        if (matrix[i][col] >= value) break
        i--
    }
    score *= row - i

    // Check right
    i = col + 1
    while (i < matrix[row].lastIndex) {
        if (matrix[row][i] >= value) break
        i++
    }
    score *= i - col

    // Check down
    i = row + 1
    while (i < matrix.lastIndex) {
        if (matrix[i][col] >= value) break
        i++
    }
    score *= i - row

    return score

}
