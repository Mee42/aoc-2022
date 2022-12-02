package sh.carson.day2

import sh.carson.*

fun main() {
    println(inputLines(day = 2, year = 2022).sumOf { line ->
        l(l(3, 1, 2), l(1, 2, 3), l(2, 3, 1))[line[2] - 'X'][line[0] - 'A'] +
                l(0, 3, 6)[line[2] - 'X']
    })
}