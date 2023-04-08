package com.example.trackit.Activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import com.example.trackit.R

class ProfileActivity : Activity() {

    private lateinit var editText: EditText
    private val sharedPreferencesKey = "my_key"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val genderRadioGroup = findViewById<RadioGroup>(R.id.radio_group_gender)

        genderRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.maleRadioButton) {
                // Мужской RadioButton выбран
                val radioButton = group.findViewById<RadioButton>(checkedId)
                radioButton.setBackgroundResource(R.drawable.gender_back)
                group.setBackgroundResource(android.R.color.transparent)
            } else if (checkedId == R.id.femaleRadioButton) {
                // Женский RadioButton выбран
                val radioButton = group.findViewById<RadioButton>(checkedId)
                radioButton.setBackgroundResource(R.drawable.gender_back)
                group.setBackgroundResource(android.R.color.transparent)
            }
        }

        editText = findViewById(R.id.height_edit_text)

        // загружаем сохраненное значение
        val sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val savedText = sharedPreferences.getString(sharedPreferencesKey, "")
        editText.setText(savedText)

        // сохраняем введенное значение при каждом изменении
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val editor = sharedPreferences.edit()
                editor.putString(sharedPreferencesKey, s.toString())
                editor.apply()
            }
            override fun afterTextChanged(s: Editable) {}
        })
    }
}