package sh.carson.day6

import sh.carson.*

fun sol(n: Int) = input(6)
    .windowed(n)
    .indexOfFirst { it.toSet().size == n } + n

fun main() {
    println("Part 1:" + sol(4))
    println("Part 2:" + sol(14))
}


