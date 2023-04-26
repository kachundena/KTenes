package com.kachundena.ktenes.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class dbHelper(context: Context?) : SQLiteOpenHelper(context, NAME_DB, null, VERSION_BASE_DE_DATOS) {

    override fun onCreate(db: SQLiteDatabase) {
        var szSQL = "CREATE TABLE IF NOT EXISTS %s(" +
                "item_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "gtin TEXT," +
                "code TEXT," +
                "vendor_code TEXT," +
                "vendor_no TEXT," +
                "vendor_name TEXT," +
                "description TEXT," +
                "category TEXT," +
                "um TEXT," +
                "location TEXT," +
                "price REAL," +
                "cost REAL," +
                "stock REAL, " +
                "image TEXT, " +
                "status TEXT, " +
                "readonly INTEGER " +
                ");"
        db.execSQL(String.format(szSQL, NAME_TABLE_ITEM))
        szSQL = "CREATE TABLE IF NOT EXISTS %s(" +
                "sales_order TEXT NOT NULL," +
                "line INTEGER," +
                "external_doc TEXT," +
                "customer_no TEXT," +
                "customer TEXT," +
                "item_code TEXT," +
                "quantity REAL," +
                "qty_outstanding REAL," +
                "order_date TEXT," +
                "prev_send_date TEXT," +
                "status INTEGER," +
                "PRIMARY KEY (sales_order, line)" +
                ");"
        db.execSQL(String.format(szSQL, NAME_TABLE_SALESORDER))
        szSQL = "CREATE TABLE IF NOT EXISTS %s(" +
                "purchase_order TEXT NOT NULL," +
                "line INTEGER," +
                "vendor_no TEXT," +
                "vendor TEXT," +
                "item_code TEXT," +
                "quantity REAL," +
                "qty_outstanding REAL," +
                "order_date TEXT," +
                "prev_receive_date TEXT," +
                "status INTEGER," +
                "PRIMARY KEY (purchase_order, line)" +
                ");"
        db.execSQL(String.format(szSQL, NAME_TABLE_PURCHASEORDER))
        szSQL = "CREATE TABLE IF NOT EXISTS %s(" +
                "category TEXT NOT NULL PRIMARY KEY," +
                "parent TEXT," +
                "logo TEXT" +
                ");"
        db.execSQL(String.format(szSQL, NAME_TABLE_CATEGORY))
        szSQL = "CREATE TABLE IF NOT EXISTS %s(" +
                "company INT NOT NULL," +
                "apiurl TEXT," +
                "logo TEXT," +
                "key TEXT," +
                "lastsync_item TEXT" +
                ");"
        db.execSQL(String.format(szSQL, NAME_TABLE_PARAMETERS))
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(String.format("DELETE TABLE $NAME_TABLE_ITEM;"))
        db.execSQL(String.format("DELETE TABLE $NAME_TABLE_CATEGORY;"))
        db.execSQL(String.format("DELETE TABLE $NAME_TABLE_PARAMETERS;"))
        db.execSQL(String.format("DELETE TABLE $NAME_TABLE_SALESORDER;"))
        db.execSQL(String.format("DELETE TABLE $NAME_TABLE_PURCHASEORDER;"))
        onCreate(db)
    }


    companion object {
        private val NAME_DB = "ktenes_v0.0.6"
        private val NAME_TABLE_ITEM = "item"
        private val NAME_TABLE_CATEGORY = "category"
        private val NAME_TABLE_PARAMETERS = "parameters"
        private val NAME_TABLE_SALESORDER = "sales_order"
        private val NAME_TABLE_PURCHASEORDER = "purchase_order"
        private val VERSION_BASE_DE_DATOS = 1

    }
}