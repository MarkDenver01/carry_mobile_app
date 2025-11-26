package com.nathaniel.carryapp.data.remote.api

import com.nathaniel.carryapp.domain.request.CashInRequest
import com.nathaniel.carryapp.domain.request.CustomerDetailRequest
import com.nathaniel.carryapp.domain.request.LoginResponse
import com.nathaniel.carryapp.domain.request.MobileRequest
import com.nathaniel.carryapp.domain.request.UserHistoryRequest
import com.nathaniel.carryapp.domain.response.CashInInitResponse
import com.nathaniel.carryapp.domain.response.CustomerDetailResponse
import com.nathaniel.carryapp.domain.response.ProductResponse
import com.nathaniel.carryapp.domain.response.UploadPhotoResponse
import com.nathaniel.carryapp.domain.response.UserHistoryResponse
import com.nathaniel.carryapp.domain.response.WalletResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("/user/public/send_otp")
    suspend fun sendOtp(
        @Body request: MobileRequest
    ): Response<ResponseBody>

    @POST("/user/public/verify_otp")
    suspend fun verifyOtp(
        @Body request: MobileRequest
    ): Response<LoginResponse>

    @GET("/user/public/api/price/all")
    suspend fun getAllProducts(): Response<List<ProductResponse>>

    @POST("/user/public/customer")
    suspend fun saveCustomerDetails(
        @Body request: CustomerDetailRequest
    ): Response<ResponseBody>

    @PUT("/user/public/customer/update")
    suspend fun updateCustomer(@Body request: CustomerDetailRequest
    ): Response<CustomerDetailResponse>

    @Multipart
    @POST("/user/public/customer/upload-photo")
    suspend fun uploadCustomerPhoto(
        @Part file: MultipartBody.Part
    ): Response<UploadPhotoResponse>

    @POST("api/wallet/cash-in")
    suspend fun cashIn(
        @Body req: CashInRequest
    ): Response<CashInInitResponse>

    @GET("api/wallet/balance")
    suspend fun getWallet(
        @Query("mobileNumber") mobileNumber: String
    ): Response<WalletResponse>

    // 🔹 NEW – AI recommendations based on history
    @GET("/user/public/recommendations/{customerId}")
    suspend fun getRecommendations(
        @Path("customerId") customerId: Long
    ): Response<List<ProductResponse>>

    // 🔹 GET user history
    @GET("/user/public/history/{customerId}")
    suspend fun getUserHistory(
        @Path("customerId") customerId: Long
    ): Response<List<UserHistoryResponse>>

    // 🔹 SAVE user history (search/click/etc.)
    @POST("/user/public/history/save")
    suspend fun saveUserHistory(
        @Body body: UserHistoryRequest
    ): Response<UserHistoryResponse>
}