package com.kachundena.ktenes

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.kachundena.ktenes.api.kTenesApi
import com.kachundena.ktenes.controller.ItemController
import com.kachundena.ktenes.model.Item
import com.kachundena.ktenes.ui.theme.KTenesTheme


class ItemList : ComponentActivity() {
    private var mItems: MutableList<Item> = ArrayList()
    private var moItems = mutableListOf<Item>()
    private var itemController: ItemController? = null
    private val iconAndTextColor: Color = Color.LightGray

    companion object {
        var barcode = ""
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        itemController = ItemController(this)
        moItems.clear()
        moItems = (itemController?.getItems() as ArrayList<Item>)
        setContent {
            KTenesTheme {
                Scaffold(
                    topBar = {
                        val listMenuItems = getMenuItemsList()

                        // state of the menu
                        var expanded by remember {
                            mutableStateOf(false)
                        }
                        TopAppBar(
                            title = {
                                Text(
                                    text = "Items",
                                    color = Color.White
                                )
                            },
                            actions = {
                                IconButton(onClick = {
                                    expanded = true
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = "Open Options"
                                    )
                                }

                                // drop down menu
                                DropdownMenu(
                                    modifier = Modifier.width(width = 150.dp),
                                    expanded = expanded,
                                    onDismissRequest = {
                                        expanded = false
                                    },
                                    // adjust the position
                                    offset = DpOffset(x = (-102).dp, y = (-64).dp),
                                    properties = PopupProperties()
                                ) {

                                    // adding each menu item
                                    listMenuItems.forEach { menuItemData ->
                                        DropdownMenuItem(
                                            onClick = {
                                                expanded = false
                                                if (menuItemData.text == "Exit") {
                                                    finish()
                                                }
                                                if (menuItemData.text == "About") {
                                                    about()
                                                }


                                            },
                                            enabled = true
                                        ) {

                                            Icon(
                                                imageVector = menuItemData.icon,
                                                contentDescription = menuItemData.text,
                                                tint = iconAndTextColor
                                            )

                                            Spacer(modifier = Modifier.width(width = 8.dp))

                                            Text(
                                                text = menuItemData.text,
                                                fontWeight = FontWeight.Medium,
                                                fontSize = 16.sp,
                                                color = iconAndTextColor
                                            )
                                        }
                                    }
                                }
                            },
                            backgroundColor = colorResource(id = R.color.purple_200),
                            contentColor = Color.White,
                            elevation = 12.dp)
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                val intent = Intent(this, ItemDetail::class.java)
                                intent.putExtra("action", "INSERT")
                                intent.putExtra("item_id", "0")
                                this.startActivity(intent)
                            },
                            backgroundColor = Color.Red,
                            content = {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "image",
                                    tint = Color.White
                                )
                            }
                        )
                    },
                    content = {
                        Surface(
                            //modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colors.background
                        ) {
                            MainScreen(moItems)

                        }
                    }
                )
            }
        }
    }

    fun about() {
        val intent = Intent(this, About::class.java)
        this.startActivity(intent)

    }
}

@Composable
fun MainScreen(moItems: MutableList<Item>) {
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    if (ItemList.barcode.toString() != "") {
        textState.value = TextFieldValue(ItemList.barcode.toString())
        ItemList.barcode = ""
    }
    Column {
        SearchView(textState)
        DisplayItems(moItems, textState) {}
    }
}


@Composable
fun SearchView(state: MutableState<TextFieldValue>) {
    val context = LocalContext.current
    Row(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = state.value,
            onValueChange = { value ->
                state.value = value
            },
            modifier = Modifier
                .padding(2.dp),
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 18.sp),
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "",
                    modifier = Modifier
                        .padding(2.dp)
                        .size(24.dp)
                )
            },
            trailingIcon = {
                if (state.value != TextFieldValue("")) {
                    IconButton(
                        onClick = {
                            state.value =
                                TextFieldValue("") // Remove text from TextField when you press the 'X' icon
                        }
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "",
                            modifier = Modifier
                                .padding(2.dp)
                                .size(24.dp)
                        )
                    }
                }
            },
            singleLine = true,
            shape = RectangleShape, // The TextFiled has rounded corners top left and right by default
        )
        Button(
            modifier = Modifier.padding(2.dp),
            onClick = {
            val intent = Intent(context, BarCode::class.java)
            intent.putExtra("origen", "MainActivity")
            context.startActivity(intent)
        }
        ) {
            Image(
                painterResource(id = R.drawable.barcode),
                contentDescription ="Read Barcode",
                modifier = Modifier
                    .size(40.dp)
                    .padding(2.dp))
        }
    }

}


@Composable
fun DisplayItems(moItems: MutableList<Item>, filter: MutableState<TextFieldValue>, selectedItem: (Item) -> Unit) {
    var itemController: ItemController? = null
    var mItems: MutableList<Item> = ArrayList()

    if (filter.value.text != "") {
        itemController = ItemController(LocalContext.current)
        mItems.clear()
        mItems = (itemController?.getItems(filter.value.text) as ArrayList<Item>)
    }
    else {
        mItems = moItems
    }

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(mItems) { item ->
                ItemListItem(item = item, selectedItem)
            }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ItemListItem(item: Item, selectedItem: (Item) -> Unit) {
    var enableState by remember {mutableStateOf<Boolean>(true)}
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth()
            .clickable(onClick = {
                //Toast.makeText(context, "Thanks for clicking!", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, ItemDetail::class.java)
                intent.putExtra("action", "UPDATE")
                intent.putExtra("item_id", item.item_id.toString())
                context.startActivity(intent)
            }),
        elevation = 2.dp,
        shape = RoundedCornerShape(corner = CornerSize(3.dp))
    ) {
        Row(
            modifier = Modifier
                .padding(1.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = item.gtin,
                    style = MaterialTheme.typography.body1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = item.description!!, style = MaterialTheme.typography.body2)
                }
            }
        }
    }
}


fun getMenuItemsList(): ArrayList<MenuItemData> {
    val listItems = ArrayList<MenuItemData>()

    listItems.add(MenuItemData(text = "Exit", icon = Icons.Default.ExitToApp))
    listItems.add(MenuItemData(text = "About", icon = Icons.Outlined.Info))

    return listItems
}

data class MenuItemData(val text: String, val icon: ImageVector)

