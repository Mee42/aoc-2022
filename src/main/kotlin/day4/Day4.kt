package sh.carson.day4

import sh.carson.*


val part2 = inputLines(day = 4)
    .map { it.split("-").flatMap(String::ints) }
    .count { (a0, a1, b0, b1) ->
        a0 in b0..b1 || a1 in b0..b1 ||
        b0 in a0..a1 || b1 in a0..a1
    }

fun main() {
    println(part2)
}