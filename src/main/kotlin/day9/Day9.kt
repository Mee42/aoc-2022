package sh.carson.day9

import sh.carson.*
import kotlin.math.absoluteValue

val test = """
R 4
U 4
L 3
D 1
R 4
D 1
L 5
R 2
""".trimIndent()

fun main() {
    val a = inputLines(day = 9, test = test, useTest = false)

    var position = point(0, 0)
    var tailPosition = point(0, 0)



    val traveledTo = mutableSetOf<Coords2D>()

    for(line in a){
        val (dir, mag) = line.split(" ")
        val dir2 = when(dir) {
            "U" -> point(0, 1)
            "D" -> point(0, -1)
            "R" -> point(1, 0)
            "L" -> point(-1, 0)
            else -> e()
        }
        val mag2 = mag.toInt()

        for(i in 0 until mag2) {
            position += dir2
            val diff = tailPosition - position;
            if(diff.x.absoluteValue > 1 || diff.y.absoluteValue > 1) {
                // we need to drag the tail towards the thing
                tailPosition = position - dir2
            }
            traveledTo.add(tailPosition)
        }
    }

    println(a)
    println(traveledTo.size)
}