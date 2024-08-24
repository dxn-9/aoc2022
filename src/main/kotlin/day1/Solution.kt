package day1

import utils.*
import java.io.BufferedReader

fun main() {
    val sampleText = readFile("day1/SampleInput")
    val input = readFile("day1/Input")

    solveSample1 { solve1(sampleText) }
    solveSolution1 { solve1(input) }

    solveSample2 { solve2(sampleText) }
    solveSolution2 { solve2(input) }


}

fun solve1(input: String): Int {
    var max = 0
    var acc = 0
    input.reader().useLines { lines ->
        for (line in lines) {
            if (line.isBlank()) acc = 0
            else acc += line.toInt()
            max = Math.max(max, acc)
        }

    }
    return max
}

fun solve2(input: String): Int {
    var lowMax = 0
    var midMax = 0
    var topMax = 0
    var acc = 0
    input.reader().useLines { lines ->
        for (line in lines + "\n") {
            if (line.isBlank()) {
                var value = acc
                if (value > topMax) {
                    val temp = value
                    value = topMax
                    topMax = temp
                }
                if (value > midMax) {
                    val temp = value
                    value = midMax
                    midMax = temp
                }
                if (value > lowMax) {
                    lowMax = value
                }
                acc = 0
            } else acc += line.toInt()
        }
    }
    return topMax + midMax + lowMax

}

