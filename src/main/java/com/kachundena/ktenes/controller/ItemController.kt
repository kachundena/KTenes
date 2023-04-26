package com.kachundena.ktenes.controller

import android.content.ContentValues
import android.content.Context
import com.kachundena.ktenes.database.dbHelper
import com.kachundena.ktenes.model.Item
import java.text.ParseException
import java.text.SimpleDateFormat

class ItemController  (context: Context){
    private var DBHelper: dbHelper
    private val TABLE_NAME_ITEM = "item"
    var dateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")

    init {
        // Create new helper
        DBHelper = dbHelper(context)
    }


    fun deleteItem(item: Item): Int {
        val DB = DBHelper!!.writableDatabase
        val argumentos = arrayOf<String>(item.gtin)
        return DB.delete(TABLE_NAME_ITEM, "gtin = ?", argumentos)
    }

    fun deleteAllItem(): Int {
        val DB = DBHelper!!.writableDatabase
        return DB.delete(TABLE_NAME_ITEM, null, null)
    }

    fun newItem(item: Item): Long {
        val DB = DBHelper!!.writableDatabase
        val InsertValues = ContentValues()
        InsertValues.put("gtin", item.gtin)
        InsertValues.put("code", item.code)
        InsertValues.put("vendor_code", item.vendor_code)
        InsertValues.put("vendor_no", item.vendor_no)
        InsertValues.put("vendor_name", item.vendor_name)
        InsertValues.put("description", item.description)
        InsertValues.put("category", item.category)
        InsertValues.put("um", item.um)
        InsertValues.put("location", item.location)        
        InsertValues.put("price", item.price)
        InsertValues.put("cost", item.cost)
        InsertValues.put("stock", item.stock)
        InsertValues.put("image", item.image)
        InsertValues.put("readonly", item.readonly)
        InsertValues.put("status", item.status)
        return DB.insert(TABLE_NAME_ITEM, null, InsertValues)
    }

    fun updateItem(item: Item): Int {
        val DB = DBHelper!!.writableDatabase
        val UpdateValues = ContentValues()
        UpdateValues.put("gtin", item.gtin)
        UpdateValues.put("code", item.code)
        UpdateValues.put("vendor_code", item.vendor_code)
        UpdateValues.put("vendor_no", item.vendor_no)
        UpdateValues.put("vendor_name", item.vendor_name)
        UpdateValues.put("description", item.description)
        UpdateValues.put("category", item.category)
        UpdateValues.put("um", item.um)
        UpdateValues.put("location", item.location)
        UpdateValues.put("price", item.price)
        UpdateValues.put("cost", item.cost)
        UpdateValues.put("stock", item.stock)
        UpdateValues.put("image", item.image)
        UpdateValues.put("status", item.status)
        val PKField = "item_id = ?"
        val ValuesPKField = arrayOf<String>(item.item_id.toString())
        return DB.update(TABLE_NAME_ITEM, UpdateValues, PKField, ValuesPKField)
    }
    
    fun updateinsertItem(item:Item): Int {
        //var _item: Item? = null
        val DB = DBHelper!!.readableDatabase
        val QueryCols = arrayOf("item_id")
        val found: Boolean = false
        val cursor = DB.query(
            TABLE_NAME_ITEM,
            QueryCols,
            "code=?", arrayOf(item.code.toString()),
            null,
            null,
            null
        ) ?: return -1
        if (!cursor.moveToFirst()) {
        	// inssert
            val DBi = DBHelper!!.writableDatabase
            val InsertValues = ContentValues()
            InsertValues.put("gtin", item.gtin)
            InsertValues.put("code", item.code)
            InsertValues.put("vendor_code", item.vendor_code)
            InsertValues.put("vendor_no", item.vendor_no)
            InsertValues.put("vendor_name", item.vendor_name)
            InsertValues.put("description", item.description)
            InsertValues.put("category", item.category)
            InsertValues.put("um", item.um)
            InsertValues.put("location", item.location)        
            InsertValues.put("price", item.price)
            InsertValues.put("cost", item.cost)
            InsertValues.put("stock", item.stock)
            InsertValues.put("image", item.image)
            InsertValues.put("readonly", item.readonly)
            InsertValues.put("status", item.status)
            DBi.insert(TABLE_NAME_ITEM, null, InsertValues)
            return 0
        
        }
        else {
        	// update
            val DBu = DBHelper!!.writableDatabase
            val UpdateValues = ContentValues()
            UpdateValues.put("gtin", item.gtin)
            UpdateValues.put("code", item.code)
            UpdateValues.put("vendor_code", item.vendor_code)
            UpdateValues.put("vendor_no", item.vendor_no)
            UpdateValues.put("vendor_name", item.vendor_name)
            UpdateValues.put("description", item.description)
            UpdateValues.put("category", item.category)
            UpdateValues.put("um", item.um)
            UpdateValues.put("location", item.location)
            UpdateValues.put("price", item.price)
            UpdateValues.put("cost", item.cost)
            UpdateValues.put("stock", item.stock)
            UpdateValues.put("image", item.image)
            UpdateValues.put("status", item.status)
            val PKField = "item_id = ?"
            val ValuesPKField = arrayOf<String>(cursor.getString(0))
            DBu.update(TABLE_NAME_ITEM, UpdateValues, PKField, ValuesPKField)
            return 0
        }    	
    }

    @Throws(ParseException::class)
    fun getItem(item_id: Int): Item? {
        var item: Item? = null
        val DB = DBHelper!!.readableDatabase
        val QueryCols = arrayOf("item_id", "gtin", "code", "vendor_code", 
            "vendor_no", "vendor_name", "description", "category", "um",
            "location", "price", "cost", "stock", "image", "readonly", "status")
        val cursor = DB.query(
            TABLE_NAME_ITEM,
            QueryCols,
            "item_id=?", arrayOf(item_id.toString()),
            null,
            null,
            null
        ) ?: return item
        if (!cursor.moveToFirst()) return item
        do {
            item = Item(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6),
                cursor.getString(7),
                cursor.getString(8),               
                cursor.getString(9),               
                cursor.getDouble(10),
                cursor.getDouble(11),
                cursor.getDouble(12),
                cursor.getString(13),
                cursor.getInt(14),
                cursor.getString(15)
            )
        } while (cursor.moveToNext())
        cursor.close()
        return item
    }

    @Throws(ParseException::class)
    fun getItems(): ArrayList<Item?>? {
        val items: ArrayList<Item?> = ArrayList()
        val DB = DBHelper!!.readableDatabase
        val QueryCols = arrayOf("item_id", "gtin", "code", "vendor_code", "vendor_no", "vendor_name", 
            "description", "category", "um",
            "location", "price", "cost", "stock", "image", "readonly", "status")
        val cursor = DB.query(
            TABLE_NAME_ITEM,
            QueryCols,
            null,
            null,
            null,
            null,
            null
        ) ?: return items
        if (!cursor.moveToFirst()) return items
        do {
            val item = Item(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6),
                cursor.getString(7),
                cursor.getString(8),
                cursor.getString(9),
                cursor.getDouble(10),
                cursor.getDouble(11),
                cursor.getDouble(12),
                cursor.getString(13),
                cursor.getInt(14),
                cursor.getString(15)
            )
            items.add(item)
        } while (cursor.moveToNext())
        cursor.close()
        return items
    }

    @Throws(ParseException::class)
    fun getItems(filter: String): ArrayList<Item?>? {
        val items: ArrayList<Item?> = ArrayList()
        val DB = DBHelper!!.readableDatabase
        val QueryCols = arrayOf("item_id", "gtin", "code", "vendor_code", "vendor_no", "vendor_name", 
            "description", "category", "um", "location",
            "price", "cost", "stock", "image", "readonly", "status")
        val cursor = DB.query(
            TABLE_NAME_ITEM,
            QueryCols,
            "gtin LIKE '%$filter%' OR code LIKE '%$filter%' OR vendor_code LIKE '%$filter%' OR description LIKE '%$filter%'",
            null,
            null,
            null,
            null
        ) ?: return items
        if (!cursor.moveToFirst()) return items
        do {
            val item = Item(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6),
                cursor.getString(7),
                cursor.getString(8),
                cursor.getString(9),
                cursor.getDouble(10),
                cursor.getDouble(11),
                cursor.getDouble(12),
                cursor.getString(13),
                cursor.getInt(14),
                cursor.getString(15)
            )
            items.add(item)
        } while (cursor.moveToNext())
        cursor.close()
        return items
    }


}