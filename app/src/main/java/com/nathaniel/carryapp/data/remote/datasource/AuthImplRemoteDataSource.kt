package com.nathaniel.carryapp.data.remote.datasource

import com.nathaniel.carryapp.data.remote.api.ApiService
import com.nathaniel.carryapp.data.remote.api.PsgcApiService
import com.nathaniel.carryapp.domain.datasource.AuthRemoteDatasource
import com.nathaniel.carryapp.domain.request.CustomerDetailRequest
import com.nathaniel.carryapp.domain.request.LoginResponse
import com.nathaniel.carryapp.domain.request.MobileRequest
import com.nathaniel.carryapp.domain.response.BarangayResponse
import com.nathaniel.carryapp.domain.response.CityResponse
import com.nathaniel.carryapp.domain.response.CustomerDetailResponse
import com.nathaniel.carryapp.domain.response.ProductResponse
import com.nathaniel.carryapp.domain.response.ProvinceResponse
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class AuthImplRemoteDataSource @Inject constructor(
    private val apiService: ApiService,
    private val psgcApiService: PsgcApiService
) : AuthRemoteDatasource {

    override suspend fun sendOtp(mobile: String): Response<ResponseBody> =
        apiService.sendOtp(MobileRequest(mobile))

    override suspend fun verifyOtp(mobile: String, otp: String): Response<LoginResponse> =
        apiService.verifyOtp(MobileRequest(mobile, otp))

    override suspend fun getAllProducts(): Response<List<ProductResponse>> =
        apiService.getAllProducts()

    override suspend fun getProvincesByRegion(regionCode: String): Response<List<ProvinceResponse>> =
        psgcApiService.getProvinces(regionCode)

    override suspend fun getCitiesByProvince(provinceCode: String): Response<List<CityResponse>> =
        psgcApiService.getCities(provinceCode)

    override suspend fun getBarangaysByCity(cityCode: String): Response<List<BarangayResponse>> =
        psgcApiService.getBarangays(cityCode)

    override suspend fun updateCustomer(
        identifier: String,
        request: CustomerDetailRequest
    ): Response<CustomerDetailResponse> =
        apiService.updateCustomer(identifier, request)
}
