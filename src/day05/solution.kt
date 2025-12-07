package day05

import java.io.File

fun parse(inp: String): Pair<List<LongRange>, List<Long>> {
    val (ranges, ids) = inp.split("\n\n")

    return Pair(
        ranges.lines().map { it.split("-").let { (start, end) -> start.toLong()..end.toLong() } },
        ids.lines().map { it.toLong() },
    )
}

fun first(inp: String): Int {
    val (ranges, ids) = parse(inp)

    return ids.count { id ->
        ranges.any { id in it }
    }
}

fun second(inp: String): Long = parse(inp)
    .first
    .sortedBy { it.first }
    .fold(emptyList<LongRange>()) { acc, next ->
        val prev = acc.lastOrNull() ?: return@fold listOf(next)

        when {
            prev.last < next.first -> acc.plusElement(next)
            prev.last < next.last -> acc.dropLast(1).plusElement(prev.first..next.last)
            else -> acc
        }
    }
    .sumOf { it.last - it.first + 1 }

fun main() {
    val testInput = File("src/day05/test-input.txt").readText()
    val testInput2 = File("src/day05/test-input2.txt").readText()
    val input = File("src/day05/input.txt").readText()
    println(first(input))
    println(second(input))
}
