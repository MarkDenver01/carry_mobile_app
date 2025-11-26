package com.nathaniel.carryapp.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.nathaniel.carryapp.data.local.prefs.TokenManager
import com.nathaniel.carryapp.data.local.room.entity.CustomerEntity
import com.nathaniel.carryapp.data.local.room.entity.DriverEntity
import com.nathaniel.carryapp.data.local.room.entity.LoginEntity
import com.nathaniel.carryapp.domain.datasource.AuthRemoteDatasource
import com.nathaniel.carryapp.domain.datasource.LoginLocalDataSource
import com.nathaniel.carryapp.domain.enum.HttpStatus
import com.nathaniel.carryapp.domain.mapper.BarangayMapper
import com.nathaniel.carryapp.domain.mapper.CityMapper
import com.nathaniel.carryapp.domain.mapper.ProductMapper
import com.nathaniel.carryapp.domain.mapper.ProvinceMapper
import com.nathaniel.carryapp.domain.model.Barangay
import com.nathaniel.carryapp.domain.model.City
import com.nathaniel.carryapp.domain.model.Product
import com.nathaniel.carryapp.domain.model.Province
import com.nathaniel.carryapp.domain.request.CashInRequest
import com.nathaniel.carryapp.domain.request.CustomerDetailRequest
import com.nathaniel.carryapp.domain.request.LoginResponse
import com.nathaniel.carryapp.domain.request.UserHistoryRequest
import com.nathaniel.carryapp.domain.response.CashInInitResponse
import com.nathaniel.carryapp.domain.response.CustomerDetailResponse
import com.nathaniel.carryapp.domain.response.UserHistoryResponse
import com.nathaniel.carryapp.domain.response.WalletResponse
import com.nathaniel.carryapp.presentation.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.internal.http.hasBody
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class ApiRepository @Inject constructor(
    private val remote: AuthRemoteDatasource,
    private val loginLocalDataSource: LoginLocalDataSource,
    private val tokenManager: TokenManager
) {

    suspend fun sendOtp(mobileNumber: String): NetworkResult<Unit> {
        return try {
            val response = remote.sendOtp(mobileNumber)
            if (response.isSuccessful) {
                NetworkResult.Success(HttpStatus.SUCCESS, Unit)
            } else {
                NetworkResult.Error(
                    HttpStatus.ERROR,
                    response.errorBody()?.string() ?: "Failed to send OTP"
                )
            }
        } catch (e: Exception) {
            NetworkResult.Error(HttpStatus.ERROR, e.message ?: "Unexpected error")
        }
    }

    suspend fun verifyOtp(mobileNumber: String, otp: String): NetworkResult<LoginResponse> {
        return try {
            val response = remote.verifyOtp(mobileNumber, otp)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && !body.jwtToken.isNullOrEmpty()) {
                    tokenManager.saveToken(body.jwtToken)
                    NetworkResult.Success(HttpStatus.SUCCESS, body)
                } else {
                    NetworkResult.Error(HttpStatus.ERROR, "Invalid response from server")
                }
            } else {
                NetworkResult.Error(
                    HttpStatus.ERROR,
                    response.errorBody()?.string() ?: "OTP verification failed"
                )
            }
        } catch (e: Exception) {
            NetworkResult.Error(HttpStatus.ERROR, e.message ?: "Network or server error")
        }
    }

    suspend fun getAllProducts(): NetworkResult<List<Product>> {
        return try {
            val response = remote.getAllProducts()

            if (response.isSuccessful) {
                val body = response.body()

                if (body != null) {
                    val mapped = ProductMapper.toDomainList(body)
                    NetworkResult.Success(HttpStatus.SUCCESS, mapped)
                } else {
                    NetworkResult.Error(HttpStatus.ERROR, "Invalid response from server")
                }

            } else {
                NetworkResult.Error(
                    HttpStatus.ERROR,
                    response.errorBody()?.string() ?: "Failed to fetch products"
                )
            }

        } catch (e: Exception) {
            NetworkResult.Error(
                HttpStatus.ERROR,
                e.message ?: "Network or server error"
            )
        }
    }

    suspend fun getProvincesByRegion(regionCode: String): NetworkResult<List<Province>> {
        return try {
            val response = remote.getProvincesByRegion(regionCode)

            if (response.isSuccessful) {
                val body = response.body()

                if (body != null) {
                    val mapped = ProvinceMapper.toDomainList(body)
                    NetworkResult.Success(HttpStatus.SUCCESS, mapped)
                } else {
                    NetworkResult.Error(HttpStatus.ERROR, "Invalid response from PSGC server")
                }

            } else {
                NetworkResult.Error(
                    HttpStatus.ERROR,
                    response.errorBody()?.string() ?: "Failed to load provinces"
                )
            }

        } catch (e: Exception) {
            NetworkResult.Error(
                HttpStatus.ERROR,
                e.message ?: "Network or server error"
            )
        }
    }


    suspend fun getCitiesByProvince(provinceCode: String): NetworkResult<List<City>> {
        return try {
            val response = remote.getCitiesByProvince(provinceCode)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val mapped = CityMapper.toDomainList(body)
                    NetworkResult.Success(HttpStatus.SUCCESS, mapped)
                } else {
                    NetworkResult.Error(HttpStatus.ERROR, "Invalid response from PSGC server")
                }
            } else {
                NetworkResult.Error(
                    HttpStatus.ERROR,
                    response.errorBody()?.string() ?: "Failed to load cities"
                )
            }
        } catch (e: Exception) {
            NetworkResult.Error(
                HttpStatus.ERROR,
                e.message ?: "Network or server error"
            )
        }
    }

    suspend fun getBarangaysByCity(cityCode: String): NetworkResult<List<Barangay>> {
        return try {
            val response = remote.getBarangaysByCity(cityCode)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val mapped = BarangayMapper.toDomainList(body)
                    NetworkResult.Success(HttpStatus.SUCCESS, mapped)
                } else {
                    NetworkResult.Error(HttpStatus.ERROR, "Invalid response from PSGC server")
                }
            } else {
                NetworkResult.Error(
                    HttpStatus.ERROR,
                    response.errorBody()?.string() ?: "Failed to load barangays"
                )
            }
        } catch (e: Exception) {
            NetworkResult.Error(
                HttpStatus.ERROR,
                e.message ?: "Network or server error"
            )
        }
    }


    // Save session to Room
    suspend fun saveLoginSession(
        loginEntity: LoginEntity,
        customerEntity: CustomerEntity?,
        driverEntity: DriverEntity?
    ) {
        loginLocalDataSource.saveLogin(loginEntity, customerEntity, driverEntity)
    }

    suspend fun getCurrentSession() =
        loginLocalDataSource.getCurrentLogin()

    suspend fun getCustomerSession() =
        loginLocalDataSource.getCustomerSession()

    suspend fun getDriverSession() =
        loginLocalDataSource.getDriverSession()

    suspend fun logout() =
        loginLocalDataSource.logout()

    suspend fun updateCustomer(
        request: CustomerDetailRequest
    ): Response<CustomerDetailResponse> {
        return remote.updateCustomer(request)
    }

    suspend fun uploadCustomerPhoto(file: MultipartBody.Part): String {
        val response = remote.uploadCustomerPhoto(file)

        if (!response.isSuccessful)
            throw Exception("Upload failed")

        val body = response.body() ?: throw Exception("No response body")

        return body.url
    }

    suspend fun cashIn(req: CashInRequest): NetworkResult<CashInInitResponse> {
        return try {
            val response = remote.cashIn(req)
            if (response.isSuccessful) {
                val body = response.body()!!
                NetworkResult.Success(HttpStatus.SUCCESS, body)
            } else {
                NetworkResult.Error(HttpStatus.ERROR, "Cash In failed")
            }
        } catch (e: Exception) {
            NetworkResult.Error(HttpStatus.ERROR, e.message ?: "Network error")
        }
    }

    suspend fun getWalletBalance(mobileNumber: String): NetworkResult<WalletResponse> {
        return try {
            val response = remote.getWalletBalance(mobileNumber)
            if (response.isSuccessful) {
                val body = response.body()!!
                NetworkResult.Success(HttpStatus.SUCCESS, body)
            } else {
                NetworkResult.Error(HttpStatus.ERROR, "Failed to load wallet")
            }
        } catch (e: Exception) {
            NetworkResult.Error(HttpStatus.ERROR, e.message ?: "Network error")
        }
    }

    suspend fun getRecommendations(customerId: Long): NetworkResult<List<Product>> {
        return try {
            val response = remote.getRecommendations(customerId)
            if (response.isSuccessful) {
                val body = response.body().orEmpty()
                NetworkResult.Success(
                    HttpStatus.SUCCESS,
                    body.map { ProductMapper.toDomain(it) }
                )
            } else {
                NetworkResult.Error(HttpStatus.ERROR, "Failed to load recommendations")
            }
        } catch (e: Exception) {
            NetworkResult.Error(HttpStatus.ERROR, e.message ?: "Network error (reco)")
        }
    }

    // 🔹 Save user history
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun saveUserHistory(customerId: Long, keyword: String): NetworkResult<Unit> {
        return try {
            val now = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

            val body = UserHistoryRequest(
                customerId = customerId,
                productKeyword = keyword,
                dateTime = now.format(formatter) // string: 2025-11-26T14:34:02
            )

            val response = remote.saveUserHistory(body)
            if (response.isSuccessful) {
                NetworkResult.Success(HttpStatus.SUCCESS, Unit)
            } else {
                NetworkResult.Error(HttpStatus.ERROR, "Failed to save history")
            }
        } catch (e: Exception) {
            NetworkResult.Error(HttpStatus.ERROR, e.message ?: "Network error (save history)")
        }
    }

    // 🔹 Get user history
    suspend fun getUserHistory(customerId: Long): NetworkResult<List<UserHistoryResponse>> {
        return try {
            val response = remote.getUserHistory(customerId)
            if (response.isSuccessful) {
                NetworkResult.Success(
                    HttpStatus.SUCCESS,
                    response.body().orEmpty()
                )
            } else {
                NetworkResult.Error(HttpStatus.ERROR, "Failed to load history")
            }
        } catch (e: Exception) {
            NetworkResult.Error(HttpStatus.ERROR, e.message ?: "Network error (get history)")
        }
    }
}
