package com.demo.netflix.utils

object CommonFunctions {
    fun isValidEmail(email:String):Boolean
    {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}