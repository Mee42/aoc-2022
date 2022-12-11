package sh.carson.day10

import sh.carson.*
import kotlin.math.absoluteValue

fun main() {
    var x = 1
    var cycle = 0
    fun f() {
        print(if ((cycle++ % 40 - x).absoluteValue <= 1) "██" else "  ")
        if (cycle % 40 == 0) println()
    }
    for (line in inputLines(day = 10)) {
        f()
        if (line.startsWith("addx")) {
            f()
            x += line.ints().first()
        }
    }
}