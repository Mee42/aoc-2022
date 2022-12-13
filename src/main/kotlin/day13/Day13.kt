package sh.carson.day13

import com.github.cliftonlabs.json_simple.Jsoner
import sh.carson.*
import java.math.BigDecimal
import kotlin.math.max

fun main() {
    val p = ArrayList((input(day = 13) + "[[2]]\n[[6]]").split("\n").filter { it.isNotBlank() }.map(Jsoner::deserialize))

    p.sortWith(Comparator { any, any2 -> if(rightOrder(any, any2) ?:  return@Comparator 0) -1 else 1 })
    println(p.indexOf(Jsoner.deserialize("[[2]]")).inc() * p.indexOf(Jsoner.deserialize("[[6]]")).inc())
}

fun rightOrder(a: Any?, b: Any?) : Boolean? {
    if(a !is List<*> && a !is BigDecimal) return false
    if(b !is List<*> && b !is BigDecimal) return false
    if(a is BigDecimal && b is BigDecimal) {
        if (a == b) return null // inconclusive
        return a < b
    }
    if(a is List<*> && b is List<*>) {
        for(i in 0 until max(a.size, b.size)) {
            if (i == a.size) return true
            if (i == b.size) return false
            return rightOrder(a[i], b[i]) ?: continue
        }
        return null // inconclusive
    }
    if(a is BigDecimal) return rightOrder(listOf(a), b)
    if(b is BigDecimal) return rightOrder(a, listOf(b))
    e()
}