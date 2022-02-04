package com.anuranjan.espprovision.common

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T> : Resource<T> {
        constructor(appException: AppException) : super(null, appException.message)
        constructor(message: String, data: T? = null) : super(data, message)
    }
}
