package app.what.investtravel.data.remote


import app.what.investtravel.data.local.settings.AppValues
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType


// Accessible Places Service
class PlacesService(
    private val apiClient: ApiClient,
    private val appValues: AppValues
) {
    // Search Accessible Places with filters
    suspend fun searchPlaces(
        latitude: Double,
        longitude: Double,
        radiusKm: Float = 5f,
        category: String? = null,
        wheelchairAccessible: Boolean? = null,
        page: Int = 1,
        size: Int = 10
    ): Result<AccessiblePlacesResponse> {
        return apiClient.safeRequest {
            get(ApiClient.BASE_URL + "/places/search") {
                parameter("token", appValues.authToken.get())
                parameter("latitude", latitude)
                parameter("longitude", longitude)
                parameter("radius_km", radiusKm)
                category?.let { parameter("category", it) }
                wheelchairAccessible?.let { parameter("wheelchair_accessible", it) }
                parameter("page", page)
                parameter("size", size)
            }.body()
        }
    }

    // Get Place by ID
    suspend fun getPlaceById(placeId: Int): Result<AccessiblePlaceResponse> {
        return apiClient.safeRequest {
            get(ApiClient.BASE_URL + "/places/$placeId") {
                parameter("token", appValues.authToken.get())
            }.body()
        }
    }

    // Get Places by Category
    suspend fun getPlacesByCategory(
        category: String,
        page: Int = 1,
        size: Int = 10
    ): Result<AccessiblePlacesResponse> {
        return apiClient.safeRequest {
            get(ApiClient.BASE_URL + "/places/category/$category") {
                parameter("page", page)
                parameter("size", size)
                parameter("token", appValues.authToken.get())
            }.body()
        }
    }

    // Apply Accessibility Filters
    suspend fun filterPlaces(
        filters: AccessibilityFilterRequest,
        page: Int = 1,
        size: Int = 10
    ): Result<AccessiblePlacesResponse> {
        return apiClient.safeRequest {
            post(ApiClient.BASE_URL + "/places/filter") {
                parameter("token", appValues.authToken.get())
                parameter("page", page)
                parameter("size", size)
                contentType(ContentType.Application.Json)
                setBody(filters)
            }.body()
        }
    }

    // Save Place to Favorites
    suspend fun savePlace(saveRequest: SavedPlaceRequest): Result<SavedPlaceResponse> {
        return apiClient.safeRequest {
            post(ApiClient.BASE_URL + "/places/save") {
                parameter("token", appValues.authToken.get())
                contentType(ContentType.Application.Json)
                setBody(saveRequest)
            }.body()
        }
    }

    // Get Saved Places
    suspend fun getSavedPlaces(
        page: Int = 1,
        size: Int = 10
    ): Result<List<SavedPlaceResponse>> {
        return apiClient.safeRequest {
            get(ApiClient.BASE_URL + "/places/saved") {
                parameter("token", appValues.authToken.get())
                parameter("page", page)
                parameter("size", size)
            }.body()
        }
    }

    // Remove Saved Place
    suspend fun removeSavedPlace(placeId: Int): Result<Map<String, Any>> {
        return apiClient.safeRequest {
            delete(ApiClient.BASE_URL + "/places/saved/$placeId") {
                parameter("token", appValues.authToken.get())
            }.body()
        }
    }
}

class AuthService(
    private val apiClient: ApiClient,
    private val appValues: AppValues
) {
    suspend fun login(loginRequest: LoginRequest): Result<TokenResponse> {
        return apiClient.safeRequest {
            post(ApiClient.BASE_URL + "/auth/login/") {
                parameter("token", appValues.authToken.get())
                contentType(ContentType.Application.Json)
                setBody(loginRequest)
            }.body()
        }
    }
}

// Users Service
class UsersService(
    private val apiClient: ApiClient,
    private val appValues: AppValues
) {
    suspend fun createUser(userCreate: UserCreate): Result<UserCreate> {
        return apiClient.safeRequest {
            post(ApiClient.BASE_URL + "/users/") {
                parameter("token", appValues.authToken.get())
                contentType(ContentType.Application.Json)
                setBody(userCreate)
            }.body()
        }
    }

    suspend fun getUsers(): Result<List<UserGet>> {
        return apiClient.safeRequest {
            get(ApiClient.BASE_URL + "/users/").body()
        }
    }

    suspend fun getUser(userId: Int): Result<UserCreate> {
        return apiClient.safeRequest {
            get(ApiClient.BASE_URL + "/users/$userId") {
                parameter("token", appValues.authToken.get())
            }.body()
        }
    }

    suspend fun updateUser(userId: Int, userCreate: UserCreate): Result<UserCreate> {
        return apiClient.safeRequest {
            put(ApiClient.BASE_URL + "/users/$userId") {
                parameter("token", appValues.authToken.get())
                contentType(ContentType.Application.Json)
                setBody(userCreate)
            }.body()
        }
    }

    suspend fun deleteUser(userId: Int): Result<Unit> {
        return apiClient.safeRequest {
            delete(ApiClient.BASE_URL + "/users/$userId") {
                parameter("token", appValues.authToken.get())
            }
        }
    }

    suspend fun getCurrentUser(): Result<UserMoreModel> {
        return apiClient.safeRequest {
            get("/users/user/me") {
                parameter("token", appValues.authToken.get())
            }.body()
        }
    }
}

// Routes Service
// Reviews Service for Accessibility Feedback
class ReviewsService(
    private val apiClient: ApiClient,
    private val appValues: AppValues
) {
    // Create Review with Accessibility Feedback
    suspend fun createReview(reviewRequest: ReviewCreateRequest): Result<ReviewResponse> {
        return apiClient.safeRequest {
            post(ApiClient.BASE_URL + "/reviews/create") {
                parameter("token", appValues.authToken.get())
                contentType(ContentType.Application.Json)
                setBody(reviewRequest)
            }.body()
        }
    }

    // Get Reviews for a Place
    suspend fun getPlaceReviews(
        placeId: Int,
        page: Int = 1,
        size: Int = 10
    ): Result<ReviewsResponse> {
        return apiClient.safeRequest {
            get(ApiClient.BASE_URL + "/reviews/place/$placeId") {
                parameter("token", appValues.authToken.get())
                parameter("page", page)
                parameter("size", size)
            }.body()
        }
    }

    // Get User's Reviews
    suspend fun getUserReviews(
        page: Int = 1,
        size: Int = 10
    ): Result<ReviewsResponse> {
        return apiClient.safeRequest {
            get(ApiClient.BASE_URL + "/reviews/my") {
                parameter("token", appValues.authToken.get())
                parameter("page", page)
                parameter("size", size)
            }.body()
        }
    }

    // Get Review by ID
    suspend fun getReviewById(reviewId: Int): Result<ReviewResponse> {
        return apiClient.safeRequest {
            get(ApiClient.BASE_URL + "/reviews/$reviewId") {
                parameter("token", appValues.authToken.get())
            }.body()
        }
    }

    // Delete Review
    suspend fun deleteReview(reviewId: Int): Result<Map<String, Any>> {
        return apiClient.safeRequest {
            delete(ApiClient.BASE_URL + "/reviews/$reviewId") {
                parameter("token", appValues.authToken.get())
            }.body()
        }
    }
}

// Accessible Routes Service for Navigation
class AccessibleRoutesService(
    private val apiClient: ApiClient,
    private val appValues: AppValues
) {
    // Create Accessible Route
    suspend fun createRoute(routeName: String, routeDescription: String?): Result<AccessibleRouteResponse> {
        return apiClient.safeRequest {
            post(ApiClient.BASE_URL + "/routes/create") {
                parameter("token", appValues.authToken.get())
                parameter("name", routeName)
                routeDescription?.let { parameter("description", it) }
            }.body()
        }
    }

    // Get Routes with Accessibility Tags
    suspend fun getRoutes(
        page: Int = 1,
        size: Int = 10,
        difficultyLevel: String? = null,
        wheelchairFriendly: Boolean? = null
    ): Result<AccessibleRoutesResponse> {
        return apiClient.safeRequest {
            get(ApiClient.BASE_URL + "/routes/accessible") {
                parameter("token", appValues.authToken.get())
                parameter("page", page)
                parameter("size", size)
                difficultyLevel?.let { parameter("difficulty_level", it) }
                wheelchairFriendly?.let { parameter("wheelchair_friendly", it) }
            }.body()
        }
    }

    // Get Route by ID
    suspend fun getRouteById(routeId: Int): Result<AccessibleRouteResponse> {
        return apiClient.safeRequest {
            get(ApiClient.BASE_URL + "/routes/$routeId") {
                parameter("token", appValues.authToken.get())
            }.body()
        }
    }

    // Add Place to Route
    suspend fun addPlaceToRoute(routeId: Int, placeId: Int): Result<AccessibleRouteResponse> {
        return apiClient.safeRequest {
            post(ApiClient.BASE_URL + "/routes/$routeId/add-place") {
                parameter("token", appValues.authToken.get())
                parameter("place_id", placeId)
            }.body()
        }
    }

    // Remove Place from Route
    suspend fun removePlaceFromRoute(routeId: Int, placeId: Int): Result<AccessibleRouteResponse> {
        return apiClient.safeRequest {
            delete(ApiClient.BASE_URL + "/routes/$routeId/remove-place/$placeId") {
                parameter("token", appValues.authToken.get())
            }.body()
        }
    }

    // Get Routes Nearby User
    suspend fun getRoutesNearby(
        latitude: Double,
        longitude: Double,
        radiusKm: Float = 10f
    ): Result<AccessibleRoutesResponse> {
        return apiClient.safeRequest {
            get(ApiClient.BASE_URL + "/routes/nearby") {
                parameter("token", appValues.authToken.get())
                parameter("latitude", latitude)
                parameter("longitude", longitude)
                parameter("radius_km", radiusKm)
            }.body()
        }
    }

    // Filter Routes by Accessibility Criteria
    suspend fun filterRoutes(
        filters: RouteFilterRequest,
        page: Int = 1,
        size: Int = 10
    ): Result<AccessibleRoutesResponse> {
        return apiClient.safeRequest {
            post(ApiClient.BASE_URL + "/routes/filter") {
                parameter("token", appValues.authToken.get())
                parameter("page", page)
                parameter("size", size)
                contentType(ContentType.Application.Json)
                setBody(filters)
            }.body()
        }
    }

    // Delete Route
    suspend fun deleteRoute(routeId: Int): Result<Map<String, Any>> {
        return apiClient.safeRequest {
            delete(ApiClient.BASE_URL + "/routes/$routeId") {
                parameter("token", appValues.authToken.get())
            }.body()
        }
    }
}
// AI Service with Mock Implementations for Accessibility Analysis
class AiService(
    private val apiClient: ApiClient,
    private val appValues: AppValues
) {
    // Mock: Analyze accessibility feedback from text
    suspend fun analyzeAccessibilityFeedback(data: AiAccessibilityAnalysisRequest): Result<AiAccessibilityAnalysisResponse> {
        return try {
            // Mock response - in production would call AI API
            val mockResponse = AiAccessibilityAnalysisResponse(
                analysis = "This place shows good accessibility features for wheelchair users. Staff appears helpful.",
                success = true,
                confidence = 0.85f
            )
            Result.success(mockResponse)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Mock: Generate optimal accessible route based on user preferences
    suspend fun generateAccessibleRoute(data: AiRouteGenerationRequest): Result<AiRouteGenerationResponse> {
        return try {
            // Mock response - in production would call AI route generation service
            val mockResponse = AiRouteGenerationResponse(
                success = true,
                route = null,
                aiRecommendations = "Based on accessibility requirements, consider starting at cafes with wheelchair access, then moving to accessible parks.",
                errorMessage = null
            )
            Result.success(mockResponse)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Mock: Generate recommendations for accessibility improvements
    suspend fun generateAccessibilityRecommendations(placeId: Int): Result<Map<String, String>> {
        return try {
            val recommendations = mapOf(
                "suggestion_1" to "Add a ramp at the entrance for wheelchair access",
                "suggestion_2" to "Install an accessible toilet on the ground floor",
                "suggestion_3" to "Train staff on accessibility best practices"
            )
            Result.success(recommendations)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
