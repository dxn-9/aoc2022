package day3

import utils.*

fun main() {
    val sample = readFile("day3/SampleInput")
    val input = readFile("day3/Input")

    solveSample1 { solve1(sample) }
    solveSolution1 { solve1(input) }

    solveSample2 { solve2(sample) }
    solveSolution2 { solve2(input) }

}


val lowerCase = "abcdefghijklmnopqrstuvwxyz"
val chars = "$lowerCase${lowerCase.uppercase()}"

data class Rucksack(val str: String) {
    val compartment1: String;
    val compartment2: String;

    init {
        val middle = str.length / 2
        compartment1 = str.slice(0 until middle)
        compartment2 = str.slice(middle..str.lastIndex)
    }


    fun getCommonPoints(): Int {
        val map = mutableMapOf<Char, Boolean>()
        val commonChars = mutableListOf<Char>()
        for (c in compartment1.toCharArray()) {
            map[c] = true
        }
        for (c in compartment2.toCharArray()) {
            if (map.getOrDefault(c, false)) {
                commonChars.add(c)
                map.remove(c)
            }
        }

        return commonChars.fold(0) { acc, element ->
            acc + chars.indexOf(element) + 1
        }

    }

    override fun toString(): String {
        return "$compartment1 $compartment2"
    }

}

fun solve1(input: String): Int {
    var points = 0
    for (line in input.lines()) {
        val ruckSack = Rucksack(line)
        points += ruckSack.getCommonPoints()
    }
    return points

}

fun solve2(input: String): Int {
    var points = 0;

    val lines = input.lines()

    for (groupIndex in 0 until lines.size / 3) {
        val map = mutableMapOf<Char, Int>()

        for (i in 0..2) {
            val elf = lines[groupIndex * 3 + i]
            for (c in elf.toCharArray()) {
                if (map[c] == null) {
                    if (i == 0) map[c] = 0
                    else continue
                } else {
                    if (map[c] != i - 1) continue
                    else map[c] = i
                }
            }
        }

        for ((key, value) in map.entries) {
            if (value == 2) {
                points += chars.indexOf(key) + 1
                break;
            }

        }
    }

    return points
}
