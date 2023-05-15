package com.example.trackit.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
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
import androidx.navigation.NavHostController
import com.example.trackit.R
import com.example.trackit.ui.theme.TrackItTheme


@SuppressLint("InflateParams", "SuspiciousIndentation")
@Composable
fun ProfilePage() {
    val context = LocalContext.current

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { contxt ->
            LayoutInflater.from(contxt).inflate(R.layout.activity_profile, null)
        })
         { view ->

             val height = view.findViewById<EditText>(R.id.height_edit_text)
             val sharedPreferencesKeyH = "height"
             val savedHeight = loadFromSharedPreferences(context, sharedPreferencesKeyH, "")
             height.setText(savedHeight)

             height.addTextChangedListener(object : TextWatcher {
                 override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                 override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                     saveToSharedPreferences(context, sharedPreferencesKeyH, s.toString())
                 }

                 override fun afterTextChanged(s: Editable) {}
             })

             val age = view.findViewById<EditText>(R.id.age_edit_text)
             val sharedPreferencesKeyA = "age"
             val savedAge = loadFromSharedPreferences(context, sharedPreferencesKeyA, "")
             age.setText(savedAge)

             age.addTextChangedListener(object : TextWatcher {
                 override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                 override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                     saveToSharedPreferences(context, sharedPreferencesKeyA, s.toString())
                 }

                 override fun afterTextChanged(s: Editable) {}
             })

             //
             //
             //

             val weight = view.findViewById<EditText>(R.id.weight_edit_text)
             val sharedPreferencesKeyW = "weight"
             val savedWeight = loadFromSharedPreferences(context, sharedPreferencesKeyW, "")
             weight.setText(savedWeight)

             weight.addTextChangedListener(object : TextWatcher {
                 override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                 override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                     saveToSharedPreferences(context, sharedPreferencesKeyW, s.toString())
                 }

                 override fun afterTextChanged(s: Editable) {}
             })

             //
             //
             //

             val genderRadioGroup = view.findViewById<RadioGroup>(R.id.radio_group_gender)
             val fem = view.findViewById<RadioButton>(R.id.femaleRadioButton)
             val male = view.findViewById<RadioButton>(R.id.maleRadioButton)

             val sharedPreferencesGender = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
             val sharedPreferencesKeyG = "gender"
             val savedGenderIndex = sharedPreferencesGender.getInt(sharedPreferencesKeyG, -1)

             // Настройка выбранной радиокнопки на основе сохраненных данных
             if (savedGenderIndex != -1) {
                 setRadioButtonState(genderRadioGroup, savedGenderIndex)
             }

             // Настройка цвета фона радиогруппы
             genderRadioGroup.setBackgroundColor(ContextCompat.getColor(context, R.color.gray))

             // Установка слушателей для радиокнопок
             genderRadioGroup.setOnCheckedChangeListener { group, checkedId ->
                 when (checkedId) {
                     R.id.maleRadioButton -> {
                         setRadioButtonState(group, 0)
                         fem.background = null
                         saveGenderPreference(sharedPreferencesGender, sharedPreferencesKeyG, 0)
                     }
                     R.id.femaleRadioButton -> {
                         setRadioButtonState(group, 1)
                         male.background = null
                         saveGenderPreference(sharedPreferencesGender, sharedPreferencesKeyG, 1)
                     }
                 }

                 // Установить цвет текста выбранной радиокнопки и сохранить его
                 val checkedRadioButton = view.findViewById<RadioButton>(checkedId)
                 if (checkedRadioButton != null) {
                     checkedRadioButton.setTextColor(ContextCompat.getColor(context, R.color.white))
                     saveSelectedRadioButtonId(sharedPreferencesGender, checkedId)
                 }

                 // Снять флажки со всех остальных радиокнопок и установить для них черный цвет текста
                 for (i in 0 until group.childCount) {
                     val radioButton = group.getChildAt(i) as RadioButton
                     if (radioButton.id != checkedId) {
                         radioButton.setTextColor(ContextCompat.getColor(context, R.color.black))
                         radioButton.isChecked = false
                     }
                 }
             }

             // Установить цвет текста выбранной радиокнопки, если она была сохранена
             val savedSelection = sharedPreferencesGender.getInt("selectedRadioButtonId", -1)
             if (savedSelection != -1) {
                 genderRadioGroup.check(savedSelection)
                 setTextColorForSelectedRadioButton(view, savedSelection, R.color.white)
             }
        }
//    LineChartScreen()
}

// Сохранение введенного значения
fun saveToSharedPreferences(context: Context, key: String, value: String) {
    val sharedPreferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString(key, value)
    editor.apply()
}

// Выгрузка сохранненного значения
fun loadFromSharedPreferences(context: Context, key: String, defaultValue: String): String {
    val sharedPreferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
    return sharedPreferences.getString(key, defaultValue) ?: defaultValue
}

// Сохранение выбранного пола
private fun saveGenderPreference(sharedPreferences: SharedPreferences, key: String, index: Int) {
    with(sharedPreferences.edit()) {
        putInt(key, index)
        apply()
    }
}

// Выделение выбранного пола
fun setRadioButtonState(group: RadioGroup, index: Int) {
    val radioButton = group.getChildAt(index) as? RadioButton
    radioButton?.apply {
        isChecked = true
        setBackgroundResource(R.drawable.gender_back)
    }
}

// Сохранение выбранной радиокнопки по id
fun saveSelectedRadioButtonId(sharedPreferencesGender: SharedPreferences, checkedId: Int) {
    val editorGender = sharedPreferencesGender.edit()
    editorGender.putInt("selectedRadioButtonId", checkedId)
    editorGender.apply()
}

// Установка цвета текста кнопки
fun setTextColorForSelectedRadioButton(view: View, selectedId: Int, color: Int) {
    val selectedRadioButton = view.findViewById<RadioButton>(selectedId)
    selectedRadioButton?.setTextColor(ContextCompat.getColor(view.context, color))
}

@Preview(showBackground = true)
@Composable
fun PreviewHomePage(){
    TrackItTheme {
    }
}