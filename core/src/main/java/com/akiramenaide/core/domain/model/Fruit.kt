package com.akiramenaide.core.domain.model

import java.math.RoundingMode
import java.text.DecimalFormat

data class Fruit(
    val id: Int,
    var name: String,
    var total: Int,
    var freshTotal: Int
) {
    private val df = DecimalFormat("#.##").apply {
        roundingMode = RoundingMode.HALF_UP
    }
    var notFreshTotal = total - freshTotal
    var freshPercentage: Float = df.format(freshTotal.toFloat() * 100/total.toFloat()).toFloat()
}
