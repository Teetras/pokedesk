package com.example.byprokhorenkopmis1

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ScreenInfo(onClick:()->Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.im),
            contentDescription = "TV",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(5.dp)
                .size(200.dp)
                .clip(CircleShape)

        )
Text(text = "Info Screen",
    fontSize = 30.sp)
        Spacer(modifier = Modifier.height((30.dp)))
        Button(onClick = {onClick()}) {
            Text(text = "Коллекция карточек")
            
        }
    }
}