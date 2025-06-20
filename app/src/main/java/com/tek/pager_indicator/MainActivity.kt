package com.tek.pager_indicator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tek.pagerindicator.DotStyle
import com.tek.pagerindicator.PagerIndicator
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(modifier = Modifier.fillMaxSize()) {
                Column(Modifier.fillMaxSize()) {
                    Column(modifier = Modifier.weight(1f)) {
                        HorizontalPagerIndicator()
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        VerticalPagerIndicator()
                    }

                }
            }
        }
    }
}

@Composable
fun HorizontalPagerIndicator() {
    var currentIndex by remember { mutableStateOf(0) }
    val pageCount = 7

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PagerIndicator(
            modifier = Modifier
                .background(Color.Yellow),
            pageCount = pageCount,
            currentIndex = currentIndex,
            orientation = Orientation.Horizontal
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Button(onClick = { if (currentIndex > 0) currentIndex-- }) {
                Text("Prev")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { if (currentIndex < pageCount - 1) currentIndex++ }) {
                Text("Next")
            }
        }
    }

}

@Composable
fun VerticalPagerIndicator() {
    var currentIndex by remember { mutableStateOf(0) }
    val pageCount = 11

    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        PagerIndicator(
            modifier = Modifier.background(Color.Red),
            pageCount = pageCount,
            currentIndex = currentIndex,
            dotStyle = DotStyle.defaultDotStyle.copy(
                visibleDotCount = 9,
                currentDotColor = Color.Yellow,
                regularDotColor = Color.White
            )
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Button(onClick = { if (currentIndex > 0) currentIndex-- }) {
                Text("Prev")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { if (currentIndex < pageCount - 1) currentIndex++ }) {
                Text("Next")
            }
        }
    }

}

