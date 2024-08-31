package day5

import utils.*

fun main() {
    val sample = readFile("day5/SampleInput")
    val input = readFile("day5/Input")
    solveSample1 { solve1(sample) }
    solveSolution1 { solve1(input) }
    solveSample2 { solve2(sample) }
    solveSolution2 { solve2(input) }
}

fun solve1(input: String): String {
    val crates = mutableListOf<MutableList<Char>>()
    val commands = mutableListOf<String>()

    var flag = false

    for (line in input.lines().iterator()) {
        if (line.startsWith(" 1")) {
            flag = true;
            continue;
        }
        if (flag && line.isNotEmpty()) {
            commands.add(line)
        } else {
            val indexOfCrate = (line.length + 1) / 4
            for (i in 0 until indexOfCrate) {
                for (j in crates.size until indexOfCrate) {
                    crates.add(mutableListOf())
                }
                if (!line[i * 4].isWhitespace())
                    crates[i].add(line[i * 4 + 1])
            }
        }
    }

    for (crate in crates) {
        crate.reverse()
    }

    val regex = Regex("move (.*?) from (.*?) to (.*?)")
    for (command in commands) {
        val res = regex.matchEntire(command)?.groupValues ?: throw Exception("Invalid regex")
        val amount = res[1].toInt()
        val stackFrom = res[2].toInt()
        val stackTo = res[3].toInt()

        for (i in 0 until amount) {
            crates[stackTo - 1].add(crates[stackFrom - 1].removeLast())
        }
    }
    return crates.fold("") { acc, el ->
        acc + el.last()
    }

}

fun solve2(input: String): String {
    val crates = mutableListOf<MutableList<Char>>()
    val commands = mutableListOf<String>()

    var flag = false

    for (line in input.lines().iterator()) {
        if (line.startsWith(" 1")) {
            flag = true;
            continue;
        }
        if (flag && line.isNotEmpty()) {
            commands.add(line)
        } else {
            val indexOfCrate = (line.length + 1) / 4
            for (i in 0 until indexOfCrate) {
                for (j in crates.size until indexOfCrate) {
                    crates.add(mutableListOf())
                }
                if (!line[i * 4].isWhitespace())
                    crates[i].add(line[i * 4 + 1])
            }
        }
    }

    for (crate in crates) {
        crate.reverse()
    }

    val regex = Regex("move (.*?) from (.*?) to (.*?)")
    for (command in commands) {
        val res = regex.matchEntire(command)?.groupValues ?: throw Exception("Invalid regex")
        val amount = res[1].toInt()
        val stackFrom = res[2].toInt()
        val stackTo = res[3].toInt()

        val temp = mutableListOf<Char>()
        for (i in 0 until amount) {
            temp.add(crates[stackFrom - 1].removeLast())
        }
        while (temp.size > 0) {
            crates[stackTo - 1].add(temp.removeLast())
        }
    }
    return crates.fold("") { acc, el ->
        acc + el.last()
    }

}
