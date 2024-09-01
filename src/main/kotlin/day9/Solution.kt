package day9

import utils.*
import java.util.stream.Collectors
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.sqrt

fun main() {
    val sample = readFile("day9/SampleInput")
    val input = readFile("day9/Input")

    solveSample1 { solve1(sample) }
    solveSolution1 { solve1(input) }
    solveSample2 {
        solve2(
            """R 5
U 8
L 8
D 3
R 17
D 10
L 25
U 20"""
        )
    }
    solveSolution2 { solve2(input) }
}


data class Vector2(var x: Int, var y: Int) {
    fun dist(other: Vector2): Double =
        sqrt(((other.x - x) * (other.x - x) + (other.y - y) * (other.y - y)).toDouble())

    override fun toString(): String {
        return "Vector2($x $y)"
    }


}

fun solve1(input: String): Int {

    val headPosition = Vector2(0, 0)
    val tailPosition = headPosition.copy()

    val pastPositions = mutableSetOf(tailPosition)

    for (line in input.lines()) {
        val (command, amountStr) = line.split(" ")
        var amount = amountStr.toInt()

        repeat(amount) {
            when (command) {
                "R" ->
                    headPosition.x++

                "L" ->
                    headPosition.x--

                "U" ->
                    headPosition.y--

                "D" ->
                    headPosition.y++

            }


            // Move tail if necessary
            val dx = headPosition.x - tailPosition.x
            val dy = headPosition.y - tailPosition.y

            if (Math.abs(dx) > 1 || Math.abs(dy) > 1) {
                tailPosition.x += dx.coerceIn(-1, 1)
                tailPosition.y += dy.coerceIn(-1, 1)
            }

            pastPositions.add(tailPosition.copy())

        }

    }
    return pastPositions.size

}

fun solve2(input: String): Int {

    val headPosition = Vector2(0, 0)
    val pastPositions = mutableSetOf(headPosition.copy())
    val knots = Array(9) { headPosition.copy() }

    for (line in input.lines()) {
        val (command, amountStr) = line.split(" ")
        var amount = amountStr.toInt()

        repeat(amount) {
            when (command) {
                "R" ->
                    headPosition.x++

                "L" ->
                    headPosition.x--

                "U" ->
                    headPosition.y--

                "D" ->
                    headPosition.y++

            }
            var previousKnot = headPosition
            var currentKnot = knots.first()

            for (i in 1..9) {
                // Move tail if necessary
                val dx = previousKnot.x - currentKnot.x
                val dy = previousKnot.y - currentKnot.y

                if (Math.abs(dx) > 1 || Math.abs(dy) > 1) {
                    currentKnot.x += dx.coerceIn(-1, 1)
                    currentKnot.y += dy.coerceIn(-1, 1)
                }

                if (i > knots.lastIndex) break
                previousKnot = currentKnot
                currentKnot = knots[i]
            }

            pastPositions.add(knots.last().copy())

        }

    }
    return pastPositions.size

}
