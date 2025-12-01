package day01

import java.io.File

fun parse(inp: String): List<Pair<Char, Int>> = inp
    .lines()
    .map { it.first() to it.drop(1).toInt() }

fun first(inp: String) = parse(inp)
    .scan(50) { dial, (direction, clicks) ->
        (if (direction == 'L') dial - clicks else dial + clicks) % 100
    }
    .count { it == 0 }

fun second(inp: String): Int {
    val rotations = parse(inp)
    var zeros = 0
    var dial = 50
    for ((direction, clicks) in rotations) {
        repeat(clicks) {
            dial = (if (direction == 'L') dial - 1 else dial + 1) % 100
            if (dial == 0) {
                zeros++
            }
        }
    }

    return zeros
}

fun main() {
    val testInput = File("src/day01/test-input.txt").readText()
    val input = File("src/day01/input.txt").readText()
    println(first(input))
    println(second(input))
}
