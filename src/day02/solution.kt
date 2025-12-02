package day02

import java.io.File

fun parse(inp: String): List<Pair<Long, Long>> = inp
    .split(",")
    .map { it.split("-").let { (a, b) -> a.toLong() to b.toLong() } }

fun compute(inp: String, isInvalid: (String) -> Boolean): Long {
    return parse(inp).sumOf { (start, end) ->
        (start..end).filter { isInvalid(it.toString()) }.sum()
    }
}

fun first(inp: String): Long {
    return compute(inp) { digits ->
        digits.take(digits.length / 2) == digits.drop(digits.length / 2)
    }
}

fun second(inp: String): Long {
    return compute(inp) { digits ->
        (1..digits.length / 2).any {
            val candidate = digits.take(it)
            val rest = digits.drop(it)
            candidate.repeat(digits.length / candidate.length - 1) == rest
        }
    }
}

fun main() {
    val testInput = File("src/day02/test-input.txt").readText()
    val input = File("src/day02/input.txt").readText()
    println(first(input))
    println(second(input))
}
