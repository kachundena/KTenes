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


class ItemSalesList : ComponentActivity() {
    private var moSalesOrder = mutableListOf<SalesOrder>()
    private var salesController: SalesController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        val intent: Intent = getIntent()
        val item_code: String = intent.getStringExtra("item_code")!!.toString()
        salesController = SalesController(this)
        moSalesOrder.clear()
        moSalesOrder = (salesController?.getItemSalesOrders(item_code) as ArrayList<SalesOrder>)
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
                            MainScreenIS(moSalesOrder)

                        }
                    }
                )
            }
        }
    }

}

@Composable
fun MainScreenIS(moSalesOrders: MutableList<SalesOrder>) {
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    Column {
        DisplaySalesOrdersIS(moSalesOrders) {}
    }
}



@Composable
fun DisplaySalesOrdersIS(moSalesOrders: MutableList<SalesOrder>, selectedSalesOrder: (SalesOrder) -> Unit) {
    var mSalesOrder: MutableList<SalesOrder> = ArrayList()
    val column1Weight = .2f // 30%
    val column2Weight = .4f // 30%
    val column3Weight = .2f // 70%
    val column4Weight = .2f // 70%
    mSalesOrder = moSalesOrders



    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {

        item {
            Row(Modifier.background(Color.Gray)) {
                TableCell(text = "Order", weight = column1Weight)
                TableCell(text = "Customer", weight = column2Weight)
                TableCell(text = "Qty", weight = column3Weight)
                TableCell(text = "Pend", weight = column4Weight)
            }
        }
        // Here are all the lines of your table.
        items(mSalesOrder) {
            Row(Modifier.fillMaxWidth()) {
                TableCell(text = it.sales_order, weight = column1Weight)
                TableCell(text = it.customer, weight = column2Weight)
                TableCell(text = it.quantity.toString(), weight = column3Weight)
                TableCell(text = it.qty_outstanding.toString(), weight = column4Weight)
            }
        }
    }
}



