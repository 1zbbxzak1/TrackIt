package com.example.trackit.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.ComponentActivity
import com.example.trackit.R

class LoadActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load)

        val isFirstRun = isFirstRun()

        Handler().postDelayed({
            if (isFirstRun) {
                // Первый запуск - переход к WelcomeActivity
                val intent = Intent(this@LoadActivity, WelcomeActivity::class.java)
                startActivity(intent)
            } else {
                // Не первый запуск - переход к MainActivity
                val intent = Intent(this@LoadActivity, MainActivity::class.java)
                startActivity(intent)
            }
            finish()
        }, 30)
    }

    private fun isFirstRun(): Boolean {
        val sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val isFirstRun = sharedPreferences.getBoolean("isFirstRun", true)
        if (isFirstRun) {
            // Установка флага, что приложение уже было запущено
            sharedPreferences.edit().putBoolean("isFirstRun", false).apply()
        }
        return isFirstRun
    }
}
