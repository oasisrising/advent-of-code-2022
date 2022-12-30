package advent.day12

import advent.colors.bright_green
import advent.colors.dark_yellow
import advent.colors.print
import advent.colors.reset
import advent.utils.println
import advent.utils.readInput

typealias Point = Pair<Int, Int>

class Mountain(private val topography: List<String>) {
    private var visitedNodes = Array(topography.size) { Array(topography[0].length) { Int.MAX_VALUE } }
    private var unvisitedSet: MutableSet<Point> = mutableSetOf()
    var possibleStartingPoints: MutableList<Point> = mutableListOf()
    private var start: Point = Point(0, 0)
    private var end: Point = Point(0, 0)
    private var rows = topography.size
    private var columns = topography[0].length
    private var startingUnvisitedSet: MutableSet<Point> = mutableSetOf()

    init {
        // find the start and end
        for (row in topography.indices) {
            for (column in topography[row].indices) {
                unvisitedSet.add(Point(row, column))
                if (topography[row][column] == 'S') {
                    start = Point(row, column)
                    possibleStartingPoints.add(start)
                }
                if (topography[row][column] == 'E') {
                    end = Point(row, column)
                    // end.println()
                }
                if (topography[row][column] == 'a') {
                    possibleStartingPoints.add(Point(row, column))
                }
            }
        }
        startingUnvisitedSet = unvisitedSet.toMutableSet()
    }

    fun reset() {
        visitedNodes = Array(topography.size) { Array(topography[0].length) { Int.MAX_VALUE } }
        unvisitedSet = startingUnvisitedSet.toMutableSet()
    }

    fun printMe() {
        for (line in topography) {
            for (j in line) {
                print(j.print(j.hashCode()))
            }
            println("")
        }
    }

    private fun isInBounds(point: Point): Boolean {
        return (point.first in 0 until rows && point.second in 0 until columns)
    }

    private fun findNextStepPossibilities(position: Point): List<Point> {
        val row = position.first
        val column = position.second

        val adjacentMoves = listOf(
            Point(row - 1, column),
            Point(row + 1, column),
            Point(row, column + 1),
            Point(row, column - 1)
        )

        return adjacentMoves.filter { isInBounds(it) && canStepTo(it, position) && unvisitedSet.contains(it) }
    }

    private fun canStepTo(nextStep: Point, currentStep: Point): Boolean {
        var currentValue = topography[currentStep.first][currentStep.second]
        if (currentValue == 'S') {
            currentValue = 'a'
        }
        if (currentValue == 'E') {
            currentValue = 'z'
        }
        var value = topography[nextStep.first][nextStep.second]
        if (value == 'S') {
            value = 'a'
        }
        if (value == 'E') {
            value = 'z'
        }
        return (value.hashCode() <= currentValue.hashCode() + 1)
    }

    override fun toString(): String {
        var output = StringBuilder()
        for (row in topography.indices) {
            for (column in topography[row].indices) {
                val value = topography[row][column]
                if (visitedNodes[row][column] < Int.MAX_VALUE) {
                    if (unvisitedSet.contains(Point(row, column))) {
                        output.append(bright_green + value + reset)
                    } else {
                        output.append(dark_yellow + value + reset)
                    }
                } else {
                    output.append(value.print(value.hashCode()))
                }
            }
            output.append("\n")
        }

        return output.toString()
    }

    fun findPath(position: Point = start): Int {
        visitedNodes[position.first][position.second] = 0

        while (unvisitedSet.isNotEmpty()) {
            // find the node in unvisited with the smallest dist.

            var currentStep =
                unvisitedSet.sortedWith(compareBy<Point> { visitedNodes[it.first][it.second] }.thenBy { it.first }
                    .thenBy { it.second }).first()
            unvisitedSet.remove(currentStep)

            if (currentStep == end) {
                return visitedNodes[currentStep.first][currentStep.second]
            }

            var possibleNextSteps = findNextStepPossibilities(currentStep)
            for (step in possibleNextSteps) {
                var currentStepDepth = visitedNodes[currentStep.first][currentStep.second] + 1
                if (currentStepDepth < 0) {
                    return Int.MAX_VALUE
                }
                if (currentStepDepth < visitedNodes[step.first][step.second]) {
                    visitedNodes[step.first][step.second] = currentStepDepth
                }
            }
        }
        return Int.MAX_VALUE
    }
}

fun main() {

    fun part1(input: List<String>) {

        val mountain = Mountain(input)
        mountain.printMe()
        mountain.findPath()
    }

    fun part2(input: List<String>) {
        val mountain = Mountain(input)
        mountain.printMe()
        var min = Int.MAX_VALUE
        for (point in mountain.possibleStartingPoints) {
            var pathLength = mountain.findPath(point)
            if (pathLength < min)
                min = pathLength
            mountain.reset()
        }
        min.println()
    }

    val testInput = readInput("advent/day12/input")

    // part1(testInput)
    part2(testInput)
}

