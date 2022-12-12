package sh.carson.day12

import sh.carson.*

val input = """
abccccaaaaaaacccaaaaaaaccccccccccccccccccccccccccccccccccaaaa
abcccccaaaaaacccaaaaaaaaaaccccccccccccccccccccccccccccccaaaaa
abccaaaaaaaaccaaaaaaaaaaaaaccccccccccccccccccccccccccccaaaaaa
abccaaaaaaaaaaaaaaaaaaaaaaacccccccccaaaccccacccccccccccaaacaa
abaccaaaaaaaaaaaaaaaaaacacacccccccccaaacccaaaccccccccccccccaa
abaccccaaaaaaaaaaaaaaaacccccccccccccaaaaaaaaaccccccccccccccaa
abaccccaacccccccccaaaaaacccccccccccccaaaaaaaacccccccccccccccc
abcccccaaaacccccccaaaaaaccccccccijjjjjjaaaaaccccccaaccaaccccc
abccccccaaaaacccccaaaacccccccciiijjjjjjjjjkkkkkkccaaaaaaccccc
abcccccaaaaacccccccccccccccccciiiirrrjjjjjkkkkkkkkaaaaaaccccc
abcccccaaaaaccccccccccccccccciiiirrrrrrjjjkkkkkkkkkaaaaaccccc
abaaccacaaaaacccccccccccccccciiiqrrrrrrrrrrssssskkkkaaaaacccc
abaaaaacaaccccccccccccccccccciiiqqrtuurrrrrsssssskklaaaaacccc
abaaaaacccccccccccaaccccccccciiqqqttuuuurrssusssslllaaccccccc
abaaaaaccccccccaaaaccccccccciiiqqqttuuuuuuuuuuusslllaaccccccc
abaaaaaacccccccaaaaaaccccccciiiqqqttxxxuuuuuuuusslllccccccccc
abaaaaaaccccaaccaaaaacccccchhiiqqtttxxxxuyyyyvvsslllccccccccc
abaaacacccccaacaaaaaccccccchhhqqqqttxxxxxyyyyvvsslllccccccccc
abaaacccccccaaaaaaaacccccchhhqqqqtttxxxxxyyyvvssqlllccccccccc
abacccccaaaaaaaaaaccaaacchhhpqqqtttxxxxxyyyyvvqqqlllccccccccc
SbaaacaaaaaaaaaaaacaaaaahhhhppttttxxEzzzzyyvvvqqqqlllcccccccc
abaaaaaaacaaaaaacccaaaaahhhppptttxxxxxyyyyyyyvvqqqlllcccccccc
abaaaaaaccaaaaaaaccaaaaahhhppptttxxxxywyyyyyyvvvqqqmmcccccccc
abaaaaaaacaaaaaaacccaaaahhhpppsssxxwwwyyyyyyvvvvqqqmmmccccccc
abaaaaaaaaaaaaaaacccaacahhhpppssssssswyyywwvvvvvqqqmmmccccccc
abaaaaaaaacacaaaacccccccgggppppsssssswwywwwwvvvqqqqmmmccccccc
abcaaacaaaccccaaaccccccccgggppppppssswwwwwrrrrrqqqmmmmccccccc
abcaaacccccccccccccccccccgggggpppoosswwwwwrrrrrqqmmmmddcccccc
abccaacccccccccccccccccccccgggggoooosswwwrrrnnnmmmmmddddccccc
abccccccccccccccccccccccccccgggggooossrrrrrnnnnnmmmddddaccccc
abaccccaacccccccccccccccccccccgggfoossrrrrnnnnndddddddaaacccc
abaccaaaaaaccccccccccccccccccccgffooorrrrnnnneeddddddaaaacccc
abaccaaaaaacccccccccccccccccccccfffooooonnnneeeddddaaaacccccc
abacccaaaaaccccccccaaccaaaccccccffffoooonnneeeeccaaaaaacccccc
abcccaaaaacccccccccaaccaaaaccccccffffoooneeeeeaccccccaacccccc
abaccaaaaaccccccccaaaacaaaaccccccafffffeeeeeaaacccccccccccccc
abacccccccccccccccaaaacaaacccccccccffffeeeecccccccccccccccaac
abaaaacccccccaaaaaaaaaaaaaacccccccccfffeeeccccccccccccccccaaa
abaaaacccccccaaaaaaaaaaaaaaccccccccccccaacccccccccccccccccaaa
abaacccccccccaaaaaaaaaaaaaaccccccccccccaacccccccccccccccaaaaa
abaaaccccccccccaaaaaaaaccccccccccccccccccccccccccccccccaaaaaa
""".trimIndent()

val realTest = """
Sabqponm
abcryxxl
accszExk
acctuvwj
abdefghi
""".trimIndent()

fun main() {
    val input = Array2D.from(input.trim().split("\n").map { it.char() }.transpose())

    val startAt = input.withIndexes().first { (c, i) -> c == 'S' }.second
    val endAt = input.withIndexes().first { (c, i) -> c == 'E' }.second

    input[endAt] = 'z'
    input[startAt] = 'a'

    println(input.coordsList.map {
        if(input[it] == 'a') lengthOfPath(it, endAt, input) else Int.MAX_VALUE }.minBy { it })
}

fun lengthOfPath(startAt: Coords2D, endAt: Coords2D, input: Array2D<Char>): Int {
    println("\n\n\n\n")
    val unvisited = mutableListOf<Coords2D>()
    val dist = mutableMapOf<Coords2D, Int>()
    val prev = mutableMapOf<Coords2D, Coords2D>()

    dist[startAt] = 0

    unvisited.addAll(input.coordsList)

    while(unvisited.isNotEmpty()) {
        var i = 0
        for(elem in unvisited.indices) {
            if((dist[unvisited[i]] ?: Int.MAX_VALUE) > (dist[unvisited[elem]] ?: Int.MAX_VALUE)) {
                i = elem
            }
        }
        val u = unvisited.removeAt(i)
//        println("processing $u")
        if(u == endAt) {
            println("found end")
            break
        }
        for(dir in CARDINAL_OFFSETS) {
            val v = dir + u
            if(!input.isInBound(v)) continue

            val startElev = input[u] // if(input[u] == 'S') 'a' else input[u]
            val endElev = input[v] // if(input[u] == 'E') 'z' else input[v]

            if(endElev > startElev + 1) {
                println("can't travel from $u to $v because ${input[v]} > ${input[u]} + 1")
                continue
            } // we can't move there lol

            val alt = (dist[u] ?: return Int.MAX_VALUE) + 1
            if(alt < (dist[v] ?: Int.MAX_VALUE)) {
                println("found shorter path to $v")
                dist[v] = alt
                prev[v] = u
            }
        }
    }


    var at = endAt
    var count = 0
    val list = mutableListOf<Coords2D>()
    while(at != startAt) {
        at = prev[at]!!
        list += at
        count++
    }
    println(list.joinToString(" ") { (x, y) -> "($x, $y)" })
    println(count)

    input.print(5, 1) { c, coords2D ->
        if(dist[coords2D] == null) "."
        else dist[coords2D]!!.toString().toString()
    }
    input.print(0, 0)
    return count
}