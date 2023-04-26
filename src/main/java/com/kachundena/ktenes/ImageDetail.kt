package com.kachundena.ktenes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.kachundena.ktenes.ui.theme.KTenesTheme

class ImageDetail : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent: Intent = getIntent()
        val image: String = intent.getStringExtra("image")!!.toString()
        setContent {
            KTenesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column() {
                        Image(
                            painter = rememberImagePainter(image),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(600.dp)
                        )
                        Button(
                            onClick = {
                                finish()
                            },
                            modifier = Modifier
                                .padding(8.dp)
                                .height(60.dp)
                                .fillMaxWidth(),
                        ) {
                            Text("Cerrar")
                        }
                    }
                }
            }
        }
    }
}
