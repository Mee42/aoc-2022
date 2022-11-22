package sh.carson.day1

import sh.carson.*
import kotlin.math.absoluteValue

fun main() {
    val input = input(day = 3, year = 2017).ints().first()
    val space = Array2D.initSquare(1000) { -1 }
    var coord = point(500, 500)
    space[coord] = 1
    var i = 2
    var travelling = point(1, 0)
    val directions = l(
        point(1, 0),
        point(0, -1),
        point(-1, 0),
        point(0, 1)
    )
    while(true) {
        coord += travelling
        if(!space.isInBound(coord) || i > input + 3) break
        space[coord] = i++
        val next = directions[(1 + directions.indexOf(travelling)).mod(4)]
        if(space[coord + next] == -1) {
            travelling = next
        }
    }
    val end = space.withIndexes().first { (a, coords) -> a == input }
    println(end)
    println((end.second.x - 500).absoluteValue + (end.second.y - 500).absoluteValue)
//    space.print(2)
//    println(space)
//    val a = input
//    println(a)
}
