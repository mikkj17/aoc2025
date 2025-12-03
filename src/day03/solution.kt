package day03

import java.io.File


fun first(inp: String): Int {
    return inp
        .lines()
        .sumOf { bank ->
            val firstMax = bank.dropLast(1).withIndex().maxBy { it.value }
            val secondMax = bank.drop(firstMax.index + 1).max()
            "${firstMax.value}$secondMax".toInt()
        }
}

fun second(inp: String): Long {
    return inp
        .lines()
        .sumOf { bank ->
            var drop = 0
            var dropLast = 11
            val maxes = mutableListOf<Char>()
            repeat(12) {
                val max = bank.dropLast(dropLast).drop(drop).withIndex().maxBy { it.value }
                maxes.add(max.value)
                drop += max.index + 1
                dropLast--
            }
            maxes.joinToString("").toLong()
        }
}

fun main() {
    val testInput = File("src/day03/test-input.txt").readText()
    val input = File("src/day03/input.txt").readText()
    println(first(input))
    println(second(input))
}
