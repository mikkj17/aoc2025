package day04

import java.io.File

typealias Position = Pair<Int, Int>

val directions = listOf(
    -1 to -1,
    -1 to 0,
    -1 to 1,
    0 to -1,
    0 to 1,
    1 to -1,
    1 to 0,
    1 to 1,
)

fun parse(inp: String): Set<Position> {
    val rows = inp.lines()
    val paperRolls = mutableSetOf<Position>()
    for ((y, row) in rows.withIndex()) {
        for ((x, value) in row.withIndex()) {
            if (value == '@') {
                paperRolls.add(y to x)
            }
        }
    }

    return paperRolls
}

fun adjacentTo(position: Position): List<Position> {
    val (y, x) = position
    return directions.map { (dy, dx) ->
        y + dy to x + dx
    }
}

fun first(inp: String): Int {
    val paperRolls = parse(inp)

    return paperRolls.count { roll ->
        adjacentTo(roll).count { it in paperRolls } < 4
    }
}

fun second(inp: String): Int {
    val paperRolls = parse(inp).toMutableSet()
    val initialCount = paperRolls.count()

    while (true) {
        val toRemove = paperRolls.filter { roll ->
            adjacentTo(roll).count { it in paperRolls } < 4
        }

        if (toRemove.isEmpty()) {
            break
        }

        paperRolls.removeAll(toRemove.toSet())
    }

    return initialCount - paperRolls.count()
}

fun main() {
    val testInput = File("src/day04/test-input.txt").readText()
    val input = File("src/day04/input.txt").readText()
    println(first(input))
    println(second(input))
}
