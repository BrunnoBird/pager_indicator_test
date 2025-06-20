package com.tek.pager_indicator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tek.pagerindicator.PagerIndicator


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(modifier = Modifier.fillMaxSize()) {
                Column(Modifier.fillMaxSize()) {
                    Column(modifier = Modifier.weight(1f)) {
                        PagerIndicatorContent()
                    }
                }
            }
        }
    }
}

@Composable
fun PagerIndicatorContent() {
    var currentIndex by remember { mutableStateOf(0) }
    val pageCount = 11

    PagerIndicator(
        modifier = Modifier
            .height(50.dp)
            .background(Color.LightGray),
        pageCount = pageCount,
        currentIndex = currentIndex,
    )
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
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

