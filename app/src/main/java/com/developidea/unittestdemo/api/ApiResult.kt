package com.developidea.unittestdemo.api

sealed class ApiResult<out T> {
    data object Loading: ApiResult<Nothing>()
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Error(val errorMessage: String) : ApiResult<Nothing>()
}