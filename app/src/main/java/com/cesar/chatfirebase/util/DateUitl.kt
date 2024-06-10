package com.cesar.chatfirebase.util

import java.text.SimpleDateFormat
import java.util.Date

fun convertStringToDate(dateTime:String): Date? {
    try {
        return SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(dateTime)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}