package day7

import utils.*


fun main() {
    val sample = readFile("day7/SampleInput")
    val input = readFile("day7/Input")
    solveSample1 { solve1(sample) }
    solveSolution1 { solve1(input) }
    solveSample2 { solve2(sample) }
    solveSolution2 { solve2(input) }
}


interface Entry
data class File(val name: String, val size: Int) : Entry
data class Directory(val name: String, val children: MutableList<Entry>, val previousDir: Directory? = null) :
    Entry {
    override fun toString(): String {
        return "Directory($name, $children, ${previousDir?.name})"
    }

    fun getSize(): Int {
        return children.fold(0) { acc, entry -> acc + if (entry is File) entry.size else (entry as Directory).getSize() }

    }
}


fun solve1(input: String): Int {
    val rootDir = Directory("/", mutableListOf())
    var currentDir = rootDir

    val lines = input.lines().iterator()

    lines.let {
        // Skip first command.
        it.next()

        while (it.hasNext()) {
            val line = it.next()
            if (line == "$ ls") {
                continue
            }
            if (line.startsWith("$")) {
                val name = line.slice(5..line.lastIndex)
                if (name == "..") {
                    currentDir = currentDir.previousDir ?: throw Exception("Cannot find previous dir")
                } else {
                    currentDir =
                        currentDir.children.find { entry -> entry is Directory && entry.name == name } as Directory
                }
                continue

            }

            val components = line.split(" ").map(String::trim)
            if (components[0] == "dir") {
                currentDir.children.add(Directory(components[1], mutableListOf(), currentDir))
            } else {
                currentDir.children.add(File(components[1], components[0].toInt()))
            }
        }
    }

    val dirsSizes = mutableListOf<Int>()
    traverse(rootDir, dirsSizes)


    return dirsSizes.sum()
}

fun solve2(input: String): Int {
    val rootDir = Directory("/", mutableListOf())
    var currentDir = rootDir

    val lines = input.lines().iterator()

    lines.let {
        // Skip first command.
        it.next()

        while (it.hasNext()) {
            val line = it.next()
            if (line == "$ ls") {
                continue
            }
            if (line.startsWith("$")) {
                val name = line.slice(5..line.lastIndex)
                if (name == "..") {
                    currentDir = currentDir.previousDir ?: throw Exception("Cannot find previous dir")
                } else {
                    currentDir =
                        currentDir.children.find { entry -> entry is Directory && entry.name == name } as Directory
                }
                continue

            }

            val components = line.split(" ").map(String::trim)
            if (components[0] == "dir") {
                currentDir.children.add(Directory(components[1], mutableListOf(), currentDir))
            } else {
                currentDir.children.add(File(components[1], components[0].toInt()))
            }
        }
    }

    val rootDirSize = rootDir.getSize()
    val unusedSpace = 70000000 - rootDirSize
    val neededSpace = 30000000 - unusedSpace


    return traverse2(rootDir, neededSpace, rootDirSize)
}

fun traverse(dir: Directory, dirsSizes: MutableList<Int>) {
    val size = dir.getSize()
    if (size <= 100000) dirsSizes.add(size)

    for (child in dir.children) {
        if (child is Directory) {
            traverse(child, dirsSizes)
        }
    }
}

fun traverse2(dir: Directory, minTarget: Int, current: Int): Int {
    val size = dir.getSize()
    var c = current
    if (size >= minTarget && size < current) {
        c = size
    }

    for (child in dir.children) {
        if (child is Directory) {
            c = traverse2(child, minTarget, c)
        }
    }
    return c
}

