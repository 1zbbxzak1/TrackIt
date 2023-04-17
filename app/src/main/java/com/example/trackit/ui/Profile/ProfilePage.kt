package com.example.trackit.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.trackit.R
import com.example.trackit.ui.theme.TrackItTheme


@SuppressLint("InflateParams", "SuspiciousIndentation")
@Composable
fun ProfilePage() {
    val context = LocalContext.current // get the Context from the LocalContextProvider

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            LayoutInflater.from(context).inflate(R.layout.activity_profile, null)
        })
     { view ->

         val genderRadioGroup = view.findViewById<RadioGroup>(R.id.radio_group_gender)
         val fem = view.findViewById<RadioButton>(R.id.femaleRadioButton)
         val male = view.findViewById<RadioButton>(R.id.maleRadioButton)

         val sharedPreferencesGender = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
         val sharedPreferencesGenderKey = "gender"
         val savedGenderIndex = sharedPreferencesGender.getInt(sharedPreferencesGenderKey, -1)

        // Set up the selected radio button based on saved preference
         if (savedGenderIndex != -1) {
             setRadioButtonState(genderRadioGroup, savedGenderIndex)
         }

        // Set up the radio group's background color
         genderRadioGroup.setBackgroundColor(ContextCompat.getColor(context, R.color.gray))

        // Set up the radio button listeners
         genderRadioGroup.setOnCheckedChangeListener { group, checkedId ->
             when (checkedId) {
                 R.id.maleRadioButton -> {
                     setRadioButtonState(group, 0)
                     fem.background = null
                     saveGenderPreference(sharedPreferencesGender, sharedPreferencesGenderKey, 0)
                 }
                 R.id.femaleRadioButton -> {
                     setRadioButtonState(group, 1)
                     male.background = null
                     saveGenderPreference(sharedPreferencesGender, sharedPreferencesGenderKey, 1)
                 }
             }

             // Set the selected radio button's text color and save it to shared preferences
             val checkedRadioButton = view.findViewById<RadioButton>(checkedId)
             if (checkedRadioButton != null) {
                 checkedRadioButton.setTextColor(ContextCompat.getColor(context, R.color.white))

                 val editor = sharedPreferencesGender.edit()
                 editor.putInt("selectedRadioButtonId", checkedId)
                 editor.apply()
             }

             // Uncheck all other radio buttons and set their text color to black
             for (i in 0 until group.childCount) {
                 val radioButton = group.getChildAt(i) as RadioButton
                 if (radioButton.id != checkedId) {
                     radioButton.setTextColor(ContextCompat.getColor(context, R.color.black))
                     radioButton.isChecked = false
                 }
             }
         }

         // Set the text color of the selected radio button if it was saved
         val savedSelection = sharedPreferencesGender.getInt("selectedRadioButtonId", -1)
         if (savedSelection != -1) {
             genderRadioGroup.check(savedSelection)

             val selectedRadioButton = view.findViewById<RadioButton>(savedSelection)
             selectedRadioButton?.setTextColor(ContextCompat.getColor(context, R.color.white))
         }

         val editText = view.findViewById<EditText>(R.id.height_edit_text)
        val sharedPreferencesKey = "my_key"

        // загружаем сохраненное значение
        val sharedPreferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
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

        val editText2 = view.findViewById<EditText>(R.id.weight_edit_text)
        val sharedPreferencesKey2 = "my_key2"

        // загружаем сохраненное значение
        val sharedPreferences2 = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val savedText2 = sharedPreferences.getString(sharedPreferencesKey2, "")
        editText2.setText(savedText2)

        // сохраняем введенное значение при каждом изменении
        editText2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val editor2 = sharedPreferences2.edit()
                editor2.putString(sharedPreferencesKey2, s.toString())
                editor2.apply()
            }

            override fun afterTextChanged(s: Editable) {}
        })

        val editText3 = view.findViewById<EditText>(R.id.age_edit_text)
        val sharedPreferencesKey3 = "my_key3"

        // загружаем сохраненное значение
        val sharedPreferences3 = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val savedText3 = sharedPreferences.getString(sharedPreferencesKey3, "")
        editText3.setText(savedText3)

        // сохраняем введенное значение при каждом изменении
        editText3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val editor3 = sharedPreferences3.edit()
                editor3.putString(sharedPreferencesKey3, s.toString())
                editor3.apply()
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }
}

private fun saveGenderPreference(sharedPreferences: SharedPreferences, key: String, index: Int) {
    with(sharedPreferences.edit()) {
        putInt(key, index)
        apply()
    }
}

private fun setRadioButtonState(group: RadioGroup, index: Int) {
    val radioButton = group.getChildAt(index) as? RadioButton
    radioButton?.apply {
        isChecked = true
        setBackgroundResource(R.drawable.gender_back)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomePage(){
    TrackItTheme {

    }
}