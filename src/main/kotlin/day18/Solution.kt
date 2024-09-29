package day18

import utils.*

fun main() {
    val sample = readFile("day18/SampleInput")
    val input = readFile("day18/Input")
    solveSample1 { solve1(sample) }
    solveSolution1 { solve1(input) }
    solveSample2 { solve2(sample) }
    solveSolution2 { solve2(input) }

}


data class Cube(val x: Int, val y: Int, val z: Int)

fun solve1(input: String): Int {

    val cubes = input.lines().map {
        it.split(',').let {
            Cube(it[0].toInt(), it[1].toInt(), it[2].toInt())
        }
    }

    var culls = 0
    // Cubes with X Axis - Note: this is n^2. An optimization would be to create a map when parsing the input and classify the cubes by X, Y, Z
    cubes.forEachIndexed { index, cube ->
        cubes.forEach { other ->
            if (other == cube) return@forEach
            if (other.y == cube.y && other.z == cube.z && other.x == (cube.x - 1)) culls++
            if (other.x == cube.x && other.z == cube.z && other.y == (cube.y - 1)) culls++
            if (other.x == cube.x && other.y == cube.y && other.z == (cube.z - 1)) culls++

        }
    }


    return cubes.size * 6 - culls * 2
}

fun solve2(input: String): Int {
    val cubes = input.lines().map {
        it.split(',').let {
            Cube(it[0].toInt(), it[1].toInt(), it[2].toInt())
        }
    }

    /**
     * Now we have to create a better representation of the data.
     * The idea is to create a 3d matrix representing the cubes: iterate through all the 1x1x1 voxels and check for air gaps.
     */

    val xMax = cubes.maxOf { it.x }
    val yMax = cubes.maxOf { it.y }
    val zMax = cubes.maxOf { it.z }

    val space = Array(xMax + 1) { Array(yMax + 1) { Array(zMax + 1) { false } } }
    cubes.forEach { space[it.x][it.y][it.z] = true }

    /** To visualize
     *
    for (z in 0..<zMax) {
    for (y in 0..<yMax) {
    for (x in 0..<xMax) {
    if (space[x][y][z]) print("x")
    else print(".")
    }
    println()
    }
    println("-----")
    }
     */

    val visitedAir = HashSet<Triple<Int, Int, Int>>()

    var culls = 0
    var insideFaces = 0

    space.forEachIndexed { xi, x ->
        x.forEachIndexed { yi, y ->
            y.forEachIndexed zForEach@{ zi, z ->
                if (z) {
                    // It's a block. Check if it has neighbours
                    if (space[xi][yi].getOrElse(zi - 1) { false }) culls++
                    if (space[xi].getOrElse(yi - 1) { arrayOf() }.getOrElse(zi) { false }) culls++
                    if (space.getOrElse(xi - 1) { arrayOf() }.getOrElse(yi) { arrayOf() }
                            .getOrElse(zi) { false }) culls++
                } else {
                    // It's air. Check if it belongs to an enclosed block. Skip the ones we already have checked.
                    if (visitedAir.contains(Triple(xi, yi, zi))) return@zForEach
                    try {
                        val localAir = HashSet<Triple<Int, Int, Int>>()
                        insideFaces -= processAir(visitedAir, localAir, space, xi, yi, zi)
                        localAir.forEach(visitedAir::add)
                    } catch (ex: Exception) {
                        // There was an OOB access, so it means it's not confined by blocks
                        visitedAir.add(Triple(xi, yi, zi))
                    }


                }
            }

        }
    }
    return cubes.size * 6 - culls * 2 - insideFaces

}


// Returns the internal faces
fun processAir(
    visitedAir: HashSet<Triple<Int, Int, Int>>,
    localAir: HashSet<Triple<Int, Int, Int>>,
    space: Array<Array<Array<Boolean>>>,
    x: Int,
    y: Int,
    z: Int
): Int {
    if (space[x][y][z]) {
        return -1
    }
    if (visitedAir.contains(Triple(x, y, z))) {
        throw Exception("This means it's connected to air!")
    }
    if (localAir.contains(Triple(x, y, z))) {
        return 0
    }
    localAir.add(Triple(x, y, z))
    var toSub = 0

    (0..2).forEach {
        arrayOf(1, -1).forEach { offset ->
            val xOffset = if (it == 0) offset else 0
            val yOffset = if (it == 1) offset else 0
            val zOffset = if (it == 2) offset else 0
            toSub += processAir(visitedAir, localAir, space, x + xOffset, y + yOffset, z + zOffset)
        }
    }

    return toSub
}