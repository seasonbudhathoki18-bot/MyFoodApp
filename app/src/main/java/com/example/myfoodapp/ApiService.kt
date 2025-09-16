package com.example.myfoodapp


import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("sydney/auth")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("dashboard/{keypass}")
    fun getDashboard(@Path("keypass") keypass: String): Call<ResponseBody>
}
