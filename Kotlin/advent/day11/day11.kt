package advent.day11

import advent.utils.println
import advent.utils.readInput


class Monkey(input: List<String>) {
    private var testNumber: Long = 0
    private var index: Long = 0
    private var items = mutableListOf<Long>()
    private var operator: String = ""
    private var modifier = ""
    private var trueMonkeyTarget = 0
    private var falseMonkeyTarget = 0
    var inspectionCount = 0L

    init {
        val monkeyPattern = Regex("Monkey (\\d+)")
        val startingItemsPattern = Regex("Starting items: (.*)")
        val operationPattern = Regex("Operation: new = old (\\S+) (.+)")
        val testPattern = Regex("Test: divisible by (\\d+)")
        val truePattern = Regex("If true: throw to monkey (\\d+)")
        val falsePattern = Regex("If false: throw to monkey (\\d+)")

        for (line in input) {
            if (monkeyPattern.containsMatchIn(line)) {
                val (name) = monkeyPattern.find(line)!!.destructured
                this.index = name.toLong()
            }
            if (startingItemsPattern.containsMatchIn(line)) {
                val (itemsAsString) = startingItemsPattern.find(line)!!.destructured
                val items = itemsAsString.split(", ").map { it.toLong() }
                this.items = items.toMutableList()
            }
            if (operationPattern.containsMatchIn(line)) {
                val (operator, modifier) = operationPattern.find(line)!!.destructured
                this.operator = operator
                this.modifier = modifier
            }
            if (testPattern.containsMatchIn(line)) {
                val (testNumber) = testPattern.find(line)!!.destructured
                this.testNumber = testNumber.toLong()
            }
            if (truePattern.containsMatchIn(line)) {
                val (trueMonkeyTarget) = truePattern.find(line)!!.destructured
                this.trueMonkeyTarget = trueMonkeyTarget.toInt()
            }
            if (falsePattern.containsMatchIn(line)) {
                val (falseMonkeyTarget) = falsePattern.find(line)!!.destructured
                this.falseMonkeyTarget = falseMonkeyTarget.toInt()
            }
        }
    }

    override fun toString(): String {
        return "Monkey: $index\n" +
                "$items\n" +
                "$inspectionCount\n"
    }

    fun getNextItem(): Long {
        return items.removeAt(0)
    }

    fun hasItems(): Boolean {
        return items.isNotEmpty()
    }

    fun inspectItem(item: Long): Long {
        inspectionCount++
        var value = if (modifier == "old") item else modifier.toLong()

        when (operator) {
            "*" -> return item * value
            "+" -> return item + value
            "-" -> return item - value
            "/" -> return item / value
        }
        return item
    }


    fun test(item: Long): Int {
        return if (item % testNumber == 0L) trueMonkeyTarget else falseMonkeyTarget
    }

    fun catch(item: Long) {
        items.add(item)
    }

    fun getTestNumber(): Long {
        return this.testNumber
    }
}

class Game(input: List<String>, private val worryLevelDivider: Long) {
    private val monkeys = mutableListOf<Monkey>()
    private var moduloTrickery = 1L

    init {
        for (lineChunk in input.chunked(7)) {
            var monkey = Monkey(lineChunk)
            moduloTrickery *= monkey.getTestNumber()
            monkeys.add(monkey)
        }
        moduloTrickery.println()
        // monkeys.println()
    }

    fun executeRounds(rounds: Int) {
        for (i in 0 until rounds) {
            for (monkey in monkeys) {
                while (monkey.hasItems()) {
                    var itemWorryLevel = monkey.getNextItem()
                    if (itemWorryLevel < 0)
                        throw Exception()
                    itemWorryLevel = monkey.inspectItem(itemWorryLevel)
                    itemWorryLevel %= moduloTrickery
                    var throwTo = monkey.test(itemWorryLevel)
                    monkeys[throwTo].catch(itemWorryLevel)
                }
            }
            // println("After round $i")
            // monkeys.println()
        }
    }

    fun getMonkeyBusiness(): Long {
        monkeys.sortByDescending { it.inspectionCount }
        return monkeys[0].inspectionCount * monkeys[1].inspectionCount
    }

    override fun toString(): String {
        return monkeys.toString()
    }
}

fun main() {

    fun part1(input: List<String>) {
        val game = Game(input, 3)
        game.executeRounds(20)
        game.getMonkeyBusiness().println()
    }

    fun part2(input: List<String>) {
        val game = Game(input, 1L)
        // game.println()
        game.executeRounds(10000)
        game.println()
        game.getMonkeyBusiness().println()
    }

    val testInput = readInput("advent/day11/input")

    // part1(testInput)
    part2(testInput)
}

