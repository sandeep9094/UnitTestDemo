package com.developidea.unittestdemo.api

import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

suspend fun <T> safeApiCall(
    apiCall: suspend () -> Response<T>
): ApiResult<T> {
    return try {
        val response = apiCall()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                ApiResult.Success(body)
            } else {
                ApiResult.Error("Response body is null")
            }
        } else {
            when (response.code()) {
                401 -> ApiResult.Error("Unauthorized: Access is denied due to invalid credentials")
                500 -> ApiResult.Error("Internal Server Error: The server encountered an error and could not complete your request.")
                else -> ApiResult.Error("Error ${response.code()}: ${response.message()}")
            }
        }
    } catch (e: Exception) {
        ApiResult.Error(
            when (e) {
                is HttpException -> {
                    val errorBody = e.response()?.errorBody()?.string()
                    "HTTP Error: ${e.code()} - ${e.message()} - $errorBody"
                }
                is SocketTimeoutException -> "Timeout Error: Request timed out"
                is IOException -> "Network Error: ${e.localizedMessage ?: "Unknown network error"}"
                else -> "Unknown Error: ${e.localizedMessage ?: "An unknown error occurred"}"
            }
        )
    }
}