package sh.carson.day8

import sh.carson.*
val test = """
30373
25512
65332
33549
35390
""".trimIndent()
fun main() {
    val input = Array2D.from(inputLines(8, test = test, useTest = false).map { it.char().map { x -> "$x".toInt() } })

    val part1 = input.withIndexes().count { (c, coord) ->
       for(dir in CARDINAL_OFFSETS) {
           var currentCord = coord + dir
           while(true) {
               if(!input.isInBound(currentCord)) return@count true
               if(input[currentCord] >= c) break
               currentCord += dir
           }
       }
        false
    }


    val part2 = input.withIndexes().maxOf { (c, coord) ->
        CARDINAL_OFFSETS.map { dir: Coords2D ->
            var count = 0
            var curr = coord + dir
            while(input.isInBound(curr)) {
                count++
                if(input[curr] < c) {
                    curr += dir
                }
                else break
            }
            count
        }.product()
    }


    println("Part1: $part1")
    println("Part2: $part2")
}
