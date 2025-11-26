package com.nathaniel.carryapp.domain.datasource

import com.nathaniel.carryapp.domain.request.CashInRequest
import com.nathaniel.carryapp.domain.request.CheckoutRequest
import com.nathaniel.carryapp.domain.request.CustomerDetailRequest
import com.nathaniel.carryapp.domain.request.LoginResponse
import com.nathaniel.carryapp.domain.request.UpdateWalletBalanceRequest
import com.nathaniel.carryapp.domain.request.UserHistoryRequest
import com.nathaniel.carryapp.domain.response.BarangayResponse
import com.nathaniel.carryapp.domain.response.CashInInitResponse
import com.nathaniel.carryapp.domain.response.CityResponse
import com.nathaniel.carryapp.domain.response.CustomerDetailResponse
import com.nathaniel.carryapp.domain.response.OrderResponse
import com.nathaniel.carryapp.domain.response.ProductCategoryResponse
import com.nathaniel.carryapp.domain.response.ProductResponse
import com.nathaniel.carryapp.domain.response.ProvinceResponse
import com.nathaniel.carryapp.domain.response.UploadPhotoResponse
import com.nathaniel.carryapp.domain.response.UserHistoryResponse
import com.nathaniel.carryapp.domain.response.WalletResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response

interface AuthRemoteDatasource {
    suspend fun sendOtp(mobile: String): Response<ResponseBody>
    suspend fun verifyOtp(mobile: String, otp: String): Response<LoginResponse>

    suspend fun getAllProducts(): Response<List<ProductResponse>>

    suspend fun getProvincesByRegion(regionCode: String): Response<List<ProvinceResponse>>

    suspend fun getCitiesByProvince(provinceCode: String): Response<List<CityResponse>>

    suspend fun getBarangaysByCity(cityCode: String): Response<List<BarangayResponse>>

    suspend fun updateCustomer(request: CustomerDetailRequest): Response<CustomerDetailResponse>

    suspend fun uploadCustomerPhoto(file: MultipartBody.Part): Response<UploadPhotoResponse>

    suspend fun cashIn(cashInRequest: CashInRequest): Response<CashInInitResponse>

    suspend fun getWalletBalance(mobileNumber: String): Response<WalletResponse>

    suspend fun getRecommendations(customerId: Long): Response<List<ProductResponse>>

    suspend fun saveUserHistory(request: UserHistoryRequest): Response<UserHistoryResponse>

    suspend fun getUserHistory(customerId: Long): Response<List<UserHistoryResponse>>

    suspend fun getRelatedProducts(productId: Long): Response<List<ProductResponse>>

    suspend fun getAllCategory(): Response<ProductCategoryResponse>

    suspend fun checkOUt(request: CheckoutRequest): Response<OrderResponse>

    suspend fun updateWallet(request: UpdateWalletBalanceRequest): Response<WalletResponse>

    suspend fun getCustomerWalletBalance(mobileNumber: String): Response<WalletResponse>
}