package sh.carson.day3

import sh.carson.*

val part2 = inputLines(day = 3).chunked(3).sumOf { x ->
        val shared = x
            .map { it.char().toSet() }
            .foldSameType { a, b -> a.intersect(b) }
            .first()
        if (shared in 'a'..'z') shared - 'a' + 1 else shared - 'A' + 27
    }

fun main() {
    println(part2)
}
