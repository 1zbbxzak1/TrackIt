package com.example.trackit.ui.welcome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.trackit.R
import com.example.trackit.ui.theme.Arsenic
import com.example.trackit.ui.theme.TrackItTheme
import com.example.trackit.ui.theme.WelcomeCaptionTextStyle
import com.example.trackit.ui.theme.WelcomeLabelTextStyle
import com.example.trackit.ui.theme.WelcomeTextFieldStyle

@Composable
fun PageWithTextField(
    modifier: Modifier = Modifier,
    onSelected: (TextFieldValue) -> Unit = {},
    onKeyboardAction: () -> Unit = {},
    label: String = "",
    caption: String = "",
    imeAction: ImeAction
){
    val textFieldValue = remember { mutableStateOf(TextFieldValue()) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(50.dp)
    ) {
        Text(
            text = label,
            style = WelcomeLabelTextStyle
        )

        WelcomeTextField(
            modifier = Modifier.width(200.dp),
            value = textFieldValue.value,
            onValueChange = {
                textFieldValue.value = it
                onSelected(it)
            },
            onKeyboardAction = onKeyboardAction,
            caption = caption,
            imeAction = imeAction
        )
    }
}

@Composable
fun WelcomeTextField(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    caption: String = "",
    onKeyboardAction: () -> Unit = {},
    imeAction: ImeAction
){
    val customTextSelectionColors = TextSelectionColors(
        handleColor = Transparent,
        backgroundColor = Transparent,
    )

    val focusRequester = remember { FocusRequester() }

    CompositionLocalProvider(
        LocalTextSelectionColors provides customTextSelectionColors,
    ) {
        BasicTextField(
            modifier = Modifier.focusRequester(focusRequester),
            value = value,
            onValueChange = { onValueChange(it) },
            textStyle = WelcomeTextFieldStyle,
            maxLines = 1,
            singleLine = true,
            cursorBrush = SolidColor(Arsenic),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = imeAction),
            keyboardActions = KeyboardActions(
                onNext = {onKeyboardAction()},
                onDone = {
                    onKeyboardAction()
                }
            )
        ){innerTextField ->
            Card(
                modifier = modifier.height(80.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = -10.dp)
                        .zIndex(3f),
                    horizontalArrangement = Arrangement.Center
                ) {
                    innerTextField()
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 15.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(text = caption, style = WelcomeCaptionTextStyle)
                    }

                    Spacer(Modifier.height(4.dp))

                    Icon(
                        painter = painterResource(R.drawable.text_field_line),
                        contentDescription = null
                    )
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Preview
@Composable
private fun PageWithTextFieldPreview(){
    TrackItTheme {
        PageWithTextField(label = "Сколько вам лет?", imeAction = ImeAction.Next)
    }
}