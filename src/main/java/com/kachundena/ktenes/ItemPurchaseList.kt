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
import com.kachundena.ktenes.controller.PurchaseController
import com.kachundena.ktenes.controller.SalesController
import com.kachundena.ktenes.model.Item
import com.kachundena.ktenes.model.PurchaseOrder
import com.kachundena.ktenes.ui.theme.KTenesTheme


class ItemPurchaseList : ComponentActivity() {
    private var moPurchaseOrder = mutableListOf<PurchaseOrder>()
    private var purchaseController: PurchaseController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        val intent: Intent = getIntent()
        val item_code: String = intent.getStringExtra("item_code")!!.toString()
        purchaseController = PurchaseController(this)
        moPurchaseOrder.clear()
        moPurchaseOrder = (purchaseController?.getItemPurchaseOrders(item_code) as ArrayList<PurchaseOrder>)
        setContent {
            KTenesTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "Purchase orders",
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
                            MainScreenIP(moPurchaseOrder)

                        }
                    }
                )
            }
        }
    }

}

@Composable
fun MainScreenIP(moPurchaseOrders: MutableList<PurchaseOrder>) {
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    Column {
        DisplayPurchaseOrdersIP(moPurchaseOrders) {}
    }
}



@Composable
fun DisplayPurchaseOrdersIP(moPurchaseOrders: MutableList<PurchaseOrder>, selectedPurchaseOrder: (PurchaseOrder) -> Unit) {
    var mPurchaseOrder: MutableList<PurchaseOrder> = ArrayList()
    val column1Weight = .2f // 30%
    val column2Weight = .4f // 30%
    val column3Weight = .2f // 70%
    val column4Weight = .2f // 70%
    mPurchaseOrder = moPurchaseOrders

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {

        item {
            Row(Modifier.background(Color.Gray)) {
                TableCell(text = "Order", weight = column1Weight)
                TableCell(text = "Vendor", weight = column2Weight)
                TableCell(text = "Qty", weight = column3Weight)
                TableCell(text = "Pend", weight = column4Weight)
            }
        }
        // Here are all the lines of your table.
        items(mPurchaseOrder) {
            Row(Modifier.fillMaxWidth()) {
                TableCell(text = it.purchase_order, weight = column1Weight)
                TableCell(text = it.vendor, weight = column2Weight)
                TableCell(text = it.quantity.toString(), weight = column3Weight)
                TableCell(text = it.qty_outstanding.toString(), weight = column4Weight)
            }
        }
    }
}

