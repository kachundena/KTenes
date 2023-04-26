package com.kachundena.ktenes.api

import com.kachundena.ktenes.model.Item
import com.kachundena.ktenes.model.LastTS
import com.kachundena.ktenes.model.PurchaseOrder
import com.kachundena.ktenes.model.SalesOrder
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import java.sql.Timestamp

interface kTenesApi {
    // Méthod to get Items
    @GET("items")
    fun getItems(): Call<List<Item>>
    // Méthod to get last Timestamp
    @GET("items/maxts")
    fun getLastTS(): Call<List<LastTS>>
    // Méthod to get Items Modifieds
    @GET("items/modified/{timestamp}")
    fun getLastItems(@Path("timestamp") timestamp: String): Call<List<Item>>
    // Méthod to get Sales Order
    @GET("salesorder")
    fun getSalesOrder(): Call<List<SalesOrder>>
    // Méthod to get Sales Order
    @GET("purchaseorder")
    fun getPurchaseOrder(): Call<List<PurchaseOrder>>


}