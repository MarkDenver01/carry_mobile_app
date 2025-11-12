package com.nathaniel.carryapp.data.remote.api

import com.nathaniel.carryapp.domain.request.LoginResponse
import com.nathaniel.carryapp.domain.request.MobileRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/user/public/send_otp")
    suspend fun sendOtp(
        @Body request: MobileRequest
    ): Response<ResponseBody>

    @POST("/user/public/verify_otp")
    suspend fun verifyOtp(
        @Body request: MobileRequest
    ): Response<LoginResponse>
}