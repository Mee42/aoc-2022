package sh.carson

import kotlin.math.absoluteValue
import kotlin.math.roundToInt
import kotlin.math.sqrt


data class Coords2D(val x: Int, val y: Int) {
    operator fun plus(other: Coords2D) = Coords2D(x + other.x, y + other.y)
    operator fun plus(c: Int) = this + point(c, c)
    operator fun minus(other: Coords2D) = Coords2D(x - other.x, y - other.y)
    operator fun minus(c: Int) = this - point(c, c)
    operator fun times(other: Coords2D) = Coords2D(x * other.x, y * other.y)
    operator fun times(c: Int) = this * point(c, c)
    operator fun div(other: Coords2D) = Coords2D(x / other.x, y / other.y)
    operator fun div(c: Int) = this / point(c, c)
    fun shittyIntegerNormalize(): Coords2D = Coords2D((x / mag()).roundToInt(), (y / mag()).roundToInt())
    fun mag() = sqrt((x * x + y * y).toDouble())
    fun manhattenDistance() = x.absoluteValue + y.absoluteValue

}

data class Coords3D(val x: Int, val y: Int, val z: Int)
data class Coords4D(val x: Int, val y: Int, val z: Int, val w: Int)

fun point(x: Int, y: Int) = Coords2D(x, y)
fun p(x: Int, y: Int) = point(x, y)
fun pointRC(row: Int, col: Int) = point(x = row, y = col)
fun point(x: Int, y: Int, z: Int) = Coords3D(x, y, z)
fun p(x: Int, y: Int, z: Int) = point(x, y, z)
fun point(x: Int, y: Int, z: Int, w: Int) = Coords4D(x, y, z, w)
fun p(x: Int, y: Int, z: Int, w: Int) = point(x, y, z, w)



class Array2D<T> (val list: List<List<MutBox<T>>>): Iterable<T> {

    fun xSize() = list.size
    fun ySize() = list[0].size

    operator fun get(coords: Coords2D): T = getMutBox(coords).get()
    fun getMutBox(coords: Coords2D): MutBox<T> = list[coords.x][coords.y]
    fun getOrNull(coords: Coords2D): T? = getMutBoxOrNull(coords)?.get()
    fun getMutBoxOrNull(coords: Coords2D): MutBox<T>? = list.getOrNull(coords.x)?.getOrNull(coords.y)

    operator fun set(coords: Coords2D, value: T) = list[coords.x][coords.y].set(value)

    fun update(coords: Coords2D, f: (T) -> T) = update(coords) { it, _ -> f(it) }
    fun update(coords: Coords2D, f: (T, Coords2D) -> T) {
        val box = getMutBox(coords)
        box.update { f(it, coords) }
    }

    fun toList(): List<T> = list.flatten().map { it.get() }
    fun toListMutBox(): List<MutBox<T>> = listRepresentation

    private val listRepresentation by lazy { list.flatten() }
    val coordsMatrix by lazy { init(list.size, list[0].size) { it } }
    val coordsList by lazy { coordsMatrix.toList() }


    fun forEach(f: (T) -> Unit) = forEach { it, _ -> f(it) }
    fun forEach(f: (T, Coords2D) -> Unit) {
        for (coords in coordsList) f(get(coords), coords)
    }

    fun forEachUpdate(f: (T) -> T) = forEachUpdate { it, _ -> f(it) }
    fun forEachUpdate(f: (T, Coords2D) -> T) {
        for (coords in coordsList) {
            val elem = getMutBox(coords)
            elem.update { f(it, coords) }
        }
    }

    fun <R> map(f: (T) -> R): Array2D<R> = map { it, _ -> f(it) }
    fun <R> map(f: (T, Coords2D) -> R): Array2D<R> {
        return Array2D(list.mapIndexed { x, xList ->
            xList.mapIndexed { y, value ->
                MutBox(
                    f(
                        value.get(),
                        Coords2D(x, y)
                    )
                )
            }
        })
    }

    fun isInBound(coords: Coords2D): Boolean {
        return coords.x in list.indices && coords.y in list[0].indices
    }

    companion object {
        fun <T> initSquare(size: Int, default: (Coords2D) -> T): Array2D<T> {
            return init(size, size, default)
        }

        fun <T> init(xSize: Int, ySize: Int, default: (Coords2D) -> T): Array2D<T> {
            return Array2D(List(xSize) { x -> List(ySize) { y -> MutBox(default(Coords2D(x, y))) } })
        }

        fun <T> from(list: List<List<T>>): Array2D<T> {
            return Array2D(list.map { it.map { x -> MutBox(x) } })
        }
    }

    override fun iterator(): Iterator<T> {
        return list.asSequence().flatMap { it.asSequence() }.map { it.get() }.iterator()
    }
    fun withIndexes(): Array2D<Pair<T, Coords2D>> {
        return map { a, idx -> a to idx}
    }
    fun print(padding: Int = 0, fixedSpace: Int = 1, converter: (T, Coords2D) -> String = { a, b -> a.toString() }) {
        for(y in 0 until ySize()) {
            for(x in 0 until xSize()) {
                print(converter(list[x][y].get(), Coords2D(x, y)).padEnd(padding, ' ') + " ".repeat(fixedSpace))
            }
            println()
        }
    }

}

class Array3D<T> private constructor(val list: List<List<List<MutBox<T>>>>): Iterable<T> {

    fun xSize() = list.size
    fun ySize() = list[0].size
    fun zSize() = list[0][0].size

    operator fun get(coords: Coords3D): T = getMutBox(coords).get()
    fun getMutBox(coords: Coords3D): MutBox<T> = list[coords.x][coords.y][coords.z]

    operator fun set(coords: Coords3D, value: T) = list[coords.x][coords.y][coords.z].set(value)

    fun update(coords: Coords3D, f: (T) -> T) = update(coords) { it, _ -> f(it) }
    fun update(coords: Coords3D, f: (T, Coords3D) -> T) {
        val box = getMutBox(coords)
        box.update { f(it, coords) }
    }

    fun toList(): List<T> = list.flatten().flatten().map { it.get() }
    fun toListMutBox(): List<MutBox<T>> = listRepresentation

    private val listRepresentation by lazy { list.flatten().flatten() }
    val coordsMatrix by lazy { init(list.size, list[0].size, list[0][0].size) { it } }
    val coordsList by lazy { coordsMatrix.toList() }


    fun forEach(f: (T) -> Unit) = forEach { it, _ -> f(it) }
    fun forEach(f: (T, Coords3D) -> Unit) {
        for(coords in coordsList) f(get(coords), coords)
    }

    fun forEachUpdate(f: (T) -> T) = forEachUpdate { it, _ -> f(it) }
    fun forEachUpdate(f: (T, Coords3D) -> T) {
        for(coords in coordsList) {
            val elem = getMutBox(coords)
            elem.update { f(it, coords) }
        }
    }

    fun <R> map(f: (T) -> R): Array3D<R> = map { it, _ -> f(it) }
    fun <R> map(f: (T, Coords3D) -> R): Array3D<R> {
        return Array3D(list.mapIndexed { x, xList ->
            xList.mapIndexed { y, yList  ->
                yList.mapIndexed { z, value ->
                    MutBox(f(value.get(), Coords3D(x, y, z)))
                }
            }
        })
    }

    companion object {
        fun <T> initCube(size: Int, default: (Coords3D) -> T): Array3D<T> {
            return init(size, size, size, default)
        }
        fun <T> init(xSize: Int, ySize: Int, zSize: Int, default: (Coords3D) -> T): Array3D<T> {
            return Array3D(List(xSize) { x -> List(ySize) { y -> List(zSize) { z -> MutBox(default(Coords3D(x, y, z))) } } })
        }
    }

    override fun iterator(): Iterator<T> {
        return list.asSequence().flatMap { it.asSequence() }.flatMap { it.asSequence() }.map { it.get() }.iterator()
    }
}

class Array4D<T> private constructor(val list: List<List<List<List<MutBox<T>>>>>): Iterable<T> {


    fun xSize() = list.size
    fun ySize() = list[0].size
    fun zSize() = list[0][0].size
    fun wSize() = list[0][0][0].size

    operator fun get(coords: Coords4D): T = getMutBox(coords).get()
    fun getMutBox(coords: Coords4D): MutBox<T> = list[coords.x][coords.y][coords.z][coords.w]

    operator fun set(coords: Coords4D, value: T) = list[coords.x][coords.y][coords.z][coords.w].set(value)

    fun update(coords: Coords4D, f: (T) -> T) = update(coords) { it, _ -> f(it) }
    fun update(coords: Coords4D, f: (T, Coords4D) -> T) {
        val box = getMutBox(coords)
        box.update { f(it, coords) }
    }

    fun toList(): List<T> = list.flatten().flatten().flatten().map { it.get() }
    fun toListMutBox(): List<MutBox<T>> = listRepresentation

    private val listRepresentation by lazy { list.flatten().flatten().flatten() }
    val coordsMatrix by lazy { init(list.size, list[0].size, list[0][0].size, list[0][0][0].size) { it } }
    val coordsList by lazy { coordsMatrix.toList() }


    fun forEach(f: (T) -> Unit) = forEach { it, _ -> f(it) }
    fun forEach(f: (T, Coords4D) -> Unit) {
        for(coords in coordsList) f(get(coords), coords)
    }

    fun forEachUpdate(f: (T) -> T) = forEachUpdate { it, _ -> f(it) }
    fun forEachUpdate(f: (T, Coords4D) -> T) {
        for(coords in coordsList) {
            val elem = getMutBox(coords)
            elem.update { f(it, coords) }
        }
    }

    fun <R> map(f: (T) -> R): Array4D<R> = map { it, _ -> f(it) }
    fun <R> map(f: (T, Coords4D) -> R): Array4D<R> {
        return Array4D(list.mapIndexed { x, xList ->
            xList.mapIndexed { y, yList  ->
                yList.mapIndexed { z, zList ->
                    zList.mapIndexed { w, value ->
                        MutBox(f(value.get(), Coords4D(x, y, z, w)))
                    }
                }
            }
        })
    }

    companion object {
        fun <T> initTesseract(size: Int, default: (Coords4D) -> T): Array4D<T> {
            return init(size, size, size, size, default)
        }
        fun <T> init(xSize: Int, ySize: Int, zSize: Int, wSize: Int, default: (Coords4D) -> T): Array4D<T> {
            return Array4D(List(xSize) { x -> List(ySize) { y -> List(zSize) { z -> List(wSize) { w -> MutBox(default(Coords4D(x, y, z, w))) } } } })
        }
    }

    override fun iterator(): Iterator<T> {
        return list.asSequence().flatMap { it.asSequence() }.flatMap { it.asSequence() }.flatMap { it.asSequence() }.map { it.get() }.iterator()
    }
}