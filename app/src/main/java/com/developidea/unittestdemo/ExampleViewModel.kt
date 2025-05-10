package com.developidea.unittestdemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developidea.unittestdemo.api.ApiResult
import com.developidea.unittestdemo.api.ApiService
import com.developidea.unittestdemo.api.safeApiCall
import com.developidea.unittestdemo.data.model.NewsResponse
import com.developidea.unittestdemo.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExampleViewModel(
    private val apiService: ApiService
    ) : ViewModel() {

    private val _newsResult = MutableStateFlow<ApiResult<NewsResponse>?>(null)
    val newsResult: StateFlow<ApiResult<NewsResponse>?> = _newsResult.asStateFlow()

    fun getNews(apiKey: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _newsResult.value = ApiResult.Loading
            _newsResult.value = safeApiCall { apiService.getNews(apiKey) }
        }
    }

}
