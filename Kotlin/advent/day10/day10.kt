package advent.day10

import advent.utils.println
import advent.utils.readInput


open class CPU {
    private val interestingCycles = listOf(20, 60, 100, 140, 180, 220)
    var register = 1
    private var cycle = 1
    var totalSignalStrength = 0

    open fun advanceCycle() {
        if (interestingCycles.contains(cycle)) {
            totalSignalStrength += cycle * register
        }
        cycle++
    }

    fun processCommand(command: String) {
        if (command[0] == 'n') {
            advanceCycle()
            return
        }
        // otherwise its addx
        val value = command.split(' ')[1].toInt()
        advanceCycle()
        advanceCycle()
        register += value
    }
}

class CRT : CPU() {
    private val lineLength = 39
    private var currentPixel = 0

    override fun advanceCycle() {
        if (currentPixel in register - 1..register + 1) {
            print('#')
        } else print('.')
        if (currentPixel == lineLength) {
            print('\n')
            currentPixel = 0
        } else currentPixel++
    }
}

fun main() {

    fun part1(input: List<String>) {
        val cpu = CPU()
        for (line in input) {
            cpu.processCommand(line)
        }
        cpu.totalSignalStrength.println()
    }

    fun part2(input: List<String>) {

        val crt = CRT()
        for (line in input) {
            crt.processCommand(line)
        }
    }

    val testInput = readInput("advent/day10/input")

    part1(testInput)
    part2(testInput)
}

