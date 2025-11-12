package com.nathaniel.carryapp.data.repository

import com.nathaniel.carryapp.data.local.prefs.TokenManager
import com.nathaniel.carryapp.data.remote.datasource.AuthRemoteDataSource
import com.nathaniel.carryapp.domain.enum.HttpStatus
import com.nathaniel.carryapp.domain.request.LoginResponse
import com.nathaniel.carryapp.presentation.utils.NetworkResult
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val remote: AuthRemoteDataSource,
    private val tokenManager: TokenManager
) {

    suspend fun sendOtp(mobileNumber: String): NetworkResult<Unit> {
        return try {
            val response = remote.sendOtp(mobileNumber)
            if (response.isSuccessful) {
                NetworkResult.Success(HttpStatus.SUCCESS, Unit)
            } else {
                NetworkResult.Error(HttpStatus.ERROR, response.errorBody()?.string() ?: "Failed to send OTP")
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
                NetworkResult.Error(HttpStatus.ERROR, response.errorBody()?.string() ?: "OTP verification failed")
            }
        } catch (e: Exception) {
            NetworkResult.Error(HttpStatus.ERROR, e.message ?: "Network or server error")
        }
    }
}
