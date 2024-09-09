package day14

import utils.*
import kotlin.math.max
import kotlin.math.min

fun main() {
    val sample = readFile("day14/SampleInput")
    val input = readFile("day14/Input")

    solveSample1 { solve1(sample) }
    solveSolution1 { solve1(input) }

    solveSample2 { solve2(sample) }
    solveSolution2 { solve2(input) }
}

operator fun Pair<Int, Int>.compareTo(other: Pair<Int, Int>): Int {
    if (this.first < other.first || this.second < other.second) return -1
    if (this.first > other.second || this.second > other.second) return 1
    return 0
}

fun Array<Array<Char>>.getChar(point: Pair<Int, Int>): Char {
    return this[point.second][point.first]
}

fun Array<Array<Char>>.setChar(point: Pair<Int, Int>, char: Char) {
    this[point.second][point.first] = char
}


fun solve1(input: String): Int {
    var xBounds = Pair(Int.MAX_VALUE, 0)
    var yBounds = Pair(0, 0)

    for (line in input.lines()) {
        line.replace("->", "-").split("-")
            .map { Pair(it.split(",")[0].trim().toInt(), it.split(",")[1].trim().toInt()) }
            .forEach {
                xBounds = Pair(min(xBounds.first, it.first), max(xBounds.second, it.first))
                yBounds = Pair(0, max(yBounds.second, it.second))
            }
    }


    val grid = Array(yBounds.second + 1) { Array(xBounds.second - xBounds.first + 1) { '.' } }

    for (line in input.lines()) {
        val rocks = line.replace("->", "-").split("-")
            .map { Pair(it.split(",")[0].trim().toInt() - xBounds.first, it.split(",")[1].trim().toInt()) }
        for (i in 0 until rocks.lastIndex) {
            // Sort them
            val firstRock = if (rocks[i] < rocks[i + 1]) {
                rocks[i]
            } else {
                rocks[i + 1]
            }
            val secondRock = if (firstRock == rocks[i]) {
                rocks[i + 1]
            } else {
                rocks[i]
            }

            // Draw them only if there is a dx or dy
            if (firstRock.first - secondRock.first != 0) {
                for (x in firstRock.first..secondRock.first) {

                    grid[firstRock.second][x] = '#'
                }
            }
            if (firstRock.second - secondRock.second != 0) {
                for (y in firstRock.second..secondRock.second) {
                    grid[y][firstRock.first] = '#'
                }
            }
        }
    }

    val sandSpawn = Pair(500 - xBounds.first, 0)
    var lastSand = Pair(500 - xBounds.first, 0)
    var sandSpawned = 0
    while (true) {

        if (lastSand == sandSpawn && grid.getChar(lastSand) == '.') {
            grid.setChar(lastSand, '+')
            sandSpawned++
            continue
        }
        var nextPosition = lastSand.copy(second = lastSand.second + 1)

        if (nextPosition.second > grid.lastIndex) {
            break
        }
        if (grid.getChar(nextPosition) == '.') {
            grid.setChar(lastSand, '.')
            lastSand = nextPosition
            grid.setChar(lastSand, '+')
            continue
        }

        nextPosition = lastSand.copy(first = lastSand.first - 1, second = lastSand.second + 1)
        if (nextPosition.first < 0) {
            break
        }
        if (grid.getChar(nextPosition) == '.') {
            grid.setChar(lastSand, '.')
            lastSand = nextPosition
            grid.setChar(lastSand, '+')
            continue
        }

        nextPosition = lastSand.copy(first = lastSand.first + 1, second = lastSand.second + 1)
        if (nextPosition.first > grid[0].lastIndex) {
            break
        }

        if (grid.getChar(nextPosition) == '.') {
            grid.setChar(lastSand, '.')
            lastSand = nextPosition
            grid.setChar(lastSand, '+')
            continue
        }

        lastSand = sandSpawn.copy()

    }


    for (line in grid) {
        println(line.joinToString(" "))

    }





    return sandSpawned - 1 // Last falling one doesnt count
}

fun solve2(input: String): Int {
    var xBounds = Pair(Int.MAX_VALUE, 0)
    var yBounds = Pair(0, 0)

    for (line in input.lines()) {
        line.replace("->", "-").split("-")
            .map { Pair(it.split(",")[0].trim().toInt(), it.split(",")[1].trim().toInt()) }
            .forEach {
                xBounds = Pair(min(xBounds.first, it.first), max(xBounds.second, it.first))
                yBounds = Pair(0, max(yBounds.second, it.second))
            }
    }


    yBounds = yBounds.copy(second = yBounds.second + 2)
    xBounds = xBounds.copy(
        first = min(500 - yBounds.second, xBounds.first),
        second = max(500 + yBounds.second, xBounds.second)
    )
    val grid = Array(yBounds.second + 1) { Array(xBounds.second - xBounds.first + 1) { '.' } }

    for (line in input.lines()) {
        val rocks = line.replace("->", "-").split("-")
            .map { Pair(it.split(",")[0].trim().toInt() - xBounds.first, it.split(",")[1].trim().toInt()) }
        for (i in 0 until rocks.lastIndex) {
            // Sort them
            val firstRock = if (rocks[i] < rocks[i + 1]) {
                rocks[i]
            } else {
                rocks[i + 1]
            }
            val secondRock = if (firstRock == rocks[i]) {
                rocks[i + 1]
            } else {
                rocks[i]
            }

            // Draw them only if there is a dx or dy
            if (firstRock.first - secondRock.first != 0) {
                for (x in firstRock.first..secondRock.first) {

                    grid[firstRock.second][x] = '#'
                }
            }
            if (firstRock.second - secondRock.second != 0) {
                for (y in firstRock.second..secondRock.second) {
                    grid[y][firstRock.first] = '#'
                }
            }
        }
    }
    for (x in 0..grid.last().lastIndex) {
        grid.last()[x] = '#'
    }

    val sandSpawn = Pair(500 - xBounds.first, 0)
    var lastSand = Pair(500 - xBounds.first, 0)
    var sandSpawned = 0
    while (true) {

        if (lastSand == sandSpawn && grid.getChar(lastSand) == '.') {
            grid.setChar(lastSand, '+')
            sandSpawned++
            continue
        }
        var nextPosition = lastSand.copy(second = lastSand.second + 1)


        if (nextPosition.second > grid.lastIndex) {
            break
        }
        if (grid.getChar(nextPosition) == '.') {
            grid.setChar(lastSand, '.')
            lastSand = nextPosition
            grid.setChar(lastSand, '+')
            continue
        }

        nextPosition = lastSand.copy(first = lastSand.first - 1, second = lastSand.second + 1)
        if (nextPosition.first < 0) {
            break
        }
        if (grid.getChar(nextPosition) == '.') {
            grid.setChar(lastSand, '.')
            lastSand = nextPosition
            grid.setChar(lastSand, '+')
            continue
        }

        nextPosition = lastSand.copy(first = lastSand.first + 1, second = lastSand.second + 1)
        if (nextPosition.first > grid[0].lastIndex) {
            break
        }
        if (lastSand == sandSpawn && grid.getChar(nextPosition) == '+') {
            break
        }

        if (grid.getChar(nextPosition) == '.') {
            grid.setChar(lastSand, '.')
            lastSand = nextPosition
            grid.setChar(lastSand, '+')
            continue
        }

        lastSand = sandSpawn.copy()

    }


    for (line in grid) {
        println(line.joinToString(" "))

    }





    return sandSpawned  // Last falling one doesnt count
}
