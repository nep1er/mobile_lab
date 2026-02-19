package ci.nsu.moble.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ci.nsu.moble.main.ui.theme.PracticeTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ChangeText(name: String, modifier: Modifier = Modifier) {
    Column {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )

        Text(
            text = "АББА РАБА",
            modifier = Modifier.padding(horizontal = 0.dp, vertical = 89.dp)
        )
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    var myBool by remember { mutableStateOf(true) }

    Column {
        if (myBool) {
            ChangeText(name = "АББА", modifier = modifier)
        }
        else {
            ChangeText(name = "РАБА", modifier = modifier)
        }

        Button(
            onClick = {
                println("Кнопка нажата!")
                myBool = !myBool
            },
            modifier = Modifier.padding(horizontal = 50.dp, vertical = 200.dp)
        ) {
            Text(text = "Нажми меня")
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PracticeTheme {
        Greeting("Android")
    }
}