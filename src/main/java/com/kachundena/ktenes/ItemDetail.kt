package com.kachundena.ktenes

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.*
import coil.compose.rememberImagePainter
import com.kachundena.ktenes.controller.ItemController
import com.kachundena.ktenes.model.Item
import com.kachundena.ktenes.ui.theme.KTenesTheme
import java.util.*

class ItemDetail : ComponentActivity() {
    private var item: Item? = null
    private var itemController: ItemController? = null

    companion object {
        var barcode = ""
        var szimage = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        val intent: Intent = getIntent()
        val item_id: Int = intent.getStringExtra("item_id")!!.toInt()
        val action: String = intent.getStringExtra("action")!!.toString()
        var readonly = false
        if (action != null) {
            if (intent.getStringExtra("action")!!.toString() == "INSERT") {
                item = Item( item_id,"", "","", "","","", "", "", "", 0.0,0.0,0.0,"", 0,"")
            } else {
                if (item_id != null) {
                    itemController = ItemController(this)
                    item = itemController?.getItem(item_id)
                    if (item!!.readonly == 1) {
                        readonly = true
                    }
                }
            }

            setContent {
                KTenesTheme {
                    Surface() {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.Start,
                        ) {
                            TopAppBar(
                                title = {
                                    Text(text = "Detalle de Items")
                                }
                            )
                            ObjectContent(item!!, action!!, readonly)
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalUnitApi::class)
@Composable
fun ObjectContent(item: Item?, action: String?, readonly: Boolean) {
    val context = LocalContext.current
    val activity = (LocalContext.current as? Activity)
    val itemController: ItemController?
    val mExpanded by remember { mutableStateOf(false) }
    itemController = ItemController(context)
    val _item: Item?

    val icon = if (mExpanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    if (action == "INSERT") {
        _item = Item(0,"", "", "", "","","", "", "", "", 0.0, 0.0,0.0,"", 0,"")
    } else {
        _item = item
    }

    if (_item != null) {
        var item_id by rememberSaveable { mutableStateOf(_item!!.item_id) }
        var gtin by rememberSaveable { mutableStateOf(_item!!.gtin) }
        var code by rememberSaveable { mutableStateOf(_item!!.code) }
        var vendor_code by rememberSaveable { mutableStateOf(_item!!.vendor_code!!) }
        var vendor_no by rememberSaveable { mutableStateOf(_item!!.vendor_no!!) }
        var vendor_name by rememberSaveable { mutableStateOf(_item!!.vendor_name!!) }
        var description by rememberSaveable { mutableStateOf(_item!!.description) }
        var category by rememberSaveable { mutableStateOf(_item!!.category) }
        var um by rememberSaveable { mutableStateOf(_item!!.um) }
        var location by rememberSaveable { mutableStateOf(_item!!.location) }
        var price by rememberSaveable { mutableStateOf(_item!!.price.toString()) }
        var cost by rememberSaveable { mutableStateOf(_item!!.cost.toString()) }
        var stock by rememberSaveable { mutableStateOf(_item!!.stock.toString()) }
        var image by rememberSaveable { mutableStateOf(_item!!.image) }
        var mreadonly by rememberSaveable { mutableStateOf(_item!!.readonly.toString()) }
        var status by rememberSaveable { mutableStateOf(_item!!.status) }
        if (ItemDetail.barcode.toString() != "") {
            gtin = ItemDetail.barcode.toString()
            ItemDetail.barcode = ""
        }
        if (ItemDetail.szimage.toString() != "") {
            image = ItemDetail.szimage.toString()
            ItemDetail.szimage = ""
        }
        Column() {
            Row(modifier = Modifier.fillMaxWidth()) {

                TextField(
                    value = gtin,
                    modifier = Modifier
                        .padding(1.dp),
                    label = { Text(text = "GTIN") },
                    onValueChange = {
                        gtin = it
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                    )
                )
                Button(
                    onClick = {
                        val intent = Intent(context, BarCode::class.java)
                        intent.putExtra("origen", "ItemDetail")
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .padding(1.dp),
                ) {
                    Image(
                        painterResource(id = R.drawable.barcode),
                        contentDescription = "Read Barcode",
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            Row() {
                TextField(
                    value = code,
                    modifier = Modifier
                        .padding(1.dp)
                        .weight(0.5F),
                    label = { Text(text = "Code") },
                    onValueChange = {
                        code = it
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                    ),
                    readOnly = readonly
                )
                TextField(
                    value = status,
                    modifier = Modifier
                        .padding(1.dp)
                        .weight(0.5F),
                    label = { Text(text = "Status") },
                    onValueChange = {
                        status = it
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                    ),
                    readOnly = readonly
                )
            }
            Row() {
                TextField(
                    value = vendor_name,
                    modifier = Modifier
                        .padding(1.dp)
                        .weight(0.5F),
                    label = { Text(text = "Vendor Name") },
                    onValueChange = {
                        vendor_name = it
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                    ),
                    readOnly = readonly
                )
                TextField(
                    value = vendor_code,
                    modifier = Modifier
                        .padding(1.dp)
                        .weight(0.5F),
                    label = { Text(text = "Vendor Code") },
                    onValueChange = {
                        vendor_code = it
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                    ),
                    readOnly = readonly
                )

            }
            description.let {
                TextField(
                    value = it,
                    modifier = Modifier
                        .padding(1.dp)
                        .fillMaxWidth(),
                    label = { Text(text = "Description") },
                    onValueChange = {
                        description = it
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                    ),
                    readOnly = readonly
                )
            }
            TextField(
                value = category,
                modifier = Modifier
                    .padding(1.dp)
                    .fillMaxWidth(),
                label = { Text(text = "Category") },
                onValueChange = {
                    category = it
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                ),
                readOnly = readonly
            )

            Row() {
                TextField(
                    value = location,
                    modifier = Modifier
                        .padding(1.dp)
                        .weight(0.34f),
                    label = { Text(text = "Location") },
                    onValueChange = {
                        location = it
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                    ),
                    readOnly = readonly
                )

                TextField(
                    value = stock,
                    modifier = Modifier
                        .padding(1.dp)
                        .weight(0.33f),
                    label = { Text(text = "Stock") },
                    onValueChange = {
                        stock = it
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next,
                    ),
                    readOnly = readonly
                )
                TextField(
                    value = um,
                    modifier = Modifier
                        .padding(1.dp)
                        .weight(0.33f),
                    label = { Text(text = "U.M.") },
                    onValueChange = {
                        um = it
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                    ),
                    readOnly = readonly
                )
            }
            Row() {
                TextField(
                    value = price,
                    modifier = Modifier
                        .padding(1.dp)
                        .weight(0.5F),
                    label = { Text(text = "Price") },
                    onValueChange = {
                        price = it
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next,
                    ),
                    readOnly = readonly
                )
                Button(
                    onClick = {
                        val intent = Intent(context, ItemSalesList::class.java)
                        intent.putExtra("item_code", code.toString())
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green)
                ) {
                    Image(
                        painterResource(id = R.drawable.ic_sales),
                        contentDescription = "Pending Shipping",
                        modifier = Modifier.size(36.dp)
                    )
                }
                TextField(
                    value = cost,
                    modifier = Modifier
                        .padding(1.dp)
                        .weight(0.5F),
                    label = { Text(text = "Cost") },
                    onValueChange = {
                        cost = it
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next,
                    ),
                    readOnly = readonly
                )
                Button(
                    onClick = {
                        val intent = Intent(context, ItemPurchaseList::class.java)
                        intent.putExtra("item_code", code.toString())
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                ) {
                    Image(
                        painterResource(id = R.drawable.ic_purchase),
                        contentDescription = "Pending Receives",
                        modifier = Modifier.size(36.dp)
                    )
                }

            }
        }
        Row() {
            Button(
                onClick = {
                    if (gtin.toString() != "") {
                        val _item: Item =
                            Item(item_id, gtin.toString(), code, vendor_code, vendor_no, vendor_name, description, category, um, location, price.toDouble(), cost.toDouble(), stock.toDouble(), image, mreadonly.toInt(), status)
                        if (action == "UPDATE") {
                            itemController?.updateItem(_item)
                        } else {
                            if (action == "INSERT") {
                                itemController?.newItem(_item)
                            }
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

@Composable
fun StringFieldOutlined(value: String, label: String, readonly: Boolean, fillmaxwidth: Boolean, format: String) {
    var _value by rememberSaveable { mutableStateOf(value) }
    var _modifier: Modifier = Modifier
    _modifier.padding(1.dp)
    if (fillmaxwidth) {
        _modifier.fillMaxWidth()
    }
    var _keyboardoptions: KeyboardOptions = KeyboardOptions()
    if (format.equals("string")) {
        _keyboardoptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
        )
    }
    else if (format.equals("number")) {
        _keyboardoptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next,
        )
    }
    OutlinedTextField(
        value = _value,
        modifier = _modifier,
        label = { Text(text = label) },
        onValueChange = {
            _value = it
        },
        singleLine = true,
        keyboardOptions = _keyboardoptions,
        readOnly = readonly
    )
}

@Composable
fun DatePicker(label: String){

    val mContext = LocalContext.current

    val mYear: Int
    val mMonth: Int
    val mDay: Int

    val mCalendar = Calendar.getInstance()

    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    mCalendar.time = Date()

    val mDate = remember { mutableStateOf("") }

    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            mDate.value = "$mDayOfMonth/${mMonth+1}/$mYear"
        }, mYear, mMonth, mDay
    )

    Row() {
        OutlinedTextField(
            value = mDate.value,
            modifier = Modifier
                .padding(1.dp)
                .height(60.dp),
            label = { Text(text = label) },
            onValueChange = {
                mDate.value = it
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
            )
        )
        Button(
            onClick = {
                mDatePickerDialog.show()
            },
            modifier = Modifier
                .padding(10.dp,10.dp,0.dp,0.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0XFF0F9D58)) ) {
            Image(
                painterResource(id = R.drawable.ic_baseline_date_range_24),
                contentDescription = "Calendar",
                modifier = Modifier.size(36.dp)
            )
        }
    }
}

