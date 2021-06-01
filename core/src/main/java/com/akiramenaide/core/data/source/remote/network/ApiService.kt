package com.akiramenaide.core.data.source.remote.network

import com.akiramenaide.core.data.source.remote.response.GetIndex
import com.akiramenaide.core.data.source.remote.response.PostedImage
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("/api/predict/img/")
    suspend fun getPredict(
        @Body data: String
    ): PostedImage

    @GET("/")
    fun getValue(): Call<GetIndex>

}