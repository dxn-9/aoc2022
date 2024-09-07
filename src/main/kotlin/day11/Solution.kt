package day11

import utils.*
import kotlin.math.floor


class Monkey(
    val items: MutableList<Long>,
    private val _operation: Monkey.() -> Unit,
    private val _test: Monkey.() -> Unit
) {
    var divisor = 0


    constructor(
        items: MutableList<Long>, _operation: Monkey.() -> Unit,
        _test: Monkey.() -> Unit,
        divisor: Int
    ) : this(items, _operation, _test) {
        this.divisor = divisor
    }

    var inspected = 0
    fun isDone(): Boolean =
        items.isEmpty()

    fun operation() {
        inspected += 1
        _operation()
    }

    fun test() {
        _test()
    }

    override fun toString(): String {
        return "Monkey(${items.joinToString(",")})"
    }
}

fun main() {
    val sample = readFile("day11/SampleInput")
    val input = readFile("day11/Input")
    solveSample1 { solve1(sample) }
    solveSolution1 { solve1(input) }
    solveSample2 { solve2(sample) }
    solveSolution2 { solve2(input) }
}

fun solve1(input: String): Int {
    val monkeysData = input.split("Monkey")
        .drop(1) /* Have to initialize them as null because i want to use a closure for the test. That closure will capture the monkeys array. */;
    val monkeys = Array<Monkey?>(monkeysData.count()) { null }; monkeysData.onEachIndexed { index, data ->
        monkeys[index] = data.lines().let {
            val items = it[1].split(":")[1].split(",").map(String::trim).map(String::toLong).toMutableList()
            val operation = it[2].split("=")[1]
            val operator = if (operation.contains("+")) "+" else "*"; val (op1, op2) = operation.split(operator)
            .map(String::trim)
            val testDivisor = it[3].split("by")[1].trim().toInt()
            val trueMonkey = it[4].split("throw to monkey")[1].trim().toInt()
            val falseMonkey = it[5].split("throw to monkey")[1].trim().toInt()

            Monkey(items, {
                val _op1 = if (op1 == "old") items[0] else op1.toLong()
                val _op2 = if (op2 == "old") items[0] else op2.toLong()

                items[0] = when (operator) {
                    "+" -> _op1 + _op2
                    "*" -> _op1 * _op2
                    else -> throw Exception("Invalid operator")
                }
                items[0] = floor(items[0].toDouble() / 3.0).toLong()
            }, {
                val item = items.removeFirst()

                if (item % testDivisor == 0L) {
                    monkeys[trueMonkey]!!.items.add(item)
                } else {
                    monkeys[falseMonkey]!!.items.add(item)
                }

            })
        }
    }

    repeat(20) {
        for (monkey in monkeys) {
            if (monkey == null) throw Exception("Uninitialized monkey! 🍌")
            while (!monkey.isDone()) {
                monkey.operation()
                monkey.test()
            }
        }
    }

    val top2Monkeys = arrayOf(0, 0)
    for (monkey in monkeys) {
        if (monkey!!.inspected > top2Monkeys[0]) {
            top2Monkeys[0] = monkey.inspected
        }

        if (top2Monkeys[0] > top2Monkeys[1]) {
            val t = top2Monkeys[0]
            top2Monkeys[0] = top2Monkeys[1]
            top2Monkeys[1] = t
        }
    }

    return top2Monkeys[0] * top2Monkeys[1]
}

fun solve2(input: String): Long {
    val monkeysData = input.split("Monkey").drop(1)
    // Have to initialize them as null because i want to use a closure for the test. That closure will capture the monkeys array.
    val monkeys = Array<Monkey?>(monkeysData.count()) {
        null
    }
    var commonDivisor = 0

    monkeysData.onEachIndexed { index, data ->
        monkeys[index] = data.lines().let {
            val items = it[1].split(":")[1].split(",").map(String::trim).map(String::toLong).toMutableList()
            val operation = it[2].split("=")[1]
            val operator = if (operation.contains("+")) {
                "+"
            } else {
                "*"
            }
            val (op1, op2) = operation.split(operator).map(String::trim)
            val testDivisor = it[3].split("by")[1].trim().toInt()
            val trueMonkey = it[4].split("throw to monkey")[1].trim().toInt()
            val falseMonkey = it[5].split("throw to monkey")[1].trim().toInt()

            Monkey(items, {
                val _op1 = if (op1 == "old") items[0] else op1.toLong()
                val _op2 = if (op2 == "old") items[0] else op2.toLong()

                items[0] = when (operator) {
                    "+" -> _op1 + _op2
                    "*" -> _op1 * _op2
                    else -> throw Exception("Invalid operator")
                }
            }, {
                val item = items.removeFirst()
                if (item % testDivisor == 0L) {

                    monkeys[trueMonkey]!!.items.add(item % commonDivisor)
                } else {
                    monkeys[falseMonkey]!!.items.add(item % commonDivisor)
                }

            }, testDivisor)
        }
    }
    commonDivisor = monkeys.fold(1) { acc, el -> acc * el!!.divisor }


    repeat(10000) {

        for (monkey in monkeys) {
            if (monkey == null) throw Exception("Uninitialized monkey! 🍌")
            while (!monkey.isDone()) {
                monkey.operation()
                monkey.test()
            }
        }
    }

    val top2Monkeys = arrayOf(0, 0)
    for (monkey in monkeys) {
        if (monkey!!.inspected > top2Monkeys[0]) {
            top2Monkeys[0] = monkey.inspected
        }

        if (top2Monkeys[0] > top2Monkeys[1]) {
            val t = top2Monkeys[0]
            top2Monkeys[0] = top2Monkeys[1]
            top2Monkeys[1] = t
        }
    }

    return top2Monkeys[0] * top2Monkeys[1].toLong()
}
