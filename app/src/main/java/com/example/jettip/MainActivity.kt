package com.example.jettip

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import com.example.jettip.components.BillTextField
import com.example.jettip.ui.theme.JetTipTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApp {
                JetTipApp()
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable ()->Unit){
    JetTipTheme {
        content()
    }
}

@Composable
fun JetTipApp(){
    var totalPrice by remember { mutableStateOf("") }
    var progress by remember { mutableStateOf(0f) }
    var numbers by remember { mutableStateOf("1") }

    Surface (
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .safeDrawingPadding()
    ){
        Column (
            modifier = Modifier.padding(10.dp)
        ){
            TotalCard((totalSplit(totalPrice, numbers) + tipCalc(totalPrice, progress, numbers)).toString())
            CalculatorCard(
                totalPrice,
                onTotalPriceChange = {totalPrice = it},
                progress,
                onProgressValueChange = {progress = it},
                numbers,
                onNumberValueChange = {numbers = it.toString()}
            )
        }
    }
}

private fun totalSplit(totalPrice: String, numbers: String): Int{
    if(totalPrice != ""){
        return totalPrice.toInt() / numbers.toInt()
    }else {
        return 0
    }
}

private fun tipCalc(totalPrice: String, progress: Float, numbers: String): Int{
    if(totalPrice != ""){
        return totalPrice.toInt() / numbers.toInt() * progress.toInt() / 100
    }else {
        return 0
    }
}

@Composable
fun TotalCard(perPersonPrice: String, modifier: Modifier = Modifier){
    Card (
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(Color(0xFFBB86FC)),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
    ){
        Column (
            modifier = modifier
                .padding(vertical = 30.dp, horizontal = 20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Text(
                text = "Total Per Person",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(
                modifier.height(10.dp)
            )
            Text(
                text = "$$perPersonPrice",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun CalculatorCard(
    totalPrice: String,
    onTotalPriceChange: (String) -> Unit,
    progress: Float,
    onProgressValueChange: (Float) -> Unit,
    numbers: String,
    onNumberValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier
){
    OutlinedCard(
        border = BorderStroke(1.dp, Color.LightGray),
        modifier = modifier.padding(5.dp),
        shape = RoundedCornerShape(4.dp)
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .padding(10.dp)
        ){
            BillTextField(
                totalPrice = totalPrice,
                onValueChange = { onTotalPriceChange(it) }
            )
            Counter(
                totalPrice = totalPrice,
                progress = progress,
                onProgressValueChange = { onProgressValueChange(it) },
                numbers = numbers,
                onNumberValueChange = { onNumberValueChange(it) }
            )
        }
    }
}

@Composable
fun Counter(
    totalPrice: String,
    progress: Float,
    onProgressValueChange: (Float) -> Unit,
    numbers: String,
    onNumberValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier
){
    Log.d("TAG", "Counter: totalPrice $totalPrice, progress $progress, numbers $numbers")
    Column (
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ){
        Row (
            modifier = modifier
                .fillMaxWidth()
        ){
            Text(
                text = "Split",
            )
            Spacer(
                modifier.width(120.dp)
            )
            Card (
                shape = CircleShape,
                colors = CardDefaults.cardColors(Color.White),
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = modifier
                    .size(24.dp)
                    .clickable {
                        if (numbers.toInt() > 1) {
                            onNumberValueChange(numbers.toInt() - 1)
                        }
                    }
            ){
                Icon(
                    Icons.Rounded.Add,
                    contentDescription = "Add"
                )
            }
            Text(
                text = numbers,
                modifier = modifier.padding(horizontal = 6.dp)
            )
            Card (
                shape = CircleShape,
                colors = CardDefaults.cardColors(Color.White),
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = modifier
                    .size(24.dp)
                    .clickable {
                        onNumberValueChange(numbers.toInt() + 1)
                    }
            ){
                Icon(
                    Icons.Rounded.Add,
                    contentDescription = "Add"
                )
            }
        }
        Row (
            modifier = modifier
                .fillMaxWidth()
        ){
            Text(
                text = "Tip",
            )
            Spacer(
                modifier.width(150.dp)
            )
            Text(
                text = "${if(totalPrice == "") 0 else totalPrice.toInt() * progress.toInt() / 100}",
            )
        }
        Text(
            text = "${progress.toInt()} %",
            textAlign = TextAlign.Center,
            modifier = modifier
                .fillMaxWidth()
        )
        Column {
            Slider(
                value = progress,
                onValueChange = {onProgressValueChange(it)},
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFFBB86FC),
                    activeTrackColor = Color(0xFFBB86FC),
                    inactiveTrackColor = Color(0x43BB86FC),
                ),
                steps = 100,
                valueRange = 0f..100f
            )
        }
    }
}


@Preview
@Composable
fun JetTipAppPreview() {
    JetTipTheme {
        JetTipApp()
    }
}
