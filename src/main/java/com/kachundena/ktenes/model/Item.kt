package com.kachundena.ktenes.model

data class Item (
    val item_id: Int,
    val gtin: String,
    val code: String,
    val vendor_code: String?,
    val vendor_no: String?,
    val vendor_name: String?,
    val description: String,
    val category: String,
    val um: String,
    val location: String,
    val price: Double,
    val cost: Double,
    val stock: Double,
    val image: String,
    val readonly: Int = 0,
    val status: String = "")