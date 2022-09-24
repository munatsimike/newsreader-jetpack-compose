package nl.project.michaelmunatsi

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import nl.project.michaelmunatsi.ui.navigation.NewsAppNavGraph
import nl.project.michaelmunatsi.ui.theme.MichaelmunatsiTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MichaelmunatsiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NewsAppNavGraph()
                }
            }
        }
    }
}



@Composable
fun DetailPage(
    modifier: Modifier = Modifier,
    onDetailClick: (name: String) -> Unit,
    detailId: Int
) {
    Column {
        Text(text = "I am the int $detailId")
        Button(onClick = { onDetailClick.invoke("") }) {

        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MichaelmunatsiTheme {
        NewsAppNavGraph()
    }
}