package advent.day08

import advent.utils.println
import advent.utils.readInput


class Forest(private val input: List<String>) {
    private val rowIndexesFromLeft = mutableListOf<List<Int>>()
    private val rowIndexesFromRight = mutableListOf<List<Int>>()
    private val columnIndexesFromTop = mutableListOf<List<Int>>()
    private val columnIndexesFromBottom = mutableListOf<List<Int>>()

    // row 1, last index of value x is 0-98
    init {

        for (lineIndex in input.indices) {
            val indexesFromLeft = mutableListOf<Int>()
            val indexesFromRight = mutableListOf<Int>()
            for (i in 0..9) {
                var firstIndexOf = input[lineIndex].indexOfFirst { it.digitToInt() >= i }
                indexesFromLeft.add(firstIndexOf)

                var firstIndexFromRight =
                    input[lineIndex].length - (input[lineIndex].reversed().indexOfFirst { it.digitToInt() >= i } + 1)
                indexesFromRight.add(firstIndexFromRight)
            }
            rowIndexesFromLeft.add(indexesFromLeft)
            rowIndexesFromRight.add(indexesFromRight)
        }

        for (columnIndex in input[0].indices) {
            val indexesFromTop = mutableListOf<Int>()

            for (i in 0..9) {
                var currentRow = 0
                while (currentRow < input.size) {
                    if (input[currentRow][columnIndex].digitToInt() >= i) {
                        indexesFromTop.add(currentRow)
                        currentRow++
                        break
                    }
                    currentRow++
                }
                if (currentRow >= input.size) {
                    indexesFromTop.add(input.size - 1)
                }
            }
            columnIndexesFromTop.add(indexesFromTop)
        }

        for (columnIndex in input[0].indices) {
            val indexesFromBottom = mutableListOf<Int>()

            for (i in 0..9) {
                var currentRow = input.size - 1
                while (currentRow >= 0) {
                    if (input[currentRow][columnIndex].digitToInt() >= i) {
                        indexesFromBottom.add(currentRow)
                        currentRow--
                        break
                    }
                    currentRow--
                }
                if (currentRow < 0) {
                    indexesFromBottom.add(0)
                }
            }
            columnIndexesFromBottom.add(indexesFromBottom)
        }
    }

    fun getVisibleTrees(): Int {
        var total = 0
        for (row in input.indices) {
            for (column in input[row].indices) {
                if (isEdge(row, input.size - 1, column, input[row].length - 1)) {
                    total++
                    continue
                }

                val value = input[row][column].digitToInt()
                // is it visible from the left?
                val firstIndexOf = rowIndexesFromLeft[row][value]
                if (firstIndexOf >= column) {
                    total++
                    continue
                }

                val firstFromRight = rowIndexesFromRight[row][value]
                // is it visible from the right?
                if (firstFromRight <= column) {
                    total++
                    continue
                }

                val firstFromTop = columnIndexesFromTop[column][value]
                if (firstFromTop >= row) {
                    total++
                    continue
                }

                val firstFromBottom = columnIndexesFromBottom[column][value]
                if (firstFromBottom <= row) {
                    total++
                    continue
                }
            }
        }
        return total
    }

    private fun isEdge(row: Int, lastRowIndex: Int, column: Int, lastColumnIndex: Int): Boolean {
        return (row == 0 || column == 0 || row == lastRowIndex || column == lastColumnIndex)
    }

    private fun getScenicScore(row: Int, column: Int): Int {
        if (isEdge(row, input.size - 1, column, input[row].length - 1)) {
            return 0
        }

        val currentTreeHeight = input[row][column].digitToInt()

        // look north
        var treesVisibleNorth = 1
        var currentRow = row - 1
        while (currentRow > 0) {
            val treeBeingExamined = input[currentRow][column].digitToInt()
            if (treeBeingExamined >= currentTreeHeight) {
                break
            }
            treesVisibleNorth++
            currentRow--
        }

        // look south
        var treesVisibleSouth = 1
        currentRow = row + 1
        while (currentRow < input.size - 1) {
            val treeBeingExamined = input[currentRow][column].digitToInt()
            if (treeBeingExamined >= currentTreeHeight) {
                break
            }
            treesVisibleSouth++
            currentRow++
        }

        // look west
        var treesVisibleWest = 1
        var currentColumn = column - 1
        while (currentColumn > 0) {
            val treeBeingExamined = input[row][currentColumn].digitToInt()
            if (treeBeingExamined >= currentTreeHeight) {
                break
            }
            treesVisibleWest++
            currentColumn--
        }

        // look east
        var treesVisibleEast = 1
        currentColumn = column + 1
        while (currentColumn < input[row].length - 1) {
            val treeBeingExamined = input[row][currentColumn].digitToInt()
            if (treeBeingExamined >= currentTreeHeight) {
                break
            }
            treesVisibleEast++
            currentColumn++
        }

        return treesVisibleNorth * treesVisibleSouth * treesVisibleEast * treesVisibleWest
    }

    fun getHighestScenicScore() {
        var highestScore = 0
        for (row in input.indices) {
            for (column in input[row].indices) {
                val score = getScenicScore(row, column)
                if (score > highestScore) {
                    highestScore = score
                }
            }
        }
        highestScore.println()
    }
}


fun main() {

    fun part1(forest: Forest) {
        forest.getVisibleTrees().println()
    }

    fun part2(forest: Forest) {
        forest.getHighestScenicScore()
    }

    val testInput = readInput("advent/day08/input")
    val forest = Forest(testInput)
    part1(forest)
    part2(forest)
}

//--- Day 8: Treetop Tree House ---
//The expedition comes across a peculiar patch of tall trees all planted carefully in a grid. The Elves explain that a previous expedition planted these trees as a reforestation effort. Now, they're curious if this would be a good location for a tree house.
//
//First, determine whether there is enough tree cover here to keep a tree house hidden. To do this, you need to count the number of trees that are visible from outside the grid when looking directly along a row or column.
//
//The Elves have already launched a quadcopter to generate a map with the height of each tree (your puzzle input). For example:
//
//30373
//25512
//65332
//33549
//35390
//Each tree is represented as a single digit whose value is its height, where 0 is the shortest and 9 is the tallest.
//
//A tree is visible if all of the other trees between it and an edge of the grid are shorter than it. Only consider trees in the same row or column; that is, only look up, down, left, or right from any given tree.
//
//All of the trees around the edge of the grid are visible - since they are already on the edge, there are no trees to block the view. In this example, that only leaves the interior nine trees to consider:
//
//The top-left 5 is visible from the left and top. (It isn't visible from the right or bottom since other trees of height 5 are in the way.)
//The top-middle 5 is visible from the top and right.
//The top-right 1 is not visible from any direction; for it to be visible, there would need to only be trees of height 0 between it and an edge.
//The left-middle 5 is visible, but only from the right.
//The center 3 is not visible from any direction; for it to be visible, there would need to be only trees of at most height 2 between it and an edge.
//The right-middle 3 is visible from the right.
//In the bottom row, the middle 5 is visible, but the 3 and 4 are not.
//With 16 trees visible on the edge and another 5 visible in the interior, a total of 21 trees are visible in this arrangement.
//
//Consider your map; how many trees are visible from outside the grid?
//
//Your puzzle answer was 1798.
//
//--- Part Two ---
//Content with the amount of tree cover available, the Elves just need to know the best spot to build their tree house: they would like to be able to see a lot of trees.
//
//To measure the viewing distance from a given tree, look up, down, left, and right from that tree; stop if you reach an edge or at the first tree that is the same height or taller than the tree under consideration. (If a tree is right on the edge, at least one of its viewing distances will be zero.)
//
//The Elves don't care about distant trees taller than those found by the rules above; the proposed tree house has large eaves to keep it dry, so they wouldn't be able to see higher than the tree house anyway.
//
//In the example above, consider the middle 5 in the second row:
//
//30373
//25512
//65332
//33549
//35390
//Looking up, its view is not blocked; it can see 1 tree (of height 3).
//Looking left, its view is blocked immediately; it can see only 1 tree (of height 5, right next to it).
//Looking right, its view is not blocked; it can see 2 trees.
//Looking down, its view is blocked eventually; it can see 2 trees (one of height 3, then the tree of height 5 that blocks its view).
//A tree's scenic score is found by multiplying together its viewing distance in each of the four directions. For this tree, this is 4 (found by multiplying 1 * 1 * 2 * 2).
//
//However, you can do even better: consider the tree of height 5 in the middle of the fourth row:
//
//30373
//25512
//65332
//33549
//35390
//Looking up, its view is blocked at 2 trees (by another tree with a height of 5).
//Looking left, its view is not blocked; it can see 2 trees.
//Looking down, its view is also not blocked; it can see 1 tree.
//Looking right, its view is blocked at 2 trees (by a massive tree of height 9).
//This tree's scenic score is 8 (2 * 2 * 1 * 2); this is the ideal spot for the tree house.
//
//Consider each tree on your map. What is the highest scenic score possible for any tree?
//
//Your puzzle answer was 259308.