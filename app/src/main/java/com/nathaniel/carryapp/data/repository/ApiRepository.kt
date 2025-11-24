package com.nathaniel.carryapp.data.repository

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
import com.nathaniel.carryapp.domain.request.CustomerDetailRequest
import com.nathaniel.carryapp.domain.request.LoginResponse
import com.nathaniel.carryapp.domain.response.CustomerDetailResponse
import com.nathaniel.carryapp.presentation.utils.NetworkResult
import okhttp3.MultipartBody
import retrofit2.Response
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
        return remote.updateCustomer( request)
    }

    suspend fun uploadCustomerPhoto(file: MultipartBody.Part): String {
        val response = remote.uploadCustomerPhoto(file)

        if (!response.isSuccessful)
            throw Exception("Upload failed")

        val body = response.body() ?: throw Exception("No response body")

        return body.url
    }
}
