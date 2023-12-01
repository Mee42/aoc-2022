package sh.carson.day1_2023

import sh.carson.*


fun main() {
    val input = inputLines(day = 1, year = 2023)
    val inpute = """
two1nine
eightwothree
abcone2threexyz
xtwone3four
4nineeightseven2
zoneight234
7pqrstsixteen
    """.trimIndent().split("\n").map { it.trim() }.filter { it.isNotBlank() }
//    val x = input.map { it.filter { it in "0123456789" } }.map { (it.first().toString() + it.last().toString()).toInt() }.sum()
    val y = input.map {line ->
        val first = Regex("""\d|one|two|three|four|five|six|seven|eight|nine""").find(line)!!.value
        val second = Regex("""\d|eno|owt|eerht|ruof|evif|xis|neves|thgie|enin""").find(line.reversed())!!.value.reversed()
        val f = { x: String ->
            when(x) {
                "one" -> 1
                "two" -> 2
                "three" -> 3
                "four" -> 4
                "five" -> 5
                "six" -> 6
                "seven" -> 7
                "eight" -> 8
                "nine" -> 9
                else -> x.toInt()
            }.toString()
        }
        f(first) + f(second)
    }.map { it.toInt() }
    println(y)
    println(y.sum())
}