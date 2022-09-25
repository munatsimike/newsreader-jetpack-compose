package nl.project.michaelmunatsi

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import nl.project.michaelmunatsi.ui.navigation.BottomNavigationMenuBuilder
import nl.project.michaelmunatsi.ui.navigation.NewsAppNavGraph
import nl.project.michaelmunatsi.ui.navigation.NewsReaderToolBar
import nl.project.michaelmunatsi.ui.theme.MichaelmunatsiTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MichaelmunatsiTheme {
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Scaffold(
                        topBar = { NewsReaderToolBar() },
                        bottomBar =
                        { BottomNavigationMenuBuilder(navController) }) { padding ->
                        NewsAppNavGraph(navController, padding)
                    }

                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MichaelmunatsiTheme {
    }
}