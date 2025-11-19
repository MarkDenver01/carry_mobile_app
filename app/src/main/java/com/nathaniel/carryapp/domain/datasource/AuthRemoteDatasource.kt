package com.nathaniel.carryapp.domain.datasource

import com.nathaniel.carryapp.domain.request.LoginResponse
import com.nathaniel.carryapp.domain.response.BarangayResponse
import com.nathaniel.carryapp.domain.response.CityResponse
import com.nathaniel.carryapp.domain.response.ProductResponse
import com.nathaniel.carryapp.domain.response.ProvinceResponse
import okhttp3.ResponseBody
import retrofit2.Response

interface AuthRemoteDatasource {
    suspend fun sendOtp(mobile: String): Response<ResponseBody>
    suspend fun verifyOtp(mobile: String, otp: String): Response<LoginResponse>

    suspend fun getAllProducts(): Response<List<ProductResponse>>

    suspend fun getProvincesByRegion(regionCode: String): Response<List<ProvinceResponse>>

    suspend fun getCitiesByProvince(provinceCode: String): Response<List<CityResponse>>

    suspend fun getBarangaysByCity(cityCode: String): Response<List<BarangayResponse>>

}