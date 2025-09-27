package com.aeterna.core.data.network

/** A generic wrapper for API responses that encapsulates success and error states */
sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val exception: ApiException) : ApiResult<Nothing>()
    object Loading : ApiResult<Nothing>()
}

/** Custom exception for API-related errors */
sealed class ApiException(message: String, cause: Throwable? = null) : Exception(message, cause) {

    class NetworkException(message: String, cause: Throwable? = null) :
            ApiException(message, cause)
    class ServerException(val code: Int, message: String) : ApiException(message)
    class AuthenticationException(message: String) : ApiException(message)
    class RateLimitException(message: String) : ApiException(message)
    class ParseException(message: String, cause: Throwable? = null) : ApiException(message, cause)
    class UnknownException(message: String, cause: Throwable? = null) :
            ApiException(message, cause)
}

/** Extension function to safely execute API calls with proper error handling */
suspend fun <T> safeApiCall(apiCall: suspend () -> T): ApiResult<T> {
    return try {
        val result = apiCall()
        ApiResult.Success(result)
    } catch (e: Exception) {
        val apiException =
                when (e) {
                    is java.net.UnknownHostException ->
                            ApiException.NetworkException("No internet connection", e)
                    is java.net.SocketTimeoutException ->
                            ApiException.NetworkException("Request timeout", e)
                    is retrofit2.HttpException -> {
                        when (e.code()) {
                            401 -> ApiException.AuthenticationException("Authentication required")
                            429 -> ApiException.RateLimitException("Rate limit exceeded")
                            in 500..599 -> ApiException.ServerException(e.code(), "Server error")
                            else ->
                                    ApiException.ServerException(
                                            e.code(),
                                            e.message() ?: "HTTP error"
                                    )
                        }
                    }
                    is com.google.gson.JsonSyntaxException ->
                            ApiException.ParseException("Failed to parse response", e)
                    else -> ApiException.UnknownException(e.message ?: "Unknown error occurred", e)
                }
        ApiResult.Error(apiException)
    }
}

/** Extension function to handle ApiResult with success and error callbacks */
inline fun <T> ApiResult<T>.onSuccess(action: (T) -> Unit): ApiResult<T> {
    if (this is ApiResult.Success) action(data)
    return this
}

inline fun <T> ApiResult<T>.onError(action: (ApiException) -> Unit): ApiResult<T> {
    if (this is ApiResult.Error) action(exception)
    return this
}

inline fun <T> ApiResult<T>.onLoading(action: () -> Unit): ApiResult<T> {
    if (this is ApiResult.Loading) action()
    return this
}

/** Extension function to map ApiResult to another type */
inline fun <T, R> ApiResult<T>.map(transform: (T) -> R): ApiResult<R> {
    return when (this) {
        is ApiResult.Success -> ApiResult.Success(transform(data))
        is ApiResult.Error -> ApiResult.Error(exception)
        is ApiResult.Loading -> ApiResult.Loading
    }
}

/** Extension function to get data or null */
fun <T> ApiResult<T>.getDataOrNull(): T? {
    return when (this) {
        is ApiResult.Success -> data
        else -> null
    }
}

/** Extension function to get data or default value */
fun <T> ApiResult<T>.getDataOrDefault(defaultValue: T): T {
    return when (this) {
        is ApiResult.Success -> data
        else -> defaultValue
    }
}
