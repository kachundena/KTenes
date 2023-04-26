package com.kachundena.ktenes

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.kachundena.ktenes.api.kTenesApi
import com.kachundena.ktenes.api.RetrofitClient
import com.kachundena.ktenes.controller.ItemController
import com.kachundena.ktenes.controller.ParameterController
import com.kachundena.ktenes.controller.PurchaseController
import com.kachundena.ktenes.controller.SalesController
import com.kachundena.ktenes.model.Item
import com.kachundena.ktenes.model.LastTS
import com.kachundena.ktenes.model.PurchaseOrder
import com.kachundena.ktenes.model.SalesOrder
import com.kachundena.ktenes.ui.theme.KTenesTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileWriter
import java.net.URL

class MainActivity : ComponentActivity() {
    private var mItems: MutableList<Item> = ArrayList()
    private var itemController: ItemController? = null
    private var salesController: SalesController? = null
    private var purchaseController: PurchaseController? = null
    private var parameterController: ParameterController? = null
    private val iconAndTextColor: Color = Color.LightGray
    private var tienePermisoAlmacenamiento = false
    private val CODIGO_PERMISOS_ALMACENAMIENTO = 2
    var ktenesApi: kTenesApi? = null
    lateinit var progressDialog: ProgressDialog

    companion object {
        var lastsync_item = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parameterController = ParameterController(this)
        lastsync_item = parameterController!!.getLastSyncItem().toString()
        initializerApi()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Loading ...")
        progressDialog.setCancelable(false) // blocks UI interaction
        progressDialog.show()
        if (lastsync_item == "null") {
            callGetLastTS()
            callGetItems()
            progressDialog.hide()
        }
        else {
            callGetLastTS()
            if (lastsync_item != null) {
                callGetLastItems(lastsync_item)
            }
            progressDialog.hide()
        }


        setContent {
            KTenesTheme {
                Scaffold(
                    topBar = {
                        val listMenuItems = getMenuMainList()

                        // state of the menu
                        var expanded by remember {
                            mutableStateOf(false)
                        }
                        TopAppBar(
                            title = {
                                Text(
                                    text = "KITems - Items Manager",
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
                                    listMenuItems.forEach { MenuMain ->
                                        DropdownMenuItem(
                                            onClick = {
                                                expanded = false
                                                if (MenuMain.text == "Export") {
                                                    Toast.makeText(
                                                        this@MainActivity,
                                                        "Exportar",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    exportDataToJson()
                                                }
                                                if (MenuMain.text == "Import") {
                                                    Toast.makeText(
                                                        this@MainActivity,
                                                        "Importar",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    importDataToJson()
                                                }
                                                if (MenuMain.text == "About") {
                                                    about()
                                                }
                                                if (MenuMain.text == "Settings") {
                                                    settings()
                                                }
                                                if (MenuMain.text == "Sync") {
                                                    initializerApi()
                                                    //callGetItems()
                                                    salesController?.setStatus1ToSalesOrder()
                                                    purchaseController?.setStatus1ToPurchaseOrder()
                                                    callGetSales()
                                                    callGetPurchase()
                                                    salesController?.deleteSalesOrderStatus1()
                                                    purchaseController?.deletePurchaseOrderStatus1()
                                                }


                                            },
                                            enabled = true
                                        ) {

                                            Icon(
                                                imageVector = MenuMain.icon,
                                                contentDescription = MenuMain.text,
                                                tint = iconAndTextColor
                                            )

                                            Spacer(modifier = Modifier.width(width = 8.dp))

                                            Text(
                                                text = MenuMain.text,
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
                    content = {
                        Surface(
                            //modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colors.background
                        ) {
                            Column(modifier = Modifier
                                .padding(5.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center) {
                                Button(
                                    onClick = {
                                        items()

                                    },
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .fillMaxWidth(),
                                ) {
                                    Column() {
                                        Image(
                                            painterResource(id = R.drawable.ic_table),
                                            contentDescription = "Producto",
                                            modifier = Modifier
                                                .size(96.dp)
                                                .fillMaxWidth()
                                        )
                                        Text(
                                            text = "PRODUCTOS",
                                            style = TextStyle(fontWeight = FontWeight.Bold),
                                            textAlign = TextAlign.Center,
                                            fontSize = 20.sp
                                        )
                                    }
                                }
                                Button(
                                    onClick = {
                                        purchase()

                                    },
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .fillMaxWidth(),
                                ) {
                                    Column() {
                                        Image(
                                            painterResource(id = R.drawable.ic_purchase),
                                            contentDescription = "Outstanding Purchases",
                                            modifier = Modifier
                                                .size(96.dp)
                                                .fillMaxWidth()
                                        )
                                        Text(
                                            text = "COMPRAS",
                                            style = TextStyle(fontWeight = FontWeight.Bold),
                                            textAlign = TextAlign.Center,
                                            fontSize = 20.sp
                                        )
                                    }
                                }
                                Button(
                                    onClick = {
                                        sales()

                                    },
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .fillMaxWidth(),
                                ) {
                                    Column() {
                                        Image(
                                            painterResource(id = R.drawable.ic_sales),
                                            contentDescription = "Outstanding Sales",
                                            modifier = Modifier
                                                .size(96.dp)
                                                .fillMaxWidth()
                                        )
                                        Text(text = "VENTAS",
                                            style = TextStyle(fontWeight = FontWeight.Bold),
                                            textAlign = TextAlign.Center,
                                            fontSize = 20.sp)
                                    }
                                }

                            }
                        }
                    }
                )
            }
        }
    }

    fun exportDataToJson() {
        var mjItem: MutableList<Item> = ArrayList()
        var file: File?=null
        itemController = ItemController(this)

        verificarYPedirPermisosDeAlmacenamiento();

        mItems = (itemController?.getItems() as ArrayList<Item>)


        //var gson = Gson()
        var gson = GsonBuilder()
            .setDateFormat("dd/MM/yyyy").create()

        var jItem:String = gson.toJson(mItems)

        val szDirFile: String = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)!!.absolutePath
        exportJsonToFile(jItem, szDirFile, "/KItems.json")
    }

    fun exportJsonToFile(pcontent: String, pdirfile: String, pfile: String) {
        val fw = FileWriter(pdirfile + pfile)
        fw.write(pcontent)
        fw.flush()
        fw.close()
    }
    private fun verificarYPedirPermisosDeAlmacenamiento() {
        val estadoDePermiso = ContextCompat.checkSelfPermission(
            this@MainActivity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (estadoDePermiso == PackageManager.PERMISSION_GRANTED) {
            permisoDeAlmacenamientoConcedido()
        } else {
            ActivityCompat.requestPermissions(
                this@MainActivity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                CODIGO_PERMISOS_ALMACENAMIENTO
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CODIGO_PERMISOS_ALMACENAMIENTO -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permisoDeAlmacenamientoConcedido()
            } else {
                permisoDeAlmacenamientoDenegado()
            }
        }
    }

    private fun permisoDeAlmacenamientoConcedido() {
        tienePermisoAlmacenamiento = true
    }

    private fun permisoDeAlmacenamientoDenegado() {
    }


    fun importJsonFromFile(pdirfile: String, pfile: String) : String? {
        var szContent: String = File(pdirfile + pfile).reader().use{it.readText()}
        return szContent
    }
    fun importJsonFromURL(purl: String) : String? {
        var szContent: String = ""
        lifecycleScope.launch {
            val szContent = urlRead(purl)
        }
        return szContent
    }

    private suspend fun urlRead(pUrl: String) = withContext(Dispatchers.IO) {
        val url = URL(pUrl)
        val stream = url.openStream()
        stream.read()
    }

    fun importDataToJson() {
        val szDirFile: String = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)!!.absolutePath
        var mItem: MutableList<Item> = ArrayList()
        itemController = ItemController(this)
        //var gson = Gson()
        var gson = GsonBuilder()
            .setDateFormat("dd/MM/yyyy").create()
        val arrayItemType = object : TypeToken<Array<Item>>() {}.type

        verificarYPedirPermisosDeAlmacenamiento();

        var mjItem: Array<Item> = gson.fromJson(importJsonFromFile(szDirFile, "/KItems.json"), arrayItemType)
        // clean all Items
        itemController?.deleteAllItem()
        // for each mjProject insert Project
        for (item in mjItem) {
            val itemid = itemController?.newItem(item)
        }
    }

    fun about() {
        val intent = Intent(this, About::class.java)
        this.startActivity(intent)

    }
    fun items() {
        val intent = Intent(this, ItemList::class.java)
        this.startActivity(intent)
    }

    fun sales() {
        val intent = Intent(this, SalesList::class.java)
        this.startActivity(intent)
    }

    fun purchase() {
        val intent = Intent(this, PurchaseList::class.java)
        this.startActivity(intent)
    }

    fun settings() {
        val intent = Intent(this, ParameterDetail::class.java)
        this.startActivity(intent)

    }

    private fun initializerApi() {
        ktenesApi = RetrofitClient.retrofit.create(kTenesApi::class.java)
        itemController = ItemController(this)
        salesController = SalesController(this)
        purchaseController = PurchaseController(this)
    }

    private fun callGetItems(){
        val callGetItem = ktenesApi?.getItems()
        callGetItem?.enqueue(object : Callback<List<Item>> {
            override fun onFailure(call: Call<List<Item>>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {
                var itemsList: List<Item>? = response.body()
                // for each mjProject insert Project
                if (itemsList != null) {
                    for (item in itemsList) {
                        val itemid = itemController?.updateinsertItem(item)
                    }
                }
            }
        })
    }

    private fun callGetLastItems(lastTS: String){
        val callLastGetItem = ktenesApi?.getLastItems(lastTS)
        callLastGetItem?.enqueue(object : Callback<List<Item>> {
            override fun onFailure(call: Call<List<Item>>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {
                var itemsList: List<Item>? = response.body()
                // for each mjProject insert Project
                if (itemsList != null) {
                    for (item in itemsList) {
                        val itemid = itemController?.updateinsertItem(item)
                    }
                }
            }
        })
    }

    private fun callGetLastTS(){
        val callGetLatTS = ktenesApi?.getLastTS()
        callGetLatTS?.enqueue(object : Callback<List<LastTS>> {
            override fun onFailure(cal: Call<List<LastTS>>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<LastTS>>, response: Response<List<LastTS>>){
                var lastTS: List<LastTS>? = response.body()
                if (lastTS != null) {
                    val itemid = parameterController?.setLastSyncItem(lastTS[0].ts)
                }
            }
        })
    }
    private fun callGetSales(){
        val callGetSales = ktenesApi?.getSalesOrder()
        callGetSales?.enqueue(object : Callback<List<SalesOrder>> {
            override fun onFailure(call: Call<List<SalesOrder>>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<SalesOrder>>, response: Response<List<SalesOrder>>) {
                var salesOrderList: List<SalesOrder>? = response.body()
                if (salesOrderList != null) {
                    for (salesOrder in salesOrderList) {
                        val salesid = salesController?.updateinsertSalesOrder(salesOrder)
                    }
                }
            }
        })
    }

    private fun callGetPurchase(){
        val callGetPurchase = ktenesApi?.getPurchaseOrder()
        callGetPurchase?.enqueue(object : Callback<List<PurchaseOrder>> {
            override fun onFailure(call: Call<List<PurchaseOrder>>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<PurchaseOrder>>, response: Response<List<PurchaseOrder>>) {
                var purchaseOrderList: List<PurchaseOrder>? = response.body()
                if (purchaseOrderList != null) {
                    for (purchaseOrder in purchaseOrderList) {
                        val purchaseid = purchaseController?.updateinsertPurchaseOrder(purchaseOrder)
                    }
                }
            }
        })
    }


}


fun getMenuMainList(): ArrayList<MenuMain> {
    val listItems = ArrayList<MenuMain>()

    listItems.add(MenuMain(text = "Sync", icon = Icons.Default.Sync))
//    listItems.add(MenuMain(text = "Import", icon = Icons.Default.ArrowCircleDown))
//    listItems.add(MenuMain(text = "Export", icon = Icons.Default.ArrowCircleUp))
    listItems.add(MenuMain(text = "Settings", icon = Icons.Default.Settings))
    listItems.add(MenuMain(text = "About", icon = Icons.Outlined.Info))

    return listItems
}

data class MenuMain(val text: String, val icon: ImageVector)

