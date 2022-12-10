package sh.carson.day9

import sh.carson.*
import kotlin.math.absoluteValue
import kotlin.math.sign

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
val test2 = """
R 5
U 8
L 8
D 3
R 17
D 10
L 25
U 20
"""

fun main() {
    val a = inputLines(day = 9, test = test2, useTest = false)

    var position = point(0, 0)
    val tailPositions = (0 until 9).map { point(0, 0) }.toMutableList()
//    var directionDragged = (0 until 9).map<Int, Coords2D?> { null }.toMutableList()



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

        for(ii in 0 until mag2) {
            println()
            position += dir2

            updateTail@ for(i in tailPositions.indices) {
                val following = if(i == 0) position else tailPositions[i - 1]
                val myself = tailPositions[i]
                val difference = following - myself
                if(i == 0) {
                    print("")
                }
                if(difference.x.absoluteValue <= 1 && difference.y.absoluteValue <= 1) {
                    break@updateTail
                }
                if(difference.x.absoluteValue == 2 && difference.y.absoluteValue == 0) {
                    tailPositions[i] += difference / 2
                    println("${i + 1} moved in big line 2-0")
                } else if(difference.x.absoluteValue == 0 && difference.y.absoluteValue == 2) {
                    tailPositions[i] += difference / 2
                    println("${i + 1} moved in big line 2-0")
                } else if(difference.x.absoluteValue == 2 && difference.y.absoluteValue == 2) {
                    tailPositions[i] += difference / 2
                    println("moving tail number ${i + 1} via diagonal: ${difference}/2")
                } else if(difference.x.absoluteValue == 2 && difference.y.absoluteValue == 1) {
                    val diagDiff = point(difference.x.sign, difference.y.sign)
                    tailPositions[i] += diagDiff
                    println("moving tail number ${i + 1} via 2-1")
                } else if(difference.x.absoluteValue == 1 && difference.y.absoluteValue == 2) {
                    val diagDiff = point(difference.x.sign, difference.y.sign)
                    tailPositions[i] += diagDiff
                    println("moving tail number ${i + 1} via 1-2")
                } else {
                    break@updateTail
                }
            }
            println("just computed ${ii + 1}th move of $line: $position")
            traveledTo.add(tailPositions.last())
            val size = 5
            for(y in size downTo -size){
                for(x in -size rangeTo size) {
                    if(point(x, y) == position) {
                        print("H")
                    } else if(point(x, y) in tailPositions) {
                        print((tailPositions.indexOfFirst { it == point(x, y) } + 1).toString())
                    } else {
                        print(".")
                    }
                }
                println("")
            }
        }
        continue
    }

    println(a)
    println(traveledTo.size)
    println(traveledTo)
}