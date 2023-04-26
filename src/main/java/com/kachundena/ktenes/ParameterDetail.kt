package com.kachundena.ktenes

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kachundena.ktenes.controller.ParameterController
import com.kachundena.ktenes.model.Item
import com.kachundena.ktenes.model.Parameter
import com.kachundena.ktenes.ui.theme.KTenesTheme

class ParameterDetail : ComponentActivity() {
    private var parameter: Parameter? = null
    private var parameterController: ParameterController? = null
    private var action = "UPDATE"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parameterController = ParameterController(this)
        parameter = parameterController?.getParameters()
        if (parameter == null) {
            parameter =
                Parameter(company = 0, apiurl = "", logo = "", key = "", lastsync_item = "")
            action = "INSERT"
        }
        setContent {
            KTenesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ObjectContent(parameter)
                }
            }
        }
    }

    @Composable
    fun ObjectContent(parameter: Parameter?) {
        val context = LocalContext.current
        val activity = (LocalContext.current as? Activity)
        val parameterController: ParameterController = ParameterController(this)

        if (parameter != null) {
            var company by rememberSaveable { mutableStateOf(parameter.company.toString()) }
            var apiurl by rememberSaveable { mutableStateOf(parameter.apiurl) }
            var logo by rememberSaveable { mutableStateOf(parameter.logo) }
            var key by rememberSaveable { mutableStateOf(parameter.key) }
            var lastsync_item by rememberSaveable { mutableStateOf(parameter.lastsync_item) }
            Column() {
                TextField(
                    value = company,
                    modifier = Modifier
                        .padding(1.dp)
                        .fillMaxWidth(),
                    label = { Text(text = "Compañia") },
                    onValueChange = {
                        company = it
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                    )
                )
                TextField(
                    value = apiurl,
                    modifier = Modifier
                        .padding(1.dp)
                        .fillMaxWidth(),
                    label = { Text(text = "URL API") },
                    onValueChange = {
                        apiurl = it
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                    )
                )
                TextField(
                    value = logo,
                    modifier = Modifier
                        .padding(1.dp)
                        .fillMaxWidth(),
                    label = { Text(text = "URL Logo") },
                    onValueChange = {
                        logo = it
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                    )
                )
                TextField(
                    value = key,
                    modifier = Modifier
                        .padding(1.dp)
                        .fillMaxWidth(),
                    label = { Text(text = "Key") },
                    onValueChange = {
                        key = it
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next,
                    )
                )
                TextField(
                    value = lastsync_item,
                    modifier = Modifier
                        .padding(1.dp)
                        .fillMaxWidth(),
                    label = { Text(text = "Fecha Ultima Sync Item") },
                    onValueChange = {
                        lastsync_item= it
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next,
                    )
                )
                Row() {
                    Button(
                        onClick = {
                            val _parameter: Parameter =
                                Parameter(company.toInt(), apiurl, logo, key, lastsync_item)
                            if (action == "INSERT") {
                                parameterController?.createParameters(_parameter)
                            } else {
                                if (action == "UPDATE") {
                                    parameterController?.updateParameters(_parameter)
                                }
                            }
                            activity?.finish()
                        },
                        modifier = Modifier
                            .padding(8.dp),
                    ) {
                        Text("Accept")
                    }
                    Button(
                        onClick = {
                            activity?.finish()
                        },
                        modifier = Modifier
                            .padding(8.dp),
                    ) {
                        Text("Cancel")
                    }

                }
            }
        }
    }
}


