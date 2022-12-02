package sh.carson.day1

import sh.carson.*

fun <T> Iterable<T>.splitBy(splitOn: (T) -> Boolean): List<List<T>> {
    val x = mutableListOf<List<T>>()
    var current = mutableListOf<T>()
    for(elem in this) {
        if(splitOn(elem)) {
            x.add(current)
            current = mutableListOf()
        } else {
            current.add(elem)
        }
    }
    return x;
}

fun main() {
    val input = input(day = 1, year = 2022).split("\n\n").map { elf ->
        elf.ints().sum()
    }.sortedByDescending { it }

    val input2 = inputLines(day = 1, year = 2022).splitBy { it == "" }.map { elf -> elf.sumOf { it.toInt() } }.sortedByDescending { it }




    println(input[0] + input[1] + input[2])
    println(input2[0] + input2[1] + input2[2])
}
