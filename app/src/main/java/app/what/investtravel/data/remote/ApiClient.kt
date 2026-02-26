package app.what.investtravel.data.remote

import app.what.investtravel.data.local.settings.AppValues
import io.ktor.client.HttpClient

class ApiClient(
    private val client: HttpClient,
    private val appValues: AppValues
) {
    // Note: Maps API Key - 576b91a0-ac5c-421a-a932-38cbe1d4c633
    companion object {
        const val BASE_URL = "http://45.155.207.232:1478" // Backend API for places, reviews, routes
        const val MAPS_API_KEY = "576b91a0-ac5c-421a-a932-38cbe1d4c633" // Maps API for displaying markers
    }

    suspend fun <T> safeRequest(block: suspend HttpClient.() -> T): Result<T> {
        return try {
            android.util.Log.d("ApiClient", "Making API request")
            val result = client.block()
            android.util.Log.d("ApiClient", "API request successful")
            Result.success(result)
        } catch (e: Exception) {
            android.util.Log.e("ApiClient", "API request failed", e)
            e.printStackTrace()
            Result.failure(e)
        }
    }

    fun updateToken(newToken: String) {
        appValues.authToken.set(newToken)
    }

    fun clearToken() {
        appValues.authToken.set(null)
    }
}
