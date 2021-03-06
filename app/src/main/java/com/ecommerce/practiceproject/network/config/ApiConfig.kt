package com.ecommerce.practiceproject.network.config

import com.ecommerce.practiceproject.data.DefaultResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiConfig
{
    @GET("api/")
    suspend fun getDataList(
        @Query("page") page : Int,
        @Query("results") items : Int
    ) : DefaultResponse

    @GET("api/")
    suspend fun getDataList(
        @Query("results") items : Int
    ) : DefaultResponse
}