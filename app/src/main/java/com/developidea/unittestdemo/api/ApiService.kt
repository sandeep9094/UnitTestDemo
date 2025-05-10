package com.developidea.unittestdemo.api

import com.developidea.unittestdemo.data.model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/v2/top-headlines?country=us")
    suspend fun getNews(@Query("apiKey") apiKey: String): Response<NewsResponse>
}