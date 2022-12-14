package sh.carson.day14

import sh.carson.*
import kotlin.math.roundToInt
import kotlin.math.sqrt

fun main() {
    val inn = inputLines(14)

    var lowestY = 0
    val off = 300
    val arr = Array2D.init(400, 180) { '.' }
    for(line in inn) {
        line.split("->").zipWithNext().map { (a, b) ->

            val start = point(a.ints()[0] - off, a.ints()[1])
            val end = point(b.ints()[0] - off, b.ints()[1])
            val normDiff = (end - start).shittyIntegerNormalize()
            var current = start
            do {
                arr[current] = '#'
                current += normDiff
            } while (current != end)
            arr[current] = '#'

            if (start.y > lowestY) lowestY = start.y
            if (end.y > lowestY) lowestY = end.y
        }
    }
    for(x in 0 until arr.xSize()) {
        arr[point(x, lowestY + 2)] = '#'
    }

    var c = 0
    outer@ while(true) {
        var sandAt = point(500 - off, 0)
        if(arr[sandAt] == 'o') {
            break@outer
        }
        c++
        while(true) {
            if(arr[sandAt + point(0, 1)] == '.') {
                sandAt += point(0, 1)
            } else if(arr[sandAt + point(-1, 1)] == '.') {
                sandAt += point(-1, 1)
            } else if(arr[sandAt + point(1, 1)] == '.') {
                sandAt += point(1, 1)
            } else {
                // settled
                arr[sandAt] = 'o'
                break
            }
        }
    }

    arr.print(0, 0, converter = { it, _ -> when(it) {
        '.' -> " "
        '#' -> "█"
        'o' -> "#"
        else -> e()
    } })
    println("answer: $c")
}
