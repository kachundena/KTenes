package com.kachundena.ktenes.controller

import android.content.ContentValues
import android.content.Context
import com.kachundena.ktenes.database.dbHelper
import com.kachundena.ktenes.model.PurchaseOrder
import java.text.ParseException
import java.text.SimpleDateFormat

class PurchaseController  (context: Context){
    private var DBHelper: dbHelper
    private val TABLE_NAME_PURCHASEORDER = "purchase_order"
    var dateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")

    init {
        // Create new helper
        DBHelper = dbHelper(context)
    }

    fun deletePurchaseOrder(purchaseOrder: PurchaseOrder): Int {
        val DB = DBHelper!!.writableDatabase
        val argumentos = arrayOf<String>(purchaseOrder.purchase_order, purchaseOrder.line.toString())
        return DB.delete(TABLE_NAME_PURCHASEORDER, "purchase_order = ? and line = ?", argumentos)
    }

    fun deleteAllPurchaseOrder(): Int {
        val DB = DBHelper!!.writableDatabase
        return DB.delete(TABLE_NAME_PURCHASEORDER, null, null)
    }

    fun newPurchaseOrder(purchaseOrder: PurchaseOrder): Long {
        val DB = DBHelper!!.writableDatabase
        val InsertValues = ContentValues()
        InsertValues.put("purchase_order", purchaseOrder.purchase_order)
        InsertValues.put("line", purchaseOrder.line)
        InsertValues.put("vendor_no", purchaseOrder.vendor_no)
        InsertValues.put("vendor", purchaseOrder.vendor)
        InsertValues.put("item_code", purchaseOrder.item_code)
        InsertValues.put("quantity", purchaseOrder.quantity)
        InsertValues.put("qty_outstanding", purchaseOrder.qty_outstanding)
        InsertValues.put("order_date", purchaseOrder.order_date)
        InsertValues.put("prev_receive_date", purchaseOrder.prev_receive_date)
        InsertValues.put("status", 0)
        return DB.insert(TABLE_NAME_PURCHASEORDER, null, InsertValues)
    }

    fun setStatus1ToPurchaseOrder(): Int {
        val DB = DBHelper!!.writableDatabase
        val UpdateValues = ContentValues()
        UpdateValues.put("status", 1)
        return DB.update(TABLE_NAME_PURCHASEORDER, UpdateValues, null, null)
    }

    fun deletePurchaseOrderStatus1(): Int {
        val DB = DBHelper!!.writableDatabase
        val argumentos = arrayOf<String>("1")
        return DB.delete(TABLE_NAME_PURCHASEORDER, "status = ?", argumentos)
    }

    fun updatePurchaseOrder(purchaseOrder: PurchaseOrder): Int {
        val DB = DBHelper!!.writableDatabase
        val UpdateValues = ContentValues()
        UpdateValues.put("purchase_order", purchaseOrder.purchase_order)
        UpdateValues.put("line", purchaseOrder.line)
        UpdateValues.put("vendor_no", purchaseOrder.vendor_no)
        UpdateValues.put("vendor", purchaseOrder.vendor)
        UpdateValues.put("item_code", purchaseOrder.item_code)
        UpdateValues.put("quantity", purchaseOrder.quantity)
        UpdateValues.put("qty_outstanding", purchaseOrder.qty_outstanding)
        UpdateValues.put("order_date", purchaseOrder.order_date)
        UpdateValues.put("prev_receive_date", purchaseOrder.prev_receive_date)
        UpdateValues.put("status", 0)
        val PKField = "purchase_order = ? and line = ?"
        val ValuesPKField = arrayOf<String>(purchaseOrder.purchase_order, purchaseOrder.line.toString())
        return DB.update(TABLE_NAME_PURCHASEORDER, UpdateValues, PKField, ValuesPKField)
    }
    
    fun updateinsertPurchaseOrder(purchaseOrder: PurchaseOrder): Int {
        //var _item: Item? = null
        val DB = DBHelper!!.readableDatabase
        val QueryCols = arrayOf("purchase_order","line")
        val found: Boolean = false
        val cursor = DB.query(
            TABLE_NAME_PURCHASEORDER,
            QueryCols,
            "purchase_order= ? and line = ?", arrayOf(purchaseOrder.purchase_order, purchaseOrder.line.toString()),
            null,
            null,
            null
        ) ?: return -1
        if (!cursor.moveToFirst()) {
        	// inssert
            val DBi = DBHelper!!.writableDatabase
            val InsertValues = ContentValues()
            InsertValues.put("purchase_order", purchaseOrder.purchase_order)
            InsertValues.put("line", purchaseOrder.line)
            InsertValues.put("vendor_no", purchaseOrder.vendor_no)
            InsertValues.put("vendor", purchaseOrder.vendor)
            InsertValues.put("item_code", purchaseOrder.item_code)
            InsertValues.put("quantity", purchaseOrder.quantity)
            InsertValues.put("qty_outstanding", purchaseOrder.qty_outstanding)
            InsertValues.put("order_date", purchaseOrder.order_date)
            InsertValues.put("prev_receive_date", purchaseOrder.prev_receive_date)
            InsertValues.put("status", 0)
            DBi.insert(TABLE_NAME_PURCHASEORDER, null, InsertValues)
            return 0
        
        }
        else {
        	// update
            val DBu = DBHelper!!.writableDatabase
            val UpdateValues = ContentValues()
            UpdateValues.put("purchase_order", purchaseOrder.purchase_order)
            UpdateValues.put("line", purchaseOrder.line)
            UpdateValues.put("vendor_no", purchaseOrder.vendor_no)
            UpdateValues.put("vendor", purchaseOrder.vendor)
            UpdateValues.put("item_code", purchaseOrder.item_code)
            UpdateValues.put("quantity", purchaseOrder.quantity)
            UpdateValues.put("qty_outstanding", purchaseOrder.qty_outstanding)
            UpdateValues.put("order_date", purchaseOrder.order_date)
            UpdateValues.put("prev_receive_date", purchaseOrder.prev_receive_date)
            UpdateValues.put("status", 0)
            val PKField = "purchase_order = ? and line = ?"
            val ValuesPKField = arrayOf<String>(purchaseOrder.purchase_order, purchaseOrder.line.toString())
            DBu.update(TABLE_NAME_PURCHASEORDER, UpdateValues, PKField, ValuesPKField)
            return 0
        }    	
    }

    @Throws(ParseException::class)
    fun getPurchaseOrder(purchase_order: String, line: Int): PurchaseOrder? {
        var purchaseOrder: PurchaseOrder? = null
        val DB = DBHelper!!.readableDatabase
        val QueryCols = arrayOf("purchase_order", "line", "vendor_no",
            "vendor", "item_code", "quantity", "qty_outstanding", "order_date",
            "prev_receive_date", "status")
        val cursor = DB.query(
            TABLE_NAME_PURCHASEORDER,
            QueryCols,
            "purchase_order=? and line=?", arrayOf(purchase_order, line.toString()),
            null,
            null,
            null
        ) ?: return purchaseOrder
        if (!cursor.moveToFirst()) return purchaseOrder
        do {
            purchaseOrder = PurchaseOrder(
                cursor.getString(0),
                cursor.getInt(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getDouble(5),
                cursor.getDouble(6),
                cursor.getString(7),
                cursor.getString(8),
                cursor.getInt(9)
            )
        } while (cursor.moveToNext())
        cursor.close()
        return purchaseOrder
    }

    @Throws(ParseException::class)
    fun getPurchaseOrders(): ArrayList<PurchaseOrder?>? {
        val purchaseOrders: ArrayList<PurchaseOrder?> = ArrayList()
        val DB = DBHelper!!.readableDatabase
        val QueryCols = arrayOf("purchase_order", "line", "vendor_no",
            "vendor", "item_code", "quantity", "qty_outstanding", "order_date",
            "prev_receive_date", "status")
        val cursor = DB.query(
            TABLE_NAME_PURCHASEORDER,
            QueryCols,
            null,
            null,
            null,
            null,
            null
        ) ?: return purchaseOrders
        if (!cursor.moveToFirst()) return purchaseOrders
        do {
            val purchaseOrder = PurchaseOrder(
                cursor.getString(0),
                cursor.getInt(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getDouble(5),
                cursor.getDouble(6),
                cursor.getString(7),
                cursor.getString(8),
                cursor.getInt(9)
            )
            purchaseOrders.add(purchaseOrder)
        } while (cursor.moveToNext())
        cursor.close()
        return purchaseOrders
    }

    @Throws(ParseException::class)
    fun getItemPurchaseOrders(item_no: String): ArrayList<PurchaseOrder?>? {
        val purchaseOrders: ArrayList<PurchaseOrder?> = ArrayList()
        val DB = DBHelper!!.readableDatabase
        val QueryCols = arrayOf("purchase_order", "line", "vendor_no",
            "vendor", "item_code", "quantity", "qty_outstanding", "order_date",
            "prev_receive_date", "status")
        val cursor = DB.query(
            TABLE_NAME_PURCHASEORDER,
            QueryCols,
            "item_code=?", arrayOf(item_no),
            null,
            null,
            null,
            null
        ) ?: return purchaseOrders
        if (!cursor.moveToFirst()) return purchaseOrders
        do {
            val purchaseOrder = PurchaseOrder(
                cursor.getString(0),
                cursor.getInt(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getDouble(5),
                cursor.getDouble(6),
                cursor.getString(7),
                cursor.getString(8),
                cursor.getInt(9)
            )
            purchaseOrders.add(purchaseOrder)
        } while (cursor.moveToNext())
        cursor.close()
        return purchaseOrders
    }

}