package com.nathaniel.carryapp.data.remote.api

import com.nathaniel.carryapp.domain.response.BarangayResponse
import com.nathaniel.carryapp.domain.response.CityResponse
import com.nathaniel.carryapp.domain.response.ProvinceResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PsgcApiService {
    @GET("/api/regions/{regionCode}/provinces/")
    suspend fun getProvinces(
        @Path("regionCode") regionCode: String
    ): Response<List<ProvinceResponse>>


    @GET("/api/provinces/{provinceCode}/cities-municipalities/")
    suspend fun getCities(
        @Path("provinceCode") provinceCode: String
    ): Response<List<CityResponse>>

    @GET("/api/cities-municipalities/{cityCode}/barangays/")
    suspend fun getBarangays(
        @Path("cityCode") cityCode: String
    ): Response<List<BarangayResponse>>
}