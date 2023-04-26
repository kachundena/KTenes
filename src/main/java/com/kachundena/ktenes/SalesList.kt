package com.kachundena.ktenes

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.kachundena.ktenes.api.kTenesApi
import com.kachundena.ktenes.controller.ItemController
import com.kachundena.ktenes.controller.SalesController
import com.kachundena.ktenes.model.Item
import com.kachundena.ktenes.model.SalesOrder
import com.kachundena.ktenes.ui.theme.KTenesTheme


class SalesList : ComponentActivity() {
    private var moSalesOrder = mutableListOf<SalesOrder>()
    private var salesController: SalesController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        salesController = SalesController(this)
        moSalesOrder.clear()
        moSalesOrder = (salesController?.getSalesOrders() as ArrayList<SalesOrder>)
        setContent {
            KTenesTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "Sales orders",
                                    color = Color.White
                                )
                            },
                            actions = {
                            },
                            backgroundColor = colorResource(id = R.color.purple_200),
                            contentColor = Color.White,
                            elevation = 12.dp)
                    },
                    content = {
                        Surface(
                            color = MaterialTheme.colors.background
                        ) {
                            MainScreen(moSalesOrder)

                        }
                    }
                )
            }
        }
    }

}

@Composable
fun MainScreen(moSalesOrders: MutableList<SalesOrder>) {
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    Column {
        DisplaySalesOrders(moSalesOrders) {}
    }
}



@Composable
fun DisplaySalesOrders(moSalesOrders: MutableList<SalesOrder>, selectedSalesOrder: (SalesOrder) -> Unit) {
    var salesController: SalesController? = null
    var mSalesOrder: MutableList<SalesOrder> = ArrayList()
    val column1Weight = .2f // 30%
    val column2Weight = .4f // 30%
    val column3Weight = .3f // 70%
    val column4Weight = .1f // 70%
    mSalesOrder = moSalesOrders

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {

        item {
            Row(Modifier.background(Color.Gray)) {
                TableCell(text = "Date", weight = column1Weight)
                TableCell(text = "Customer", weight = column2Weight)
                TableCell(text = "Item", weight = column3Weight)
                TableCell(text = "Qty", weight = column4Weight)
            }
        }
        // Here are all the lines of your table.
        items(mSalesOrder) {
            //val (id, text) = it
            Row(Modifier.fillMaxWidth()) {
                TableCell(text = it.order_date, weight = column1Weight)
                TableCell(text = it.customer, weight = column2Weight)
                TableCell(text = it.item_code, weight = column3Weight)
                TableCell(text = it.qty_outstanding.toString(), weight = column4Weight)
            }
        }
    }
}



@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float
) {
    Text(
        modifier = Modifier
            .border(1.dp, Color.Black)
            .weight(weight)
            .padding(8.dp),
        text = text,
        style = TextStyle(
            fontSize = 8.sp
        )
    )
}

