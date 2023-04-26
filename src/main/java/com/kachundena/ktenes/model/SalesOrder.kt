package com.kachundena.ktenes.model

data class SalesOrder (
    val sales_order: String,
    val line: Int,
    val external_doc: String,
    val customer_no: String,
    val customer: String,
    val item_code: String,
    val quantity: Double,
    val qty_outstanding: Double,
    val order_date: String,
    val prev_send_date: String,
    val status: Int
)