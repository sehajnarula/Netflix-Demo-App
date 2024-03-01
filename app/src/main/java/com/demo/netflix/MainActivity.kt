package com.demo.netflix

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Handler(Looper.getMainLooper()).postDelayed({
            sharedPreferences = getSharedPreferences(packageName, MODE_PRIVATE)
            if (sharedPreferences.getBoolean("userloggedin",false) == true) {
                val homeIntent = Intent(this@MainActivity, HomeScreen::class.java)
                startActivity(homeIntent)
            }
            else
            {
                val homeIntent = Intent(this@MainActivity, LoginScreen::class.java)
                startActivity(homeIntent)
            }

        },3000)
    }
}