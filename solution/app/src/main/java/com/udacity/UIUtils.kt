package com.udacity

import android.content.res.Resources
import android.util.TypedValue

fun Number.toUnit(unit: Int): Float =
        TypedValue.applyDimension(unit, toFloat(), Resources.getSystem().displayMetrics)

inline val Number.dp get() = toUnit(TypedValue.COMPLEX_UNIT_DIP)