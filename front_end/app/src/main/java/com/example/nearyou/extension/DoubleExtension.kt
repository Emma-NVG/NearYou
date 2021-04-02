package com.example.nearyou.extension

fun Double.format(digits: Int) = "%.${digits}f".format(this)