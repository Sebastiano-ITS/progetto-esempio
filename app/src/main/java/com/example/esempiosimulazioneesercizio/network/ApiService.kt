package com.example.esempiosimulazioneesercizio.network

import com.example.esempiosimulazioneesercizio.data.Product
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiService {
    @GET("products")
    suspend fun getProducts(): List<Product>

    @GET("products/{id}")
    suspend fun getProduct(@retrofit2.http.Path("id") id: Int): Product

    companion object {
        private const val BASE_URL = "https://api.escuelajs.co/api/v1/"

        fun create(): ApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}
