package com.example.dotogether.util.helper

import com.example.dotogether.R
import kotlin.math.abs

object ColorGenerator {

    private val colorList = listOf(
        R.color.dark_blue,
        R.color.dark_red,
        R.color.dark_green,
        R.color.dark_orange,
        R.color.orange,
        R.color.dark_purple,
        R.color.white,
        R.color.dark_yellow,
        R.color.dark_burgundy,
        R.color.dark_grey,
    )

    fun getColorForKey(key: String): Int {
        val index = abs(key.hashCode()) % colorList.size
        return colorList[index]
    }
}