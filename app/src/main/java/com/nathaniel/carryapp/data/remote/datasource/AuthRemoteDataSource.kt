package com.nathaniel.carryapp.data.remote.datasource

import com.nathaniel.carryapp.data.remote.api.ApiService
import com.nathaniel.carryapp.domain.request.LoginResponse
import com.nathaniel.carryapp.domain.request.MobileRequest
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun sendOtp(mobile: String): Response<ResponseBody> =
        apiService.sendOtp(MobileRequest(mobile))

    suspend fun verifyOtp(mobile: String, otp: String): Response<LoginResponse> =
        apiService.verifyOtp(MobileRequest(mobile, otp))
}
