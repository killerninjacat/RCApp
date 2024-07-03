package com.deanrc.rcapp

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("api/v1/5kerfra12ajnj")
    fun getFileData(): Call<FileData>
}