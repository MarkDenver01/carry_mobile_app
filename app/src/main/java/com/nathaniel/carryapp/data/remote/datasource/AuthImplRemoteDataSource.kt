package com.nathaniel.carryapp.data.remote.datasource

import com.nathaniel.carryapp.data.remote.api.ApiService
import com.nathaniel.carryapp.data.remote.api.PsgcApiService
import com.nathaniel.carryapp.domain.datasource.AuthRemoteDatasource
import com.nathaniel.carryapp.domain.request.CashInRequest
import com.nathaniel.carryapp.domain.request.CustomerDetailRequest
import com.nathaniel.carryapp.domain.request.LoginResponse
import com.nathaniel.carryapp.domain.request.MobileRequest
import com.nathaniel.carryapp.domain.request.UserHistoryRequest
import com.nathaniel.carryapp.domain.response.BarangayResponse
import com.nathaniel.carryapp.domain.response.CashInInitResponse
import com.nathaniel.carryapp.domain.response.CityResponse
import com.nathaniel.carryapp.domain.response.CustomerDetailResponse
import com.nathaniel.carryapp.domain.response.ProductCategoryResponse
import com.nathaniel.carryapp.domain.response.ProductResponse
import com.nathaniel.carryapp.domain.response.ProvinceResponse
import com.nathaniel.carryapp.domain.response.UploadPhotoResponse
import com.nathaniel.carryapp.domain.response.UserHistoryResponse
import com.nathaniel.carryapp.domain.response.WalletResponse
import okhttp3.MultipartBody
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
        request: CustomerDetailRequest
    ): Response<CustomerDetailResponse> =
        apiService.updateCustomer(request)

    override suspend fun uploadCustomerPhoto(
        file: MultipartBody.Part
    ): Response<UploadPhotoResponse> = apiService.uploadCustomerPhoto(file)

    override suspend fun cashIn(cashInRequest: CashInRequest): Response<CashInInitResponse> {
        return apiService.cashIn(cashInRequest)
    }

    override suspend fun getWalletBalance(mobileNumber: String): Response<WalletResponse> {
        return apiService.getWallet(mobileNumber)
    }

    override suspend fun getRecommendations(customerId: Long): Response<List<ProductResponse>> =
        apiService.getRecommendations(customerId)

    override suspend fun saveUserHistory(request: UserHistoryRequest): Response<UserHistoryResponse> =
        apiService.saveUserHistory(request)

    override suspend fun getUserHistory(customerId: Long): Response<List<UserHistoryResponse>> =
        apiService.getUserHistory(customerId)

    override suspend fun getRelatedProducts(productId: Long): Response<List<ProductResponse>> =
        apiService.getRelatedProducts(productId)

    override suspend fun getAllCategory(): Response<ProductCategoryResponse> =
        apiService.getAllCategories()

}
