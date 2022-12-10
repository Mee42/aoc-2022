package sh.carson.day10

import sh.carson.*
import kotlin.math.absoluteValue

fun main() {
    val a = inputLines(10)
    var x = 1
    val xValues = mutableListOf<Int>()
    for(line in a ) {
        xValues += x
        if(line.startsWith("addx")) {
            xValues += x
            x += line.ints().first()
        }
    }
    for(line in 0 until 6) {
        for(cycle in 0 until 40) {
            val index= line*40 + cycle
            if((cycle - xValues[index]).absoluteValue <= 1) {
                print("█")
            } else {
                print(" ")
            }
        }
        println()
    }

}