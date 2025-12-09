package day07

import java.io.File

typealias Position = Pair<Int, Int>

fun parse(inp: String): Pair<Position, Set<Position>> {
    lateinit var start: Position
    val splitters = mutableSetOf<Position>()
    for ((y, line) in inp.lines().withIndex()) {
        for ((x, value) in line.withIndex()) {
            if (value == 'S') start = y to x
            if (value == '^') splitters.add(y to x)
        }
    }

    return start to splitters
}

fun first(inp: String): Int {
    val (start, splitters) = parse(inp)
    val height = inp.lines().size
    val beams = mutableListOf(start)
    val visited = mutableSetOf<Position>()
    var counter = 0

    while (beams.isNotEmpty()) {
        val beam = beams.removeFirst()
        val next = beam.first + 1 to beam.second

        if (next.first == height) continue
        when (next) {
            in visited -> continue
            in splitters -> {
                beams += next.first to next.second - 1
                beams += next.first to next.second + 1
                counter++
            }

            else -> {
                beams += next
            }
        }
        visited += next
    }

    return counter
}

fun second(inp: String): Long {
    val (start, splitters) = parse(inp)
    val height = inp.lines().size
    val memo = mutableMapOf<Position, Long>()

    fun timeline(position: Position): Long {
        return memo.getOrPut(position) {
            val next = position.first + 1 to position.second

            when {
                next.first == height -> 1
                next in splitters -> timeline(next.first to next.second - 1) +
                        timeline(next.first to next.second + 1)

                else -> timeline(next)
            }

        }
    }

    return timeline(start)
}

fun main() {
    val testInput = File("src/day07/test-input.txt").readText()
    val input = File("src/day07/input.txt").readText()
    println(first(input))
    println(second(input))
}
