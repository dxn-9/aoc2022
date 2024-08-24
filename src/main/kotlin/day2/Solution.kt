package day2

import utils.*

fun main() {
    val sample = readFile("day2/SampleInput")
    val input = readFile("day2/Input")

    solveSample1 { solve1(sample) }
    solveSolution1 { solve1(input) }

    solveSample2 { solve2(sample) }
    solveSolution2 { solve2(input) }
}

val winTo = mapOf('A' to 'C', 'B' to 'A', 'C' to 'B')
val looseTo = winTo.map { map -> map.value to map.key }.toMap()


val movePoints = mapOf('A' to 1, 'B' to 2, 'C' to 3)


fun solve1(input: String): Int {
    var points = 0
    input.reader().useLines { lines ->
        for (line in lines) {
            var (enemyMove, yourMove) = line.split(" ").map { it.toCharArray()[0] }
            yourMove -= ('X' - 'A')
            points += when {
                winTo[yourMove] == enemyMove -> 6
                yourMove == enemyMove -> 3
                else -> 0
            }
            points += movePoints[yourMove] ?: throw Exception("Invalid move!")

        }
    }

    return points
}

fun solve2(input: String): Int {
    var points = 0
    input.reader().useLines { lines ->
        for (line in lines) {
            val (enemyMove, yourResult) = line.split(" ").map { it.toCharArray()[0] }
            var yourMove: Char? = null
            points += when (yourResult) {
                'Z' -> {
                    yourMove = looseTo[enemyMove]
                    6
                }

                'Y' -> {
                    yourMove = enemyMove
                    3
                }

                else -> {
                    yourMove = winTo[enemyMove]
                    0
                }
            }
            points += movePoints[yourMove] ?: throw Exception("Invalid move!")

        }
    }

    return points

}