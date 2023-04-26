package com.kachundena.ktenes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.kachundena.ktenes.ui.theme.KTenesTheme

class About : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KTenesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Content()
                }
            }
        }
    }
}

@Preview
@Composable
fun Content() {
    val activity = (LocalContext.current as? Activity)
    Column() {
        Text(text = "About KTenes",
            modifier = Modifier
                .padding(50.dp,70.dp,50.dp,20.dp)
                .fillMaxWidth(),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(text = "Inventory Management",
            modifier = Modifier
                .padding(50.dp,10.dp,50.dp,10.dp)
                .fillMaxWidth(),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(text = "by Alex Lleida ",
            modifier = Modifier
                .padding(50.dp,10.dp,50.dp,10.dp)
                .fillMaxWidth(),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(text = "February/2023 - Barcelona (Spain)",
            modifier = Modifier
                .padding(50.dp,5.dp,50.dp,10.dp)
                .fillMaxWidth(),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Image(
            painterResource(id = R.drawable.ic_storage),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clickable(
                    onClick = {
                    }
                )
        )
        Button(
            onClick = {
                activity?.finish()
            },
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
        ) {
            Text("Exit")
        }

    }
}

