package com.skills.bcg_demo.utils

suspend fun <T : Any> safeApiCall(call: suspend () -> Result<T>): Result<T> = try {
    call.invoke()
} catch (e: Exception) {
    Result.Error(e.message)
}