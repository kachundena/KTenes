package com.kachundena.ktenes.controller

import android.content.ContentValues
import android.content.Context
import com.kachundena.ktenes.database.dbHelper
import com.kachundena.ktenes.model.Item
import com.kachundena.ktenes.model.SalesOrder
import java.text.ParseException
import java.text.SimpleDateFormat

class SalesController  (context: Context){
    private var DBHelper: dbHelper
    private val TABLE_NAME_SALESORDER = "sales_order"
    var dateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")

    init {
        // Create new helper
        DBHelper = dbHelper(context)
    }


    fun deleteSalesOrder(salesOrder: SalesOrder): Int {
        val DB = DBHelper!!.writableDatabase
        val argumentos = arrayOf<String>(salesOrder.sales_order, salesOrder.line.toString())
        return DB.delete(TABLE_NAME_SALESORDER, "sales_order = ? and line = ?", argumentos)
    }

    fun deleteAllSalesOrder(): Int {
        val DB = DBHelper!!.writableDatabase
        return DB.delete(TABLE_NAME_SALESORDER, null, null)
    }

    fun setStatus1ToSalesOrder(): Int {
        val DB = DBHelper!!.writableDatabase
        val UpdateValues = ContentValues()
        UpdateValues.put("status", 1)
        return DB.update(TABLE_NAME_SALESORDER, UpdateValues, null, null)
    }

    fun deleteSalesOrderStatus1(): Int {
        val DB = DBHelper!!.writableDatabase
        val argumentos = arrayOf<String>("1")
        return DB.delete(TABLE_NAME_SALESORDER, "status = ?", argumentos)
    }

    fun newSalesOrder(salesOrder: SalesOrder): Long {
        val DB = DBHelper!!.writableDatabase
        val InsertValues = ContentValues()
        InsertValues.put("sales_order", salesOrder.sales_order)
        InsertValues.put("line", salesOrder.line)
        InsertValues.put("external_doc", salesOrder.external_doc)
        InsertValues.put("customer_no", salesOrder.customer_no)
        InsertValues.put("customer", salesOrder.customer)
        InsertValues.put("item_code", salesOrder.item_code)
        InsertValues.put("quantity", salesOrder.quantity)
        InsertValues.put("qty_outstanding", salesOrder.qty_outstanding)
        InsertValues.put("order_date", salesOrder.order_date)
        InsertValues.put("prev_send_date", salesOrder.prev_send_date)
        InsertValues.put("status", 0)
        return DB.insert(TABLE_NAME_SALESORDER, null, InsertValues)
    }

    fun updateSalesOrder(salesOrder: SalesOrder): Int {
        val DB = DBHelper!!.writableDatabase
        val UpdateValues = ContentValues()
        UpdateValues.put("sales_order", salesOrder.sales_order)
        UpdateValues.put("line", salesOrder.line)
        UpdateValues.put("external_doc", salesOrder.external_doc)
        UpdateValues.put("customer_no", salesOrder.customer_no)
        UpdateValues.put("customer", salesOrder.customer)
        UpdateValues.put("item_code", salesOrder.item_code)
        UpdateValues.put("quantity", salesOrder.quantity)
        UpdateValues.put("qty_outstanding", salesOrder.qty_outstanding)
        UpdateValues.put("order_date", salesOrder.order_date)
        UpdateValues.put("prev_send_date", salesOrder.prev_send_date)
        UpdateValues.put("status", 0)
        val PKField = "sales_order = ? and line = ?"
        val ValuesPKField = arrayOf<String>(salesOrder.sales_order, salesOrder.line.toString())
        return DB.update(TABLE_NAME_SALESORDER, UpdateValues, PKField, ValuesPKField)
    }
    
    fun updateinsertSalesOrder(salesOrder: SalesOrder): Int {
        //var _item: Item? = null
        val DB = DBHelper!!.readableDatabase
        val QueryCols = arrayOf("sales_order","line")
        val found: Boolean = false
        val cursor = DB.query(
            TABLE_NAME_SALESORDER,
            QueryCols,
            "sales_order= ? and line = ?", arrayOf(salesOrder.sales_order, salesOrder.line.toString()),
            null,
            null,
            null
        ) ?: return -1
        if (!cursor.moveToFirst()) {
        	// inssert
            val DBi = DBHelper!!.writableDatabase
            val InsertValues = ContentValues()
            InsertValues.put("sales_order", salesOrder.sales_order)
            InsertValues.put("line", salesOrder.line)
            InsertValues.put("external_doc", salesOrder.external_doc)
            InsertValues.put("customer_no", salesOrder.customer_no)
            InsertValues.put("customer", salesOrder.customer)
            InsertValues.put("item_code", salesOrder.item_code)
            InsertValues.put("quantity", salesOrder.quantity)
            InsertValues.put("qty_outstanding", salesOrder.qty_outstanding)
            InsertValues.put("order_date", salesOrder.order_date)
            InsertValues.put("prev_send_date", salesOrder.prev_send_date)
            InsertValues.put("status", 0)
            DBi.insert(TABLE_NAME_SALESORDER, null, InsertValues)
            return 0
        
        }
        else {
        	// update
            val DBu = DBHelper!!.writableDatabase
            val UpdateValues = ContentValues()
            UpdateValues.put("sales_order", salesOrder.sales_order)
            UpdateValues.put("line", salesOrder.line)
            UpdateValues.put("external_doc", salesOrder.external_doc)
            UpdateValues.put("customer_no", salesOrder.customer_no)
            UpdateValues.put("customer", salesOrder.customer)
            UpdateValues.put("item_code", salesOrder.item_code)
            UpdateValues.put("quantity", salesOrder.quantity)
            UpdateValues.put("qty_outstanding", salesOrder.qty_outstanding)
            UpdateValues.put("order_date", salesOrder.order_date)
            UpdateValues.put("prev_send_date", salesOrder.prev_send_date)
            UpdateValues.put("status", 0)
            val PKField = "sales_order = ? and line = ?"
            val ValuesPKField = arrayOf<String>(salesOrder.sales_order, salesOrder.line.toString())
            DBu.update(TABLE_NAME_SALESORDER, UpdateValues, PKField, ValuesPKField)
            return 0
        }    	
    }

    @Throws(ParseException::class)
    fun getSalesOrder(sales_order: String, line: Int): SalesOrder? {
        var salesOrder: SalesOrder? = null
        val DB = DBHelper!!.readableDatabase
        val QueryCols = arrayOf("sales_order", "line", "external_doc", "customer_no",
            "customer", "item_code", "quantity", "qty_outstanding", "order_date",
            "prev_send_date", "status")
        val cursor = DB.query(
            TABLE_NAME_SALESORDER,
            QueryCols,
            "sales_order=? and line=?", arrayOf(sales_order, line.toString()),
            null,
            null,
            null
        ) ?: return salesOrder
        if (!cursor.moveToFirst()) return salesOrder
        do {
            salesOrder = SalesOrder(
                cursor.getString(0),
                cursor.getInt(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getDouble(6),
                cursor.getDouble(7),
                cursor.getString(8),
                cursor.getString(9),
                cursor.getInt(10)
            )
        } while (cursor.moveToNext())
        cursor.close()
        return salesOrder
    }

    @Throws(ParseException::class)
    fun getItemSalesOrders(item_no: String): ArrayList<SalesOrder?>? {
        val salesOrders: ArrayList<SalesOrder?> = ArrayList()
        val DB = DBHelper!!.readableDatabase
        val QueryCols = arrayOf("sales_order", "line", "external_doc", "customer_no",
            "customer", "item_code", "quantity", "qty_outstanding", "order_date",
            "prev_send_date", "status")
        val cursor = DB.query(
            TABLE_NAME_SALESORDER,
            QueryCols,
            "item_code=?", arrayOf(item_no),
            null,
            null,
            null
        ) ?: return salesOrders
        if (!cursor.moveToFirst()) return salesOrders
        do {
            val salesOrder = SalesOrder(
                cursor.getString(0),
                cursor.getInt(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getDouble(6),
                cursor.getDouble(7),
                cursor.getString(8),
                cursor.getString(9),
                cursor.getInt(10)
            )
            salesOrders.add(salesOrder)
        } while (cursor.moveToNext())
        cursor.close()
        return salesOrders
    }

    @Throws(ParseException::class)
    fun getSalesOrders(): ArrayList<SalesOrder?>? {
        val salesOrders: ArrayList<SalesOrder?> = ArrayList()
        val DB = DBHelper!!.readableDatabase
        val QueryCols = arrayOf("sales_order", "line", "external_doc", "customer_no",
            "customer", "item_code", "quantity", "qty_outstanding", "order_date",
            "prev_send_date", "status")
        val cursor = DB.query(
            TABLE_NAME_SALESORDER,
            QueryCols,
            null,
            null,
            null,
            null,
            null
        ) ?: return salesOrders
        if (!cursor.moveToFirst()) return salesOrders
        do {
            val salesOrder = SalesOrder(
                cursor.getString(0),
                cursor.getInt(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getDouble(6),
                cursor.getDouble(7),
                cursor.getString(8),
                cursor.getString(9),
                cursor.getInt(10)
            )
            salesOrders.add(salesOrder)
        } while (cursor.moveToNext())
        cursor.close()
        return salesOrders
    }

}