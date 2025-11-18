package com.nathaniel.carryapp.data.remote.datasource

import com.nathaniel.carryapp.data.remote.api.ApiService
import com.nathaniel.carryapp.domain.datasource.AuthRemoteDatasource
import com.nathaniel.carryapp.domain.request.LoginResponse
import com.nathaniel.carryapp.domain.request.MobileRequest
import com.nathaniel.carryapp.domain.response.ProductResponse
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class AuthImplRemoteDataSource @Inject constructor(
    private val apiService: ApiService
) : AuthRemoteDatasource {

    override suspend fun sendOtp(mobile: String): Response<ResponseBody> =
        apiService.sendOtp(MobileRequest(mobile))

    override suspend fun verifyOtp(mobile: String, otp: String): Response<LoginResponse> =
        apiService.verifyOtp(MobileRequest(mobile, otp))

    override suspend fun getAllProducts(): Response<List<ProductResponse>> =
        apiService.getAllProducts()
}
