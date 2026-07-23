package com.harish.floatiq.ui.scientific

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.harish.floatiq.ui.scientific.ScientificCalculatorEngine
@Composable
fun ScientificCalculatorScreen(
    onBack: () -> Unit
) {

    BackHandler {
        onBack()
    }

    var display by remember {
        mutableStateOf("0")
    }

    var result by remember {
        mutableStateOf("")
    }
    val scientificButtons = listOf(
        "sin",
        "cos",
        "tan",
        "log",
        "ln",
        "√",
        "x²",
        "π",
        "e",
        "(",
        ")"
    )


    val calculatorButtons = listOf(
        "C", "⌫", "(", ")",
        "7", "8", "9", "÷",
        "4", "5", "6", "×",
        "1", "2", "3", "-",
        "0", ".", "=", "+"
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF020617),
                        Color(0xFF071226),
                        Color(0xFF0F172A)
                    )
                )
            )
            .padding(20.dp)
    ) {

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Scientific Calculator",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(30.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Expression",
                color = Color.Gray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = display,
                maxLines = 1,
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Result",
                color = Color.Gray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = result,
                maxLines = 1,
                softWrap = false,
                color = Color(0xFF00E676),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(
                    rememberScrollState()
                )
        ) {

            scientificButtons.forEach { button ->

                ScientificCalculatorButton(

                    text = button,

                    onClick = {


                        when(button){

                            "sin" -> {
                                result = ""
                                display =
                                    if(display == "0") "sin("
                                    else display + "sin("
                            }

                            "cos" -> {
                                result = ""
                                display =
                                    if(display == "0") "cos("
                                    else display + "cos("
                            }

                            "tan" -> {
                                result = ""
                                display =
                                    if(display == "0") "tan("
                                    else display + "tan("
                            }

                            "log" -> {
                                result = ""
                                display =
                                    if(display == "0") "log("
                                    else display + "log("
                            }

                            "ln" -> {
                                result = ""
                                display =
                                    if(display == "0") "ln("
                                    else display + "ln("
                            }

                            else -> {
                                result = ""
                                display =
                                    if(display == "0") button
                                    else display + button
                            }
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.fillMaxSize()
        ) {

            items(calculatorButtons) { button ->

                ScientificCalculatorButton(

                    text = button,

                    onClick = {

                        when (button) {


                            "C" -> {

                                display = "0"
                                result = ""
                            }

                            "⌫" -> {
                                result = ""
                                if (display.length > 1) {

                                    display = display.dropLast(1)

                                } else {

                                    display = "0"

                                }
                            }

                            "=" -> {

                                result =
                                    ScientificCalculatorEngine
                                        .evaluate(display)
                            }
                            else -> {

                                display =
                                    if (display == "0")
                                        button
                                    else
                                        display + button
                            }
                        }
                    }
                )
            }
        }
    }
}