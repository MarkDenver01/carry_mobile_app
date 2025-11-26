package com.nathaniel.carryapp.data.local.prefs

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        prefs.edit().putString("jwt_token", token).apply()
    }

    fun saveMobileOrEmail(mobileOrEmail: String) {
        prefs.edit().putString("mobile_email", mobileOrEmail).apply()
    }

    fun getToken(): String? {
        return prefs.getString("jwt_token", null)
    }

    fun getMobileOrEmail(): String? {
        return prefs.getString("mobile_email", null)
    }

    fun userSession(session: Boolean) {
        prefs.edit().putBoolean("session_key", session).apply()
    }

    fun getUserSession(): Boolean = prefs.getBoolean("session_key", false)


    fun clearToken() {
        prefs.edit().remove("jwt_token").apply()
        prefs.edit().remove("mobile_email").apply()
    }
}