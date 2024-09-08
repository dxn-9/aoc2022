package day13

import utils.*

fun main() {
    val sample = readFile("day13/SampleInput")
    val input = readFile("day13/Input")

    solveSample1 { solve1(sample) }
    solveSolution1 { solve1(input) }
    solveSample2 { solve2(sample) }
    solveSolution2 { solve2(input) }
}


data class Packet(val data: Any) {

    fun isInt(): Boolean {
        return this.data is Int
    }

    fun isList(): Boolean {
        return this.data is List<*>
    }

    companion object {
        fun fromString(str: String): List<Packet> {
            val mainList = mutableListOf<Packet>()
            var currentList: MutableList<Packet>? = mainList
            var listStack = mutableListOf<MutableList<Packet>>(mainList)

            var accumulatedStr = ""
            for (char in str.toCharArray().drop(1)) {
                if (char == '[') {
                    val newList = mutableListOf<Packet>()
                    currentList!!.add(Packet(newList))
                    listStack.add(newList)
                    currentList = newList
                } else if (char == ']') {
                    if (accumulatedStr.isNotEmpty()) {
                        currentList!!.add(Packet(accumulatedStr.toInt()))
                        accumulatedStr = ""
                    }
                    listStack.removeLast()
                    currentList = listStack.lastOrNull()

                } else if (char == ',') {
                    if (accumulatedStr.isNotEmpty()) {
                        currentList!!.add(Packet(accumulatedStr.toInt()))
                        accumulatedStr = ""
                    }
                    continue
                } else {
                    accumulatedStr += char
                }
            }
            return mainList
        }

    }
}

enum class ReturnStatus {
    WrongOrder,
    RightOrder,
    Continue
}

fun compareLists(p1: List<Packet>, p2: List<Packet>): ReturnStatus {
    var l = 0

    while (l < p1.size) {
        if (p2.lastIndex < l) return ReturnStatus.WrongOrder
        if (p1[l].isInt() && p2[l].isInt()) {
            val v1 = p1[l].data as Int
            val v2 = p2[l].data as Int

            if (v1 < v2) return ReturnStatus.RightOrder
            if (v1 > v2) return ReturnStatus.WrongOrder
        } else if (p1[l].isList() && p2[l].isInt()) {
            val r = compareLists(listOf(p1[l]), listOf(Packet(listOf(p2[l]))))
            if (r != ReturnStatus.Continue) return r
        } else if (p1[l].isInt() && p2[l].isList()) {
            val r = compareLists(listOf(Packet(listOf(p1[l]))), listOf(p2[l]))
            if (r != ReturnStatus.Continue) return r
        } else {
            // Both are lists
            val r = compareLists(p1[l].data as List<Packet>, p2[l].data as List<Packet>)
            if (r != ReturnStatus.Continue) return r
        }
        l++
    }

    if (l <= p2.lastIndex) return ReturnStatus.RightOrder

    return ReturnStatus.Continue
}


fun solve1(input: String): Int {
    val iter = input.lines().iterator()

    var rightOrder = 0
    var indices = 1

    while (iter.hasNext()) {
        val leftPacket = iter.next()
        val rightPacket = iter.next()

        val leftPackets = Packet.fromString(leftPacket)
        val rightPackets = Packet.fromString(rightPacket)

        val result = compareLists(leftPackets, rightPackets)
        when (result) {
            ReturnStatus.Continue -> throw Exception("It should not return continue")
            ReturnStatus.RightOrder ->
                rightOrder += indices


            ReturnStatus.WrongOrder -> {}
        }

        if (iter.hasNext()) iter.next() // Skip new line
        indices++
    }


    return rightOrder
}

fun solve2(input: String): Int {
    val iter = input.lines().iterator()

    val packets = mutableListOf<List<Packet>>()

    while (iter.hasNext()) {
        val leftPacket = iter.next()
        val rightPacket = iter.next()

        val leftPackets = Packet.fromString(leftPacket)
        val rightPackets = Packet.fromString(rightPacket)

        packets.addAll(listOf(leftPackets, rightPackets))

        if (iter.hasNext()) iter.next() // Skip new line
    }
    val comparator = Comparator { packet1: List<Packet>, packet2: List<Packet> ->
        when (compareLists(packet1, packet2)) {
            ReturnStatus.Continue -> throw Exception("It should not return continue")
            ReturnStatus.RightOrder -> -1
            ReturnStatus.WrongOrder -> +1
        }

    }


    val dividers = listOf(listOf(Packet(listOf(Packet(2)))), listOf(Packet(listOf(Packet(6)))))
    packets.addAll(dividers)
    packets.sortWith(comparator)


    return (packets.indexOf(dividers[0]) + 1) * (packets.indexOf(dividers[1]) + 1)

}
