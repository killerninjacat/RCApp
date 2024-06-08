package com.deanrc.rcapp

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("api/v1/q0a22df77cwki")
    fun getFileData(): Call<FileData>
}