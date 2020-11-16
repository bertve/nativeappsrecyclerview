package com.example.nativeapps.api

import android.util.Log
import com.example.nativeapps.util.Resource
import retrofit2.Response

/**
 * Encapsulate the Retrofit response in a Resource, so that we can catch errors nicely.
 */
abstract class BaseDataSource {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return Resource.success(body)
            }
            return error(" ${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String): Resource<T> {
        Log.e("remoteDataSource", message)
        return Resource.error(data = null, "Network call has failed for a following reason: $message")
    }
}