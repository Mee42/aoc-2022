package sh.carson.day5

import sh.carson.*

fun main() {
    val (stacks, instructions) = input(day = 5).split("\n\n")

    val s3 = stacks
        .split("\n")
        .transpose()
        .map { it.joinToString("").replace("[", "").replace("]", "") }
        .filter(String::isNotBlank)
        .map { ArrayDeque(it.trim().dropLast(1).char().reversed()) }

    for(line in instructions.split("\n").filter(String::isNotBlank)) {
        val (count, a, b) = line.ints()
        val crane = ArrayDeque<Char>()
        for(n in 0 until count) crane.push(s3[a - 1].pop())
        for(n in 0 until count) s3[b - 1].push(crane.pop())
    }
    println(s3.map { it.last() }.joinToString(""))
}
