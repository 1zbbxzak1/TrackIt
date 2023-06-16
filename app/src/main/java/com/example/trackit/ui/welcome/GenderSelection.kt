package com.example.trackit.ui.welcome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackit.R
import com.example.trackit.ui.theme.Arsenic
import com.example.trackit.ui.theme.BrightGray
import com.example.trackit.ui.theme.TrackItTheme
import com.example.trackit.ui.theme.WelcomeLabelTextStyle

@Composable
fun GenderSelection(
    modifier: Modifier = Modifier,
    onSelected: (Gender) -> Unit = {}
){
    val selectedGender = remember { mutableStateOf<Gender?>(null) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Какой у вас пол?",
            style = WelcomeLabelTextStyle
        )

        Spacer(Modifier.height(100.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            GenderCard(
                gender = Gender.Man,
                onSelected = {
                    onSelected(it)
                    selectedGender.value = it
                },
                selected = selectedGender.value == Gender.Man
            )

            GenderCard(
                gender = Gender.Woman,
                onSelected = {
                    onSelected(it)
                    selectedGender.value = it
                },
                selected = selectedGender.value == Gender.Woman
            )
        }
    }
}

@Composable
private fun GenderCard(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    onSelected: (Gender) -> Unit,
    gender: Gender
){
    val cardColor = if (selected) Arsenic else BrightGray
    val textColor = if (selected) Color.White else Arsenic

    Button(
        modifier = modifier,
        onClick = { onSelected(gender) },
        elevation = ButtonDefaults.elevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = cardColor),
        contentPadding = PaddingValues(0.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(5.dp))
            Icon(painter = painterResource(gender.iconId), contentDescription = null, tint = Color.Unspecified)

            Spacer(Modifier.height(25.dp))

            Text(
                text = gender.text,
                style = TextStyle(
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = textColor
                )
            )
        }
    }
}

enum class Gender(val text: String, val iconId: Int) {
    Man("Мужчина", R.drawable.man),
    Woman("Женщина", R.drawable.woman)
}

@Preview
@Composable
private fun GenderSelectionPreview(){
    TrackItTheme {
        GenderSelection()
    }
}