package com.arer.app.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val isLoggedIn: Flow<Boolean>
    suspend fun requestOtp(mobileNumber: String): Result<Unit>
    suspend fun verifyOtp(mobileNumber: String, otp: String): Result<Unit>
    suspend fun logout()
}
