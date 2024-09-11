package day15

import utils.*

fun main() {
    val sample = readFile("day15/SampleInput")
    val input = readFile("day15/Input")

    solveSample2 { solve2(sample, 20) }
    solveSolution2 { solve2(input, 4000000) }

}

data class Coverage(val center: Pair<Int, Int>, val span: Int) {
    constructor(centerX: Int, centerY: Int, span: Int) : this(Pair(centerX, centerY), span)

    fun isInRange(x: Int, y: Int): Boolean =
        kotlin.math.abs(center.first - x) + kotlin.math.abs(center.second - y) <= span

}

fun solve1(input: String, line: Int): Int {
    val coverages = mutableListOf<Coverage>()
    val beaconsPositions = mutableSetOf<Pair<Int, Int>>()
    var lowestX = Int.MAX_VALUE
    var highestX = Int.MIN_VALUE

    for (line in input.lines()) {
        val equals = line.split("=")
        val sensorX = equals[1].split(',')[0].toInt()
        val sensorY = equals[2].split(":")[0].toInt()

        val beaconX = equals[3].split(',')[0].toInt()
        val beaconY = equals[4].toInt()
        beaconsPositions.add(Pair(beaconX, beaconY))

        val span = kotlin.math.abs(beaconX - sensorX) + kotlin.math.abs(beaconY - sensorY)

        lowestX = kotlin.math.min(lowestX, sensorX - span)
        highestX = kotlin.math.max(highestX, sensorX + span)

        coverages.add(Coverage(sensorX, sensorY, span))
    }


    var occupied = 0

    for (x in lowestX..highestX) {
        if (beaconsPositions.contains(Pair(x, line))) continue
        for (coverage in coverages) {
            if (coverage.isInRange(x, line)) {
                occupied++
                break
            }
        }

    }

    return occupied
}

fun solve2(input: String, upperBound: Int): Long {
    val coverages = mutableListOf<Coverage>()
    val beaconsPositions = mutableSetOf<Pair<Int, Int>>()
    val skipTo = mutableMapOf<Int, MutableMap<IntRange, Int>>()

    for (line in input.lines()) {
        val equals = line.split("=")
        val sensorX = equals[1].split(',')[0].toInt()
        val sensorY = equals[2].split(":")[0].toInt()

        val beaconX = equals[3].split(',')[0].toInt()
        val beaconY = equals[4].toInt()
        beaconsPositions.add(Pair(beaconX, beaconY))

        val span = kotlin.math.abs(beaconX - sensorX) + kotlin.math.abs(beaconY - sensorY)

        val coverage = Coverage(sensorX, sensorY, span)
        for (y in -coverage.span..coverage.span) {
            val x = coverage.span - kotlin.math.abs(y)

            val rY = coverage.center.second + y

            if (skipTo[rY] == null) {
                skipTo[rY] = mutableMapOf()
            }

            if (rY < 0 || rY > upperBound) continue
            // TODO: If it's slow, we  can also eliminate internal ranges. ( if a range is bigger than another, we can just eliminate the smaller one.)
            skipTo[rY]!![coverage.center.first - x..coverage.center.first + x] = coverage.center.first + x + 1
        }

        coverages.add(coverage)
    }


    var y = 0
    while (y <= upperBound) {
        var x = 0
        xLoop@ while (x <= upperBound) {

            val map = skipTo[y.toInt()] ?: throw Exception("Can this happen?")
            for (range in map.keys) {
                if (x in range) {
                    x = map[range]!!
                    continue@xLoop
                }
            }

            println("X! $x $y")

            return x.toLong() * 4000000 + y.toLong()
        }
        y++
    }

    return 0
}