package sh.carson.day11

import sh.carson.*

data class Monkey(val id: Long, val items: ArrayDeque<Long>, val op: (Long) -> Long, val divideBy: Long, val monkeyIfTrue: Int, val monkeyIfFalse: Int, var inspects: Long)

fun main() {
    val monkeys = input(11).split("\n\n").map { monkey ->
        val m = monkey.split("\n")
        Monkey(id = m[0].ints().first().toLong(),
                items = ArrayDeque(m[1].ints().toMutableList().map { it.toLong() }),
                op = { x: Long ->
                    val xs = m[2].ints()
                    if(xs.isEmpty()) x * x
                    else if(m[2][23] == '+') xs[0] + x
                    else xs[0] * x
                },
                divideBy = m[3].ints().first().toLong(),
                monkeyIfTrue = m[4].ints().first(),
                monkeyIfFalse = m[5].ints().first(),
                inspects = 0)
    }

    val lcm = monkeys.map { it.divideBy }.product()

    for(round in 1 .. 10_000) {
        for (m in monkeys) {
            m.inspects += m.items.size
            while (m.items.size > 0) {
                val i = m.op(m.items.removeFirst()) % lcm
                monkeys[if (i % m.divideBy == 0L) m.monkeyIfTrue else m.monkeyIfFalse].items.addLast(i)
            }
        }
    }
    println(monkeys.map { it.inspects }.sorted().takeLast(2).product())
}