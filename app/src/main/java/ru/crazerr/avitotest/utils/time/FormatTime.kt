package ru.crazerr.avitotest.utils.time

fun Long.formatTime(): String {
    val minutes = (this / 1000) / 60
    val seconds = (this / 1000) % 60
    return "%02d:%02d".format(minutes, seconds)
}