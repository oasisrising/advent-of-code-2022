package advent.day09

import advent.utils.println
import advent.utils.readInput
import kotlin.math.abs
import kotlin.math.max

class Point(copy: Point? = null) {
    var x = 0
    var y = 0

    init {
        if (copy != null) {
            x = copy.x
            y = copy.y
        }
    }

    override fun toString(): String {
        return "($x, $y)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Point

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }

}

class Rope(knots: Int) {

    private var rope = List(knots) { Point() }
    private val tailPositions = mutableSetOf<Point>()

    private fun getDistance(knot1: Point, knot2: Point): Int {
        val deltaFirst = abs(knot1.x - knot2.x)
        val deltaLast = abs(knot1.y - knot2.y)

        return (max(deltaLast, deltaFirst))
    }

    private fun moveHead(direction: Char) {
        when (direction) {
            'U' -> rope[0].y++
            'D' -> rope[0].y--
            'L' -> rope[0].x--
            'R' -> rope[0].x++
        }
        for (i in rope.indices) {
            if (i < 1) {
                continue
            }
            if (getDistance(rope[i - 1], rope[i]) > 1) {
                follow(rope[i - 1], rope[i])
            }
        }

    }

    private fun follow(knot1: Point, knot2: Point) {
        val diffX = knot1.x - knot2.x
        if (diffX >= 1) {
            knot2.x++
        } else if (diffX <= -1) {
            knot2.x--
        }
        val diffY = knot1.y - knot2.y
        if (diffY >= 1) {
            knot2.y++
        } else if (diffY <= -1) {
            knot2.y--
        }
    }

    override fun toString(): String {
        return rope.toString()
    }

    fun processMoves(moves: List<String>) {
        tailPositions.add(Point(rope.last()))
        for (move in moves) {

            val spacesToMove = move.substring(2).toInt()
            for (i in 0 until spacesToMove) {
                moveHead(move[0])
                tailPositions.add(Point(rope.last()))
            }
        }
        tailPositions.size.println()
    }
}

fun main() {

    fun part1(input: List<String>) {
        val rope = Rope(2)
        rope.processMoves(input)
    }

    fun part2(input: List<String>) {
        val rope = Rope(10)
        rope.processMoves(input)
    }

    val testInput = readInput("advent/day09/input")

    part1(testInput)
    part2(testInput)
}

