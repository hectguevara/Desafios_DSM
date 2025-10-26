package com.example.composedemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            ScoreScreen()
        }
    }
}

@Composable
fun ScoreScreen() {
    var scoreTeamA by remember { mutableStateOf(0) }
    var scoreTeamB by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Marcador de Fútbol - GE233262", fontSize = 28.sp)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            // Equipo A
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Barcelona \uD83D\uDC51", fontSize = 20.sp)
                Text(text = "$scoreTeamA", fontSize = 40.sp)
                Button(onClick = { scoreTeamA++ }) {
                    Text("Gol ⚽")
                }
            }

            // Equipo B
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Real Madrid \uD83E\uDD2E", fontSize = 20.sp)
                Text(text = "$scoreTeamB", fontSize = 40.sp)
                Button(onClick = { scoreTeamB++ }) {
                    Text("Gol ⚽")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSoccer() {
    MyApp()
}