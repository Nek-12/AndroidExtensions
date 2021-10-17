@file:Suppress("unused")

package com.nek12.androidutils.extensions.core


import java.time.DayOfWeek
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import kotlin.math.abs
import kotlin.math.log10

/**
 * @param selector is a function using which the value by which we reorder is going to be selected, must be the same
 * value that is specified in the [order]
 * @param order a list of objects that represents the order which should be used to sort the original list
 * @returns A copy of this list ordered according to the order
 */
fun <T, R> Iterable<T>.reorderBy(order: List<R>, selector: (T) -> R): List<T> {
    //associate the values with indexes and create a map
    val orderMap = order.withIndex().associate { it.value to it.index }
    //sort the habits sorted using the comparator that places values not present in a map last
    //and uses the order of the items in the map as its basis for sorting.
    return sortedWith(compareBy(nullsLast()) { orderMap[selector(it)] }).toMutableList()
}

fun <T> MutableList<T>.swap(index1: Int, index2: Int): MutableList<T> {
    val tmp = this[index1] // 'this' corresponds to the list
    this[index1] = this[index2]
    this[index2] = tmp
    return this
}

fun Calendar.setDayOfWeek(dayOfWeek: DayOfWeek) {
    //mapping 1=mon..7=sun -> 1=sun..7=mon
    val mapped: Int = if (dayOfWeek.value == 7) 1 else dayOfWeek.value + 1
    set(Calendar.DAY_OF_WEEK, mapped)
}

fun Int.length() = when (this) {
    0 -> 1
    else -> log10(abs(toDouble())).toInt() + 1
}

fun Boolean.toInt(): Int = if (this) 1 else 0

fun Instant.toZDT(zoneId: ZoneId = ZoneId.systemDefault()): ZonedDateTime = ZonedDateTime.ofInstant(this, zoneId)