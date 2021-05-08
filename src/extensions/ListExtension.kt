package extensions

fun <E> List<E>.second(): E {
    return get(1)
}