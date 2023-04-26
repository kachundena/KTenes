package com.kachundena.ktenes.api

import com.kachundena.ktenes.config.Parameters
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit

class RetrofitClient {
    companion object {
        val _baseUrl = Parameters.baseUrl

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(_baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}