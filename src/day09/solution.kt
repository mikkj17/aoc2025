package day09

import java.io.File
import kotlin.math.abs

data class Point(val x: Long, val y: Long)

fun parse(inp: String): List<Point> {
    return inp.lines().map { it.split(",").let { (x, y) -> Point(x.toLong(), y.toLong()) } }
}

fun first(inp: String): Long {
    val tiles = parse(inp)

    return tiles.flatMap { tile ->
        tiles.map { (abs(tile.x - it.x) + 1) * (abs(tile.y - it.y) + 1) }
    }.max()
}

fun borderFrom(tiles: List<Point>): Set<Point> {
    val border = mutableSetOf<Point>()

    (tiles + tiles.first()).zipWithNext().forEach { (pi, pj) ->
        val toAdd = when {
            pi.x == pj.x -> (minOf(pi.y, pj.y)..maxOf(pi.y, pj.y))
                .map { Point(pi.x, it) }

            else -> (minOf(pi.x, pj.x)..maxOf(pi.x, pj.x))
                .map { Point(it, pj.y) }
        }
        border.addAll(toAdd)
    }

    return border
}

fun rayCasting(point: Point, tiles: List<Point>): Boolean {
    val intersections = (tiles + tiles.first())
        .zipWithNext()
        .count { (pi, pj) ->
            // handle horizontal edge
            if (pi.y == pj.y) {
                if (point.y == pi.y) {
                    // check boundary
                    if (point.x in (minOf(pi.x, pj.x)..maxOf(pi.x, pj.x))) {
                        return true
                    }
                }

                // regular horizontal edge that doesn't collide with the point
                return@count false
            }

            check(pi.x == pj.x)
            // handle vertical edge
            val minY = minOf(pi.y, pj.y)
            val maxY = maxOf(pi.y, pj.y)

            // check boundary
            if (point.x == pi.x && point.y in (minY..maxY)) {
                return true
            }

            point.x < pi.x && point.y in (minY..<maxY)
        }

    return intersections % 2 == 1
}

// this runs for several hours, but I really don't care
fun second(inp: String): Long {
    val redTiles = parse(inp)

    val candidates = redTiles.flatMapIndexed { index, tile ->
        redTiles
            .drop(index + 1)
            .filter { inner ->
                val corners = listOf(
                    tile,
                    Point(tile.x, inner.y),
                    inner,
                    Point(inner.x, tile.y),
                )
                val border = borderFrom(corners)
                border.all { rayCasting(it, redTiles) }
            }
            .map { tile to it }
            .also { println("$index: found ${it.size} candidates for $tile") }
    }

    return candidates.maxOf { (pi, pj) ->
        (abs(pi.x - pj.x) + 1) * (abs(pi.y - pj.y) + 1)
    }
}

fun draw(tiles: List<Point>, border: Set<Point>) {
    val maxX = tiles.maxOf { it.x } + 2
    val maxY = tiles.maxOf { it.y } + 1

    for (y in 0..maxY) {
        for (x in 0..maxX) {
            val pos = Point(x, y)
            when (pos) {
                in tiles -> print('#')
                in border -> print('X')
                else -> print('.')
            }
            print(" ")
        }
        println()
    }

}

fun main() {
    val testInput = File("src/day09/test-input.txt").readText()
    val testInput2 = File("src/day09/test-input2.txt").readText()
    val input = File("src/day09/input.txt").readText()
    println(first(input))
    println(second(input))
}
