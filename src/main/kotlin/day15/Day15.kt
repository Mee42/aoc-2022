package sh.carson.day15

import sh.carson.*
import kotlin.math.absoluteValue
import kotlin.system.exitProcess

val test = """
Sensor at x=2, y=18: closest beacon is at x=-2, y=15
Sensor at x=9, y=16: closest beacon is at x=10, y=16
Sensor at x=13, y=2: closest beacon is at x=15, y=3
Sensor at x=12, y=14: closest beacon is at x=10, y=16
Sensor at x=10, y=20: closest beacon is at x=10, y=16
Sensor at x=14, y=17: closest beacon is at x=10, y=16
Sensor at x=8, y=7: closest beacon is at x=2, y=10
Sensor at x=2, y=0: closest beacon is at x=2, y=10
Sensor at x=0, y=11: closest beacon is at x=2, y=10
Sensor at x=20, y=14: closest beacon is at x=25, y=17
Sensor at x=17, y=20: closest beacon is at x=21, y=22
Sensor at x=16, y=7: closest beacon is at x=15, y=3
Sensor at x=14, y=3: closest beacon is at x=15, y=3
Sensor at x=20, y=1: closest beacon is at x=15, y=3
""".trimIndent()

fun main() {
    val testT = false


    val xOff = if(testT) 5 else 3000000

    val lines = inputLines(15, test = test, useTest = testT)
    val arr = (0 until (if(testT) 50 else 50000000)).map { '.' }.toMutableList()
    val sensors = lines.map {
        val i = it.ints()

        point(i[0] + xOff, i[1]) to point(i[2] + xOff, i[3])
    }
    val possibilities = mutableSetOf<Coords2D>()

    fun check(checking: Coords2D) {

        var found = true
        if(checking.x - xOff !in 0 .. (if(testT) 20 else 4000000)) return
        if(checking.y !in 0 .. (if(testT) 20 else 4000000)) return

        for((sensor, beacon) in sensors) {
            val distance = (sensor - beacon).manhattenDistance()
            if((sensor - checking).manhattenDistance() <= distance) {
                found = false
                break
            }
        }

        if(found) {
            println("NTOHEUSNTOEHUSNTOEHUN ${checking - p(xOff,0)}")
            val (x, y) = checking - p(xOff, 0)
            println(x.toLong() *4000000 + y.toLong())
            exitProcess(0)
        }
    }

    sensors.forEachIndexed { i, (sensor, beacon) ->
        val distance = (sensor - beacon).manhattenDistance()
        (- distance - 1.. distance + 1).forEach { x ->
            check(point(x + sensor.x,  1 * (distance + 1 - x.absoluteValue) + sensor.y))
            check(point(x + sensor.x, -1 * (distance + 1 - x.absoluteValue) + sensor.y))
        }
        println("checked $sensor: $i / ${sensors.size}")

    }
}