package com.deanrc.rcapp

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("api/v1/d1skg62lqtqo1")
    fun getFileData(): Call<FileData>
}