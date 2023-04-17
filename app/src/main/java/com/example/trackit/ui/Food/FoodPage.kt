package com.example.trackit.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.widget.Button
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.trackit.R
import com.example.trackit.ui.theme.TrackItTheme
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*


@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "InflateParams")
@Composable
fun FoodPage(
    navigateToEntry: () -> Unit,
    selectedDate: LocalDate = LocalDate.now(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(modifier = modifier) {
        Text(
            text = "Питание",
            fontSize = 50.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                LayoutInflater.from(context).inflate(R.layout.activity_nutrition, null)
            }
        ) { view ->
            val button = view.findViewById<Button>(R.id.add_button)
            button.setOnClickListener {
                navigateToEntry()
            }

            val button2 = view.findViewById<Button>(R.id.add_button2)
            button2.setOnClickListener {
                navigateToEntry()
            }

            val button3 = view.findViewById<Button>(R.id.add_button3)
            button3.setOnClickListener {
                navigateToEntry()
            }

            val button4 = view.findViewById<Button>(R.id.add_button4)
            button4.setOnClickListener {
                navigateToEntry()
            }

        }

        Divider()

        Text(text = selectedDate.dayOfMonth.toString() + " " + selectedDate.month
            .getDisplayName(TextStyle.FULL, Locale.getDefault())
            .lowercase()
            .replaceFirstChar { it.titlecase() } + " " + selectedDate.year,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Divider()

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFoodPage(){
    TrackItTheme {

    }
}