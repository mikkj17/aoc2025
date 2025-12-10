package day08

import java.io.File
import kotlin.math.pow
import kotlin.math.sqrt

data class JunctionBox(val x: Int, val y: Int, val z: Int) {
    fun asList() = listOf(x, y, z)
    fun distanceTo(other: JunctionBox) = sqrt(
        asList().zip(other.asList()).sumOf { (a, b) -> (a - b).toDouble().pow(2) }
    )

    companion object {
        fun fromList(coords: List<Int>) = JunctionBox(coords[0], coords[1], coords[2])
    }
}

fun parse(inp: String): List<JunctionBox> {
    return inp.lines().map { line ->
        JunctionBox.fromList(line.split(",").map { it.toInt() })
    }
}

fun getDistances(boxes: List<JunctionBox>) = boxes
    .flatMapIndexed { index, box ->
        boxes
            .drop(index + 1)
            .map { Pair(box, it) to box.distanceTo(it) }
    }
    .sortedBy { it.second }

fun first(inp: String, repetitions: Int): Int {
    val boxes = parse(inp)
    val distances = getDistances(boxes)
    val circuits = boxes.map { mutableSetOf(it) }

    for ((a, b) in distances.map { it.first }.take(repetitions)) {
        val circuitA = circuits.single { a in it }
        val circuitB = circuits.single { b in it }

        if (circuitA == circuitB) continue
        circuitA.addAll(circuitB)
        circuitB.clear()
    }

    return circuits.map { it.size }.sortedDescending().take(3).reduce { acc, i -> acc * i }
}

fun second(inp: String): Long {
    val boxes = parse(inp)
    val distances = getDistances(boxes)
    val circuits = boxes.map { mutableSetOf(it) }

    for ((a, b) in distances.map { it.first }) {
        val circuitA = circuits.single { a in it }
        val circuitB = circuits.single { b in it }

        if (circuitA == circuitB) continue
        circuitA.addAll(circuitB)
        circuitB.clear()

        if (circuits.count { it.isNotEmpty() } == 1) {
            return a.x.toLong() * b.x.toLong()
        }
    }

    throw IllegalStateException("should not happen")
}

fun main() {
    val testInput = File("src/day08/test-input.txt").readText()
    val input = File("src/day08/input.txt").readText()
    println(first(input, 1000))
    println(second(input))
}
