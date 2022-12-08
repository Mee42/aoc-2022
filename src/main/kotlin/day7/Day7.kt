package sh.carson.day7

import sh.carson.*

data class Node(val name: String,
                val parent: Node?,
                val sub: MutableList<Node>?,
                var size: Int?){
    fun list(): List<Node> = listOf(this) + (sub ?: emptyList()).flatMap { it.list() }
}

fun main() {
    val input = ArrayDeque(inputLines(day = 7).drop(1))

    val root = Node("/", null, mutableListOf(), null)
    var current = root
    while(input.isNotEmpty()) {
        val line = input.removeFirst()
        if(line.startsWith("$ cd")) {
            val arg = line.split(" ").last()
            current = if(arg == "..") {
                current.parent!!
            } else {
                current.sub?.firstOrNull { it.name == arg } ?: Node(arg, current, mutableListOf(), null)
            }
        } else if(!line.startsWith("$ ls")) {
            while(input.isNotEmpty() && !input.first().startsWith("$")) {
                val (first, second) = input.removeFirst().split(" ")
                if(first == "dir") {
                    current.sub!!.add(Node(second, current, mutableListOf(), null))
                } else {
                    current.sub!!.add(Node(second, current, null, first.toInt()))
                }
            }
        }
    }

    // compute sizes
    // also assign all directories sizes
    fun computeSizes(n: Node): Int {
        if(n.sub == null) {
            return n.size!!
        } else {
            val x = n.sub!!.sumOf { computeSizes(it) }
            n.size = x
            return x
        }
    }
    computeSizes(root)

    val existingFreeSpace = 70000000 - root.size!!

    val part2 = root.list()
        .filter { existingFreeSpace + it.size!! >= 30000000 && it.sub != null }
        .minBy { it.size!! }

    println(part2.size)
}