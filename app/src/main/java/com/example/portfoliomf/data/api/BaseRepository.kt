package com.example.portfoliomf.data.api

import retrofit2.Response

abstract class BaseRepository {
    protected suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): NetworkResult<T> {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    NetworkResult.Success(body)
                } else {
                    NetworkResult.Error(code = response.code(), message = "Empty response body")
                }
            } else {
                NetworkResult.Error(code = response.code(), message = response.message())
            }
        } catch (e: Exception) {
            NetworkResult.Error(exception = e, message = e.localizedMessage)
        }
    }
}
