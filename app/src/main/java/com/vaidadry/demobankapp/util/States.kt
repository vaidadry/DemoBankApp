package com.vaidadry.demobankapp.util

sealed class Resource<T>(val data: T?, val message: String?) {
    class Success<T>(data: T) : Resource<T>(data, null)
    class Error<T>(message: String?) : Resource<T>(null, message)
}

sealed class LoadingStatus {
    object Success : LoadingStatus()
    data class Error(val message: String) : LoadingStatus()
    object Loading : LoadingStatus()
}