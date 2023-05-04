package com.example.trackit.ui.statistics

import android.security.identity.CredentialDataResult.Entries
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.trackit.ui.theme.TrackItTheme
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.entriesOf
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.marker.Marker
import java.security.KeyStore.Entry
import java.time.LocalDate

@Composable
fun WeightChart(
    weightsWithDate: List<Pair<LocalDate, Float>>,
    modifier: Modifier = Modifier
){
    val bottomAxisValueFormatter =
        AxisValueFormatter<AxisPosition.Horizontal.Bottom> { x, _ -> weightsWithDate[x.toInt()].first.toString() }

    //Chart(
    //    chart = ,
    //    model =
    //)

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WeightChartPreview(){
    TrackItTheme {
        val chartEntryModelProducer  = listOf(
            LocalDate.now().minusDays(1) to 2f,
            LocalDate.now() to 4f
        )

        //WeightChart(weights)
    }
}