package com.arer.app.data.repository

import com.arer.app.data.local.preferences.AppPreferences
import com.arer.app.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * DEVELOPMENT ONLY authentication implementation.
 * Never use in production. Does not store OTPs in plain text or interact with remote servers.
 */
@Singleton
class DevelopmentAuthRepository @Inject constructor(
    private val appPreferences: AppPreferences
) : AuthRepository {

    override val isLoggedIn: Flow<Boolean> = appPreferences.isLoggedIn

    override suspend fun requestOtp(mobileNumber: String): Result<Unit> {
        // Development only: Always succeeds
        return Result.success(Unit)
    }

    override suspend fun verifyOtp(mobileNumber: String, otp: String): Result<Unit> {
        // Development only: Any 4-digit OTP or default "1234" succeeds
        if (otp.length >= 4) {
            appPreferences.setLoggedIn(true)
            return Result.success(Unit)
        }
        return Result.failure(IllegalArgumentException("Invalid OTP for Development mode"))
    }

    override suspend fun logout() {
        appPreferences.setLoggedIn(false)
        appPreferences.clearAll()
    }
}
