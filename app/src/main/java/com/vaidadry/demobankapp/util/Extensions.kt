package com.vaidadry.demobankapp.util

fun Any.allTrue(vararg booleans: Boolean): Boolean {
    return booleans.all { it }
}