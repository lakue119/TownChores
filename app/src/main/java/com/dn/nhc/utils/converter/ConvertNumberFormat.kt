package com.dn.nhc.utils.converter

import java.text.DecimalFormat

object ConvertNumberFormat {

    fun numberFormat(count: Int, unit: String): String? {
        val decimalFormat = DecimalFormat("#,##0")
        return "${decimalFormat.format(count.toLong())}$unit"
    }
}