package app.what.investtravel.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


// Accessible Places Models
@Serializable
data class AccessiblePlacesResponse(
    @SerialName("places") val places: List<AccessiblePlaceResponse>,
    @SerialName("total") val total: Int,
    @SerialName("page") val page: Int,
    @SerialName("size") val size: Int,
    @SerialName("total_pages") val totalPages: Int
)

@Serializable
data class AccessiblePlaceResponse(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("description") val description: String? = null,
    @SerialName("address") val address: String,
    @SerialName("city") val city: String,
    @SerialName("latitude") val latitude: Double,
    @SerialName("longitude") val longitude: Double,
    @SerialName("category") val category: String,
    @SerialName("accessibility_info") val accessibilityInfo: AccessibilityInfo,
    @SerialName("phone") val phone: String? = null,
    @SerialName("email") val email: String? = null,
    @SerialName("website") val website: String? = null,
    @SerialName("rating") val rating: Float = 0f,
    @SerialName("review_count") val reviewCount: Int = 0,
    @SerialName("images") val images: List<String>? = null,
    @SerialName("status") val status: String = "active",
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String? = null
)

@Serializable
data class AccessibilityInfo(
    @SerialName("wheelchair_accessible") val wheelchairAccessible: Boolean = false,
    @SerialName("elevator") val elevator: Boolean = false,
    @SerialName("accessible_toilet") val accessibleToilet: Boolean = false,
    @SerialName("hearing_loop") val hearingLoop: Boolean = false,
    @SerialName("visual_guides") val visualGuides: Boolean = false,
    @SerialName("parking_accessible") val parkingAccessible: Boolean = false,
    @SerialName("pet_friendly") val petFriendly: Boolean = false,
    @SerialName("service_animals_allowed") val serviceAnimalsAllowed: Boolean = false,
    @SerialName("staff_trained") val staffTrained: Boolean = false,
    @SerialName("notes") val notes: String? = null
)

@Serializable
enum class PlaceCategory {
    @SerialName("cafe")
    CAFE,

    @SerialName("cinema")
    CINEMA,

    @SerialName("restaurant")
    RESTAURANT,

    @SerialName("park")
    PARK,

    @SerialName("transport")
    TRANSPORT,

    @SerialName("theater")
    THEATER,

    @SerialName("shop")
    SHOP,

    @SerialName("medical")
    MEDICAL,

    @SerialName("other")
    OTHER
}

// Reviews Models
@Serializable
data class ReviewsResponse(
    @SerialName("reviews") val reviews: List<ReviewResponse>,
    @SerialName("total") val total: Int,
    @SerialName("page") val page: Int,
    @SerialName("size") val size: Int
)

@Serializable
data class ReviewResponse(
    @SerialName("id") val id: Int,
    @SerialName("place_id") val placeId: Int,
    @SerialName("user_id") val userId: Int,
    @SerialName("user_name") val userName: String,
    @SerialName("rating") val rating: Float,
    @SerialName("text") val text: String,
    @SerialName("accessibility_feedback") val accessibilityFeedback: AccessibilityFeedback,
    @SerialName("photos") val photos: List<String>? = null,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String? = null
)

@Serializable
data class ReviewCreateRequest(
    @SerialName("place_id") val placeId: Int,
    @SerialName("rating") val rating: Float,
    @SerialName("text") val text: String,
    @SerialName("accessibility_feedback") val accessibilityFeedback: AccessibilityFeedback
)

@Serializable
data class AccessibilityFeedback(
    @SerialName("difficulty_level") val difficultyLevel: String = "easy", // easy, moderate, hard
    @SerialName("issues_encountered") val issuesEncountered: List<String>? = null,
    @SerialName("recommendations") val recommendations: String? = null,
    @SerialName("staff_helpfulness") val staffHelpfulness: Int = 3, // 1-5
    @SerialName("visited_date") val visitedDate: String? = null
)

// Accessible Routes Models
@Serializable
data class AccessibleRoutesResponse(
    @SerialName("routes") val routes: List<AccessibleRouteResponse>,
    @SerialName("total") val total: Int,
    @SerialName("page") val page: Int,
    @SerialName("size") val size: Int
)

@Serializable
data class AccessibleRouteResponse(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("description") val description: String? = null,
    @SerialName("start_point") val startPoint: RoutePoint,
    @SerialName("end_point") val endPoint: RoutePoint,
    @SerialName("waypoints") val waypoints: List<RoutePoint>? = null,
    @SerialName("total_distance_km") val totalDistanceKm: Float,
    @SerialName("estimated_duration_minutes") val estimatedDurationMinutes: Int,
    @SerialName("accessibility_tags") val accessibilityTags: List<String>,
    @SerialName("difficulty_level") val difficultyLevel: String = "easy",
    @SerialName("wheelchair_friendly") val wheelchairFriendly: Boolean = false,
    @SerialName("rest_points") val restPoints: List<AccessiblePlaceResponse>? = null,
    @SerialName("created_by") val createdBy: Int,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String? = null
)

@Serializable
data class RoutePoint(
    @SerialName("latitude") val latitude: Double,
    @SerialName("longitude") val longitude: Double,
    @SerialName("name") val name: String? = null,
    @SerialName("notes") val notes: String? = null
)

@Serializable
data class RouteFilterRequest(
    @SerialName("accessibility_tags") val accessibilityTags: List<String>? = null,
    @SerialName("wheelchair_friendly") val wheelchairFriendly: Boolean? = null,
    @SerialName("difficulty_level") val difficultyLevel: String? = null,
    @SerialName("categories") val categories: List<String>? = null
)

@Serializable
data class LoginRequest(
    @SerialName("login") val login: String,
    @SerialName("password") val password: String
)

@Serializable
data class TokenResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("token_type") val tokenType: String = "bearer"
)

// User Models
@Serializable
data class UserCreate(
    @SerialName("login") val login: String,
    @SerialName("role_id") val roleId: Int? = 0,
    @SerialName("name") val name: String? = null,
    @SerialName("email") val email: String? = null,
    @SerialName("password") val password: String = ""
)

@Serializable
data class UserGet(
    @SerialName("id") val id: Int,
    @SerialName("login") val login: String,
    @SerialName("role") val role: Role,
    @SerialName("name") val name: String?
)

@Serializable
data class UserMoreModel(
    @SerialName("id") val id: Int,
    @SerialName("login") val login: String,
    @SerialName("email") val email: String?
)

// Role Models
@Serializable
data class Role(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String
)

@Serializable
data class RoleCreate(
    @SerialName("name") val name: String
)

@Serializable
data class RoleGet(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String
)

// Accessibility Filter Models
@Serializable
data class AccessibilityFilterRequest(
    @SerialName("wheelchair_accessible") val wheelchairAccessible: Boolean? = null,
    @SerialName("elevator") val elevator: Boolean? = null,
    @SerialName("accessible_toilet") val accessibleToilet: Boolean? = null,
    @SerialName("hearing_loop") val hearingLoop: Boolean? = null,
    @SerialName("visual_guides") val visualGuides: Boolean? = null,
    @SerialName("parking_accessible") val parkingAccessible: Boolean? = null,
    @SerialName("pet_friendly") val petFriendly: Boolean? = null,
    @SerialName("service_animals_allowed") val serviceAnimalsAllowed: Boolean? = null,
    @SerialName("categories") val categories: List<String>? = null,
    @SerialName("latitude") val latitude: Double? = null,
    @SerialName("longitude") val longitude: Double? = null,
    @SerialName("radius_km") val radiusKm: Float = 5f
)

// Favorites and Saved Places
@Serializable
data class SavedPlaceRequest(
    @SerialName("place_id") val placeId: Int,
    @SerialName("notes") val notes: String? = null
)

@Serializable
data class SavedPlaceResponse(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: Int,
    @SerialName("place_id") val placeId: Int,
    @SerialName("place") val place: AccessiblePlaceResponse,
    @SerialName("notes") val notes: String? = null,
    @SerialName("saved_at") val savedAt: String
)

// Common Models
@Serializable
data class ErrorResponse(
    @SerialName("detail") val detail: List<ValidationError>
)

@Serializable
data class ValidationError(
    @SerialName("loc") val location: List<String>, // TODO: List<Any>
    @SerialName("msg") val message: String,
    @SerialName("type") val type: String
)

// AI Stubs (mock implementations)
@Serializable
data class AiAccessibilityAnalysisRequest(
    @SerialName("text") val text: String,
    @SerialName("context") val context: String? = null
)

@Serializable
data class AiAccessibilityAnalysisResponse(
    @SerialName("analysis") val analysis: String,
    @SerialName("success") val success: Boolean,
    @SerialName("confidence") val confidence: Float? = null
)

@Serializable
data class AiRouteGenerationRequest(
    @SerialName("user_preferences") val userPreferences: String,
    @SerialName("accessibility_requirements") val accessibilityRequirements: List<String>,
    @SerialName("start_latitude") val startLatitude: Double,
    @SerialName("start_longitude") val startLongitude: Double,
    @SerialName("radius_km") val radiusKm: Float = 10f
)

@Serializable
data class AiRouteGenerationResponse(
    @SerialName("success") val success: Boolean,
    @SerialName("route") val route: AccessibleRouteResponse?,
    @SerialName("ai_recommendations") val aiRecommendations: String?,
    @SerialName("error_message") val errorMessage: String?
)
