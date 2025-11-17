package com.nathaniel.carryapp.domain.datasource

import com.nathaniel.carryapp.domain.request.LoginResponse
import okhttp3.ResponseBody
import retrofit2.Response

interface AuthRemoteDatasource {
    suspend fun sendOtp(mobile: String): Response<ResponseBody>
    suspend fun verifyOtp(mobile: String, otp: String): Response<LoginResponse>
}