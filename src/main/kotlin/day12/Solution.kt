package day12

import utils.*
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

fun main() {
    val sample = readFile("day12/SampleInput")
    val input = readFile("day12/Input")

    solveSample1 { solve1(sample) }
    solveSolution1 { solve1(input) }

    solveSample2 { solve2(sample) }
    solveSolution2 { solve2(input) }
}


val alphabet = "abcdefghijklmnopqrstuvwxyz"

data class Node(val value: Int, val position: Pair<Int, Int>) {
    val nodes = mutableListOf<Node>()
}

// Is this even a graph if it has head and tail? XD
class Graph(val head: Node, val tail: Node) {

    val possibleStarting = mutableListOf<Node>(head)

    companion object {
        fun createGraph(matrix: List<List<Int>>, startPosition: Pair<Int, Int>, endPosition: Pair<Int, Int>): Graph {
            val map = mutableMapOf<Pair<Int, Int>, Node>()
            val head = Node(matrix[startPosition.first][startPosition.second], startPosition)
            map[startPosition] = head
            val tail = Node(matrix[endPosition.first][endPosition.second], endPosition)
            map[endPosition] = tail
            val graph = Graph(head, tail)

            for ((i) in matrix.withIndex()) {
                for ((j, col) in matrix[i].withIndex()) {
                    val position = Pair(i, j)
                    if (position == startPosition || position == endPosition) continue
                    val node = Node(col, position)
                    map[position] = node
                    if (col == 0) graph.possibleStarting.add(node)
                }
            }


            for (node in map.values) {
                val neighbors = listOf(
                    Pair(node.position.first - 1, node.position.second),
                    Pair(node.position.first + 1, node.position.second),
                    Pair(node.position.first, node.position.second - 1),
                    Pair(node.position.first, node.position.second + 1)
                )
                for (neighborPos in neighbors) {
                    val neighbor = map[neighborPos]
                    if (neighbor != null && neighbor.value <= node.value + 1) {
                        node.nodes.add(neighbor)
                    }
                }
            }

            return graph
        }
    }

}


fun reconstruct_path(cameFrom: Map<Node, Node>, current: Node): List<Node> {
    var c = current
    val total_path = mutableListOf(c)
    while (cameFrom.keys.contains(c)) {
        c = cameFrom[c]!!
        total_path.addFirst(c)
    }
    return total_path

}

// https://en.wikipedia.org/wiki/A*_search_algorithm
fun A_STAR(start: Node, goal: Node, h: (node: Node) -> Double): List<Node> {
    val openSet = mutableListOf<Node>(start)
    val cameFrom = mutableMapOf<Node, Node>()
    val gScore = mutableMapOf<Node, Double>().withDefault { Double.MAX_VALUE }
    gScore[start] = 0.0
    val fScore = mutableMapOf<Node, Double>().withDefault { Double.MAX_VALUE }
    fScore[start] = h(start)

    while (openSet.isNotEmpty()) {
        val current = openSet.sortedBy { fScore[it] }[0]
        if (current == goal) {
            return reconstruct_path(cameFrom, current);
        }

        openSet.remove(current)

        for (neighbor in current.nodes) {
            val tentative_gScore = gScore[current]!! + 1;
            if (tentative_gScore < gScore.getValue(neighbor)) {
                cameFrom[neighbor] = current
                gScore[neighbor] = tentative_gScore
                fScore[neighbor] = tentative_gScore + h(neighbor)
                if (!openSet.contains(neighbor)) {
                    openSet.add(neighbor)
                }
            }

        }
    }
    return listOf()
}

fun Pair<Int, Int>.calcDist(other: Pair<Int, Int>): Double {
    return sqrt(
        (this.first.toDouble() - other.first).pow(2) + (this.second.toDouble() - other.second).pow(2)
    )
}


fun solve1(input: String): Int {
    var startPosition: Pair<Int, Int>? = null
    var endPosition: Pair<Int, Int>? = null

    val matrix = mutableListOf<MutableList<Int>>()

    val lines = input.lines()
    for ((i, row) in lines.withIndex()) {
        val list = mutableListOf<Int>()
        for ((j, char) in row.withIndex()) {
            val value = when (char) {
                'S' -> {
                    startPosition = Pair(i, j); alphabet.indexOf("a")
                }

                'E' -> {
                    endPosition = Pair(i, j); alphabet.indexOf("z")
                }

                else -> alphabet.indexOf(char)
            }

            list.add(value)
        }
        matrix.add(list)
    }

    val graph = Graph.createGraph(matrix, startPosition!!, endPosition!!)
    val result = A_STAR(
        graph.head,
        graph.tail,
        { node ->
            node.position.calcDist(endPosition)
        })

    return result.size - 1

}

fun solve2(input: String): Int {
    var startPosition: Pair<Int, Int>? = null
    var endPosition: Pair<Int, Int>? = null

    val matrix = mutableListOf<MutableList<Int>>()

    val lines = input.lines()
    for ((i, row) in lines.withIndex()) {
        val list = mutableListOf<Int>()
        for ((j, char) in row.withIndex()) {
            val value = when (char) {
                'S' -> {
                    startPosition = Pair(i, j); alphabet.indexOf("a")
                }

                'E' -> {
                    endPosition = Pair(i, j); alphabet.indexOf("z")
                }

                else -> alphabet.indexOf(char)
            }

            list.add(value)
        }
        matrix.add(list)
    }

    val graph = Graph.createGraph(matrix, startPosition!!, endPosition!!)
    val results = graph.possibleStarting.map {
        A_STAR(
            it,
            graph.tail,
            { node ->
                node.position.calcDist(endPosition)
            })
    }

    return results.filter { it.size != 0 }.minBy { it.size }.size - 1

}
