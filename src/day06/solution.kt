package day06

import utils.transpose
import java.io.File

data class Problem(val numbers: List<Long>, val operator: (Long, Long) -> Long) {
    fun compute() = numbers.reduce { a, b -> operator(a, b) }

    companion object {
        fun fromColumn(numbers: List<Long>, operator: String) = Problem(
            numbers,
            if (operator == "+") { a, b -> a + b } else { a, b -> a * b },
        )
    }
}

fun compute(inp: String, parser: (String) -> List<Problem>): Long {
    return parser(inp).sumOf { it.compute() }
}

fun first(inp: String): Long = compute(inp) {
    val operators = "[+*]".toRegex()
        .findAll(inp.lines().last())
        .map { it.value }
        .toList()

    inp.lines().dropLast(1)
        .map { line -> """\d+""".toRegex().findAll(line).map { it.value.toLong() }.toList() }
        .transpose()
        .zip(operators)
        .map { (numbers, operator) -> Problem.fromColumn(numbers, operator) }
}

fun second(inp: String): Long = compute(inp) {
    val width = inp.lines().maxOf { it.length }
    val lines = inp.lines().map { line ->
        line.padEnd(width, ' ')
    }

    val operators = "[+*]".toRegex().findAll(lines.last()).toList()
    operators.map { it.range.first }.plus(width + 1).zipWithNext()
        .map { (current, next) ->
            (current..<next - 1).map { i ->
                lines.dropLast(1).map { it[i] }.joinToString("").trim().toLong()
            }
        }
        .zip(operators.map { it.value })
        .map { (numbers, operator) -> Problem.fromColumn(numbers, operator) }
}

fun main() {
    val testInput = File("src/day06/test-input.txt").readText()
    val input = File("src/day06/input.txt").readText()
    println(first(input))
    println(second(input))
}
