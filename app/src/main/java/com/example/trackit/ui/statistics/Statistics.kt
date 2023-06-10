package com.example.trackit.ui.statistics

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackit.R
import com.example.trackit.ui.theme.Arsenic
import com.example.trackit.ui.theme.BrightGray
import java.time.LocalDate

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Statistics(
    navigateBack: () -> Unit,
    selectedDate: LocalDate = LocalDate.now()
) {
    Column {
        Row(
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, top = 30.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = navigateBack
            ) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    backgroundColor = BrightGray,
                    elevation = 8.dp,
                    modifier = Modifier
                        .size(54.dp)
                ) {
                    Icon(
                        Icons.Rounded.ArrowBack,
                        contentDescription = "Вернуться",
                        modifier = Modifier
                            .size(40.dp)
                            .requiredSize(40.dp),
                        tint = Arsenic
                    )
                }
            }

            Icon(
                painterResource(R.drawable.line),
                null,
                Modifier
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 8.dp)
                    .weight(2f),
                Arsenic
            )

            Text(
                text = "Статистика",
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.Default,
                softWrap = false,
                color = Arsenic,
                modifier = Modifier.offset(y = (-4).dp)
            )

            Icon(
                painterResource(R.drawable.line),
                null,
                Modifier
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 8.dp)
                    .weight(2f),
                Arsenic
            )

            Card(
                shape = RoundedCornerShape(20.dp),
                backgroundColor = BrightGray,
                elevation = 8.dp,
                modifier = Modifier
                    .size(54.dp)
            ) {
                Icon(
                    painterResource(id = R.drawable.insights),
                    contentDescription = null,
                    modifier = Modifier.padding(5.dp),
                    tint = Arsenic
                )
            }
        }
        WeightStats(date = selectedDate)
    }
}