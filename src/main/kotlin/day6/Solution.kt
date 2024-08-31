package day6

import utils.*

fun main() {
    val sample = readFile("day6/SampleInput")
    val input = readFile("day6/Input")
    solveSample1 { solve1(sample) }
    solveSolution1 { solve1(input) }
    solveSample2 { solve2(sample) }
    solveSample2 { solve2("bvwbjplbgvbhsrlpgdmjqwftvncz") }
    solveSolution2 { solve2(input) }
}

fun solve1(input: String): Int {
    var left = 0;
    var right = 0;
    while (left <= input.lastIndex) {
        while (right - left < 4) {
            right += 1
        }
        val chars = Array<Char?>(4) { null }
        for (i in 0..3) {
            chars[i] = input[left + i]
        }
        var duplicate = -1
        m@ for (i in 0 until chars.lastIndex) {
            for (j in i + 1..chars.lastIndex) {
                if (chars[i] == chars[j]) {
                    duplicate = i + 1
                    break@m
                }
            }
        }
        if (duplicate == -1) {
            return right
        }
        left += duplicate
    }
    return right

}

fun solve2(input: String): Int {
    var left = 0;
    var right = 0;
    while (left <= input.lastIndex) {
        while (right - left < 14) {
            right += 1
        }
        val chars = Array<Char?>(14) { null }
        for (i in 0..chars.lastIndex) {
            chars[i] = input[left + i]
        }
        var duplicate = -1
        m@ for (i in 0 until chars.lastIndex) {
            for (j in i + 1..chars.lastIndex) {
                if (chars[i] == chars[j]) {
                    duplicate = i + 1
                    break@m
                }
            }
        }
        if (duplicate == -1) {
            return right
        }
        left += duplicate
    }
    return right

}
