package com.kachundena.ktenes.controller

import android.content.ContentValues
import android.content.Context
import com.kachundena.ktenes.database.dbHelper
import com.kachundena.ktenes.model.Parameter
import java.text.ParseException
import java.text.SimpleDateFormat

class ParameterController  (context: Context){
    private var DBHelper: dbHelper
    private val TABLE_NAME_PARAMETERS = "parameters"
    var dateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")

    init {
        // Create new helper
        DBHelper = dbHelper(context)
    }

    fun deleteParameter(): Int {
        val DB = DBHelper!!.writableDatabase
        return DB.delete(TABLE_NAME_PARAMETERS, null, null)
    }

    fun createParameters(parameter: Parameter): Long {
        val DB = DBHelper!!.writableDatabase
        val InsertValues = ContentValues()
        InsertValues.put("company", parameter.company)
        InsertValues.put("apiurl", parameter.apiurl)
        InsertValues.put("logo", parameter.logo)
        InsertValues.put("key", parameter.key)
        InsertValues.put("lastsync_item", parameter.lastsync_item)
        return DB.insert(TABLE_NAME_PARAMETERS, null, InsertValues)
    }

    fun updateParameters(parameter: Parameter): Int {
        val DB = DBHelper!!.writableDatabase
        val UpdateValues = ContentValues()
        UpdateValues.put("company", parameter.company)
        UpdateValues.put("apiurl", parameter.apiurl)
        UpdateValues.put("logo", parameter.logo)
        UpdateValues.put("key", parameter.key)
        UpdateValues.put("lastsync_item", parameter.lastsync_item)
        return DB.update(TABLE_NAME_PARAMETERS, UpdateValues, null, null)
    }

    fun setLastSyncItem(lastsync: String): Int {
        val DB = DBHelper!!.readableDatabase
        val QueryCols = arrayOf("lastsync_item")
        val found: Boolean = false
        val cursor = DB.query(
            TABLE_NAME_PARAMETERS,
            QueryCols,
            null,
            null,
            null,
            null,
            null
        ) ?: return -1
        if (!cursor.moveToFirst()) {
            //insert
            val DBi = DBHelper!!.writableDatabase
            val InsertValues = ContentValues()
            InsertValues.put("company", 94)
            InsertValues.put("lastsync_item", lastsync)
            DBi.insert(TABLE_NAME_PARAMETERS, null, InsertValues)
            return 0
        }
        else {
            // update
            val DBW = DBHelper!!.writableDatabase
            val UpdateValues = ContentValues()
            UpdateValues.put("lastsync_item", lastsync)
            return DBW.update(TABLE_NAME_PARAMETERS, UpdateValues, null, null)

        }
    }

    @Throws(ParseException::class)
    fun getLastSyncItem(): String? {
        var lastsync: String? = null
        val DB = DBHelper!!.readableDatabase
        val QueryCols = arrayOf("lastsync_item")
        val cursor = DB.query(
            TABLE_NAME_PARAMETERS,
            QueryCols,
            null,
            null,
            null,
            null,
            null
        ) ?: return lastsync
        if (!cursor.moveToFirst()) return lastsync
        do {
            lastsync = cursor.getString(0)
        } while (cursor.moveToNext())
        cursor.close()
        return lastsync
    }

    fun setLastSyncSales(lastsync: String): Int {
        val DB = DBHelper!!.writableDatabase
        val UpdateValues = ContentValues()
        UpdateValues.put("lastsync_sales", lastsync)
        return DB.update(TABLE_NAME_PARAMETERS, UpdateValues, null, null)
    }

    @Throws(ParseException::class)
    fun getLastSyncSales(): String? {
        var lastsync: String? = null
        val DB = DBHelper!!.readableDatabase
        val QueryCols = arrayOf("lastsync_sales")
        val cursor = DB.query(
            TABLE_NAME_PARAMETERS,
            QueryCols,
            null,
            null,
            null,
            null,
            null
        ) ?: return lastsync
        if (!cursor.moveToFirst()) return lastsync
        do {
            lastsync = cursor.getString(0)
        } while (cursor.moveToNext())
        cursor.close()
        return lastsync
    }

    fun setLastSyncPurchase(lastsync: String): Int {
        val DB = DBHelper!!.writableDatabase
        val UpdateValues = ContentValues()
        UpdateValues.put("lastsync_purchase", lastsync)
        return DB.update(TABLE_NAME_PARAMETERS, UpdateValues, null, null)
    }

    @Throws(ParseException::class)
    fun getLastSyncPurchase(): String? {
        var lastsync: String? = null
        val DB = DBHelper!!.readableDatabase
        val QueryCols = arrayOf("lastsync_purchase")
        val cursor = DB.query(
            TABLE_NAME_PARAMETERS,
            QueryCols,
            null,
            null,
            null,
            null,
            null
        ) ?: return lastsync
        if (!cursor.moveToFirst()) return lastsync
        do {
            lastsync = cursor.getString(0)
        } while (cursor.moveToNext())
        cursor.close()
        return lastsync
    }


    @Throws(ParseException::class)
    fun getParameters(): Parameter? {
        var parameter: Parameter? = null
        val DB = DBHelper!!.readableDatabase
        val QueryCols = arrayOf("company", "apiurl", "logo", "key", "lastsync_item")
        val cursor = DB.query(
            TABLE_NAME_PARAMETERS,
            QueryCols,
            null,
            null,
            null,
            null,
            null
        ) ?: return parameter
        if (!cursor.moveToFirst()) return parameter
        do {
            parameter = Parameter(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4)
            )
        } while (cursor.moveToNext())
        cursor.close()
        return parameter
    }


}