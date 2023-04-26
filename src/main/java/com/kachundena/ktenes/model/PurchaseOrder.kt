package com.kachundena.ktenes.model

data class PurchaseOrder (
    val purchase_order: String,
    val line: Int,
    val vendor_no: String,
    val vendor: String,
    val item_code: String,
    val quantity: Double,
    val qty_outstanding: Double,
    val order_date: String,
    val prev_receive_date: String,
    val status: Int
)