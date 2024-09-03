package day10

import utils.*

fun main() {
    val sample = readFile("day10/SampleInput")
    val input = readFile("day10/Input")

    solveSample1 { solve1(sample) }
    solveSolution1 { solve1(input) }
    solveSample1 { solve2(sample) }
    solveSolution1 { solve2(input) }
}

fun solve1(input: String): Int {
    var cycle = 0
    var x = 1
    var breakPoints = arrayOf(20, 60, 100, 140, 180, 220)
    var l = 0

    val it = input.lines().iterator()

    while (it.hasNext()) {
        var w = 0
        val line = it.next()
        val command = if (line == "noop") "noop" else "addx"
        val add = when (command) {
            "noop" -> {
                w = 1
                0
            }

            "addx" -> {
                w = 2
                line.split(" ")[1].toInt()
            }

            else -> 0
        }

        while (w > 0) {
            cycle++
            w--

            if (breakPoints.size > l && cycle == breakPoints[l]) {
                breakPoints[l] *= x
                l++
            }
        }
        x += add
    }

    return breakPoints.reduce { acc, it -> acc + it }
}

fun solve2(input: String): String {
    var cycle = 0
    var x = 1
    var pixels = mutableListOf<Char>()

    val it = input.lines().iterator()

    while (it.hasNext()) {
        var w = 0
        val line = it.next()
        val command = if (line == "noop") "noop" else "addx"
        val add = when (command) {
            "noop" -> {
                w = 1
                0
            }

            "addx" -> {
                w = 2
                line.split(" ")[1].toInt()
            }

            else -> 0
        }

        while (w > 0) {
            cycle++
            w--
            val pixelValue = if ((pixels.size % 40) in x - 1..x + 1) '#' else '.'
            pixels.add(pixelValue)
        }
        x += add
    }

    return pixels.foldIndexed(StringBuilder()) { index, acc, el ->
        acc.append(el)
        if ((index + 1) % 40 == 0 && index > 0) acc.append('\n')
        acc
    }.toString()
}