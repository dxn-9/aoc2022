package day4

import utils.*

fun main() {
    val sample = readFile("day4/SampleInput")
    val input = readFile("day4/Input")

    solveSample1 { solve1(sample) }
    solveSolution1 { solve1(input) }

    solveSample2 { solve2(sample) }
    solveSolution2 { solve2(input) }
}


data class ElfSection(val range: IntRange)

fun Pair<ElfSection, ElfSection>.areContained(): Boolean =
    this.first.range.first <= this.second.range.first && this.first.range.last >= this.second.range.last ||
            this.second.range.first <= this.first.range.first && this.second.range.last >= this.first.range.last

fun Pair<ElfSection, ElfSection>.areOverlapping(): Boolean =
    if (this.areContained()) true else {
        // Basically doing AABB :D
        this.second.range.first >= this.first.range.first && this.second.range.first <= this.first.range.last ||
                this.second.range.last >= this.first.range.first && this.second.range.last <= this.first.range.last
    }


fun solve1(input: String): Int {

    var count = 0
    for (line in input.lines()) {
        val (firstElf, secondElf) = line.split(",").map {
            val (start, end) = it.split("-")
            ElfSection(start.toInt()..end.toInt())
        }
        val pair = Pair(firstElf, secondElf)
        if (pair.areContained()) {
            count += 1
        }
    }
    return count
}

fun solve2(input: String): Int {

    var count = 0
    for (line in input.lines()) {
        val (firstElf, secondElf) = line.split(",").map {
            val (start, end) = it.split("-")
            ElfSection(start.toInt()..end.toInt())
        }
        val pair = Pair(firstElf, secondElf)
        if (pair.areOverlapping()) {
            count += 1
        }
    }
    return count
}

