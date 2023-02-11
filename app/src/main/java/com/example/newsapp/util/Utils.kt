package com.example.newsapp.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun timeFormatter(time: String): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    val sdfUI = SimpleDateFormat("dd MMM, yyyy - hh:mm aa")
    sdf.timeZone = TimeZone.getTimeZone("IST")
    return sdfUI.format(sdf.parse(time))
}