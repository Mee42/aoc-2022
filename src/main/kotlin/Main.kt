package sh.carson


import java.io.File
import java.net.CookieHandler
import java.net.CookieManager
import java.net.HttpURLConnection
import java.net.URL
import java.util.regex.Pattern

val storeDir = File("inputs/")

const val YEAR = 2022


fun input(day: Int, year: Int = YEAR, test: String = "", useTest: Boolean = false): String {
    if(useTest) return test
    val file = File(File(storeDir, "$year"), "$day.txt")
    file.parentFile.mkdirs()
    if(file.exists()) {
        return file.readText()
    }
    val cookie = CookieManager()
    CookieHandler.setDefault(cookie)
    val text = with(URL("https://adventofcode.com/$year/day/$day/input").openConnection() as HttpURLConnection){
        this.setRequestProperty("Cookie", "session=${File(storeDir, "session.txt").readLines()[0]}")
        inputStream.bufferedReader().readText()
    }
    file.writeText(text)
    return text
}

// 3.rangeTo(-4) ->
infix fun Int.rangeTo(to: Int) = if(this < to) this..to else this downTo to

fun inputLines(day: Int, year: Int = YEAR, filterBlank: Boolean = false, test: String = "", useTest: Boolean = false): List<String> =
    input(day, year, test, useTest).trim()
        .split("\n")
        .apIf(filterBlank) { it.filter(String::isNotBlank) }

fun String.allMatches(regex: String): List<String> {
    val list = mutableListOf<String>()
    val m = Pattern.compile(regex).matcher(this)
    while(m.find()) {
        list.add(m.group())
    }
    return list
}

// returns all ints in a string. Delim by anything
fun String.ints(): List<Int> = allMatches("""(-)?[0-9]+""").map { it.toInt() }


fun int(str: String): Int {
    return str.toInt()
}

fun c_plus(a: Int): (Int) -> Int = { it + a }
fun c_plus(a: Long): (Long) -> Long = { it + a}

fun <T> T.apIf(conditional: Boolean, block: (T) -> T): T = if(conditional) block(this) else this

enum class Part { ONE, TWO }

fun <T> List<T>.foldSameType(ifSizeIsZero: T? = null, folder: (T, T) -> T): T {
    if(isEmpty()) return ifSizeIsZero ?: error("size == 0, and there's no sizeofzero default value")
    var value = first()
    for(i in 1 until size) {
        value = folder(this[i], value)
    }
    return value
}

fun e(): Nothing = error("e()")

// ignore null safety
fun List<Int>.min(): Int = this.minOrNull() ?: error("No elements")
fun List<Int>.max(): Int = this.maxOrNull() ?: error("No elements")
fun <T> List<T>.minBy(f: (T) -> Int): T = this.minByOrNull(f) ?: error("No elements")
fun <T> List<T>.maxBy(f: (T) -> Int): T = this.maxByOrNull(f) ?: error("No elements")

fun <T, A> List<T>.foldr(acc: A, proc: (A, T) -> A): A {
    var x = acc
    for(elem in this) {
        x = proc(x, elem)
    }
    return x
}
fun <T> Boolean.if_(if_true: T, if_false: T) = if(this) if_true else if_false
fun <T> Boolean.ternary(if_true: T, if_false: T) = if(this) if_true else if_false

fun List<Char>.charListToString() = this.joinToString("") { "" + it }

@JvmName("transposeStringJvmGarbage")
fun Iterable<String>.transpose(): List<List<Char>> = this.map { it.toList() }.transpose()

fun <T> Iterable<Iterable<T>>.transpose(): List<List<T>> {
    val list = this.map { it.toList() }.toList()
    if(list.isEmpty()) error("list is zero")
    val size = list[0].size
    if(list.any { it.size != size }) error("inner list sizes are uneven")
    return List(list[0].size) { row ->
        List(list.size) { col ->
            list[col][row]
        }
    }
}



fun <T> id(x: T) = x
fun <T> l(vararg values: T): List<T> = values.toList()
fun List<Int>.product() = this.fold(1) { a, b -> a * b }

val CARDINAL_OFFSETS = l(point(-1, 0), point(1, 0), point(0, -1), point(0, 1))
val CARDINAL_OFFSETS_AND_SELF = l(point(-1, 0), point(1, 0), point(0, -1), point(0, 1), point(0, 0))

val CARDINAL_OFFSETS_INCL_DIAGONALS = (-1..1).flatMap { a -> (-1..1).mapNotNull { b ->
    if(a == 0 && b == 0) null else point(a, b)
} }
val CARDINAL_OFFSETS_INCL_DIAGONALS_AND_SELF = (-1..1).flatMap { a -> (-1..1).map { b -> point(a, b) } }

fun loopWhile(block: () -> Boolean) {
    while(true) if(!block()) return
}
fun <A, B, X, Y> Pair<A, B>.manipulateTuple(l: (A, B) -> Pair<X, Y>): Pair<X, Y> =
    l(first, second)

fun <A, B, C, X, Y, Z> Triple<A, B, C>.manipulateTriple(l: (A, B, C) -> Triple<X, Y, Z>): Triple<X, Y, Z> =
    l(first, second, third)

fun <T> List<T>.twoElements(): Pair<T, T> {
    val (a, b) = this
    return a to b
}

fun <T> permutations(input: List<T>, length: Int): List<List<T/*size=length*/>> {
    if(length == 0) return input.map { emptyList() }
    if(length == 1) return input.map { listOf(it) }
    return permutations(input, length - 1).flatMap { list -> input.map { list  + it } }
}

fun <T> ArrayDeque<T>.removeN(n: Int): List<T> {
    val x = take(n)
    for(i in 0 until n) removeFirst()
    return x
}
fun Iterable<Char>.fromBin() = this.joinToString("").toLong(2)
fun String.fromBin() = this.toCharArray().toList().fromBin()

fun String.char(): List<Char> = this.toCharArray().toList()


fun <T> ArrayDeque<T>.push(t: T) = addLast(t)
fun <T> ArrayDeque<T>.pop() = removeLast()


fun Iterable<Long>.sum() = this.sumOf { it }

fun Iterable<Long>.product() = this.fold(1L) { a, b -> a * b }