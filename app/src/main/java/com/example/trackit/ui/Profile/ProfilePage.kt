package com.example.trackit.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trackit.R
import com.example.trackit.data.Weight.WeightEntry
import com.example.trackit.data.Weight.WeightViewModel
import com.example.trackit.ui.statistics.WeightStats
import com.example.trackit.ui.theme.Arsenic
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("InflateParams", "SuspiciousIndentation")
@Composable
fun ProfilePage(
    selectedDate: LocalDate = LocalDate.now(),
    navigateToStats: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: WeightViewModel = viewModel(factory = AppViewModelProvider.Factory)

    val lastWeightState = remember { mutableStateOf(0.0) }

    LaunchedEffect(Unit) {
        lastWeightState.value = viewModel.getLast()
    }

    LazyColumn {
        item {
            Surface(
                color = MaterialTheme.colors.primaryVariant,
                shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp),
                modifier = Modifier
                    .height(70.dp)
                    .zIndex(1f)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        painterResource(id = R.drawable.profile_label),
                        contentDescription = null,
                        tint = androidx.compose.ui.graphics.Color.Unspecified
                    )
                }
            }
        }

        item {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { contxt ->
                    LayoutInflater.from(contxt).inflate(R.layout.activity_profile, null)
                }
            ) { view ->

                val weightTextView = view.findViewById<TextView>(R.id.weight_edit_text)
                weightTextView.text = lastWeightState.value.toString()

                val height = view.findViewById<EditText>(R.id.height_edit_text)
                val sharedPreferencesKeyH = "height"
                val savedHeight = loadFromSharedPreferences(context, sharedPreferencesKeyH, "")
                height.setText(savedHeight)

                height.setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        height.clearFocus()
                        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(height.windowToken, 0)
                        true
                    } else {
                        false
                    }
                }

                height.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        saveToSharedPreferences(context, sharedPreferencesKeyH, s.toString())
                    }

                    override fun afterTextChanged(s: Editable) {}
                })

                val age = view.findViewById<EditText>(R.id.age_edit_text)
                val sharedPreferencesKeyA = "age"
                val savedAge = loadFromSharedPreferences(context, sharedPreferencesKeyA, "")
                age.setText(savedAge)

                age.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        saveToSharedPreferences(context, sharedPreferencesKeyA, s.toString())
                    }

                    override fun afterTextChanged(s: Editable) {}
                })

                val genderRadioGroup = view.findViewById<RadioGroup>(R.id.radio_group_gender)
                val fem = view.findViewById<RadioButton>(R.id.femaleRadioButton)
                val male = view.findViewById<RadioButton>(R.id.maleRadioButton)

                val sharedPreferencesGender =
                    context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
                val sharedPreferencesKeyG = "gender"
                val savedGenderIndex = sharedPreferencesGender.getInt(sharedPreferencesKeyG, -1)

                // Настройка выбранной радиокнопки на основе сохраненных данных
                if (savedGenderIndex != -1) {
                    setRadioButtonState(genderRadioGroup, savedGenderIndex)
                }

                // Настройка цвета фона радиогруппы
                genderRadioGroup.setBackgroundColor(ContextCompat.getColor(context, R.color.white))

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
                        checkedRadioButton.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.white
                            )
                        )
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
        }

        item {
            Card(
                onClick = { navigateToStats() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 20.dp)
                    .height(70.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = 8.dp,
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = "Статистика",
                        modifier = Modifier.weight(1f),
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Medium,
                            fontSize = 24.sp,
                            color = Arsenic
                        )
                    )

                    Icon(
                        painterResource(id = R.drawable.insights),
                        contentDescription = "Статистика",
                        tint = Arsenic,
                        modifier = Modifier.size(50.dp)
                    )
                }
            }
        }
    }
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