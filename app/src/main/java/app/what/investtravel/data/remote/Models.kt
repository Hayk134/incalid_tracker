package app.what.investtravel.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// === Enums ===

@Serializable
enum class DisabilityType(val displayName: String) {
    @SerialName("wheelchair") WHEELCHAIR("Колясочники"),
    @SerialName("visually_impaired") VISUALLY_IMPAIRED("Слабовидящие"),
    @SerialName("hearing_impaired") HEARING_IMPAIRED("Слабослышащие"),
    @SerialName("limited_mobility") LIMITED_MOBILITY("Ограниченная подвижность"),
    @SerialName("cognitive") COGNITIVE("Когнитивные нарушения"),
    @SerialName("elderly") ELDERLY("Пожилые"),
    @SerialName("stroller") STROLLER("С колясками");

    companion object {
        fun fromString(s: String): DisabilityType? = entries.find { it.name.equals(s, true) || s == it.displayName }
        fun fromCsv(csv: String): List<DisabilityType> = csv.split(",").mapNotNull { fromString(it.trim()) }
    }
}

@Serializable
enum class AccessibilityTag(val displayName: String) {
    @SerialName("ramp") RAMP("Пандус"),
    @SerialName("wide_door") WIDE_DOOR("Широкие двери"),
    @SerialName("elevator") ELEVATOR("Лифт"),
    @SerialName("tactile_tiles") TACTILE_TILES("Тактильная плитка"),
    @SerialName("audio_guide") AUDIO_GUIDE("Аудиогид"),
    @SerialName("vibro_signal") VIBRO_SIGNAL("Вибросигнал"),
    @SerialName("subtitles") SUBTITLES("Субтитры"),
    @SerialName("handrails") HANDRAILS("Поручни"),
    @SerialName("parking_disabled") PARKING_DISABLED("Парковка для инвалидов"),
    @SerialName("low_floor_transport") LOW_FLOOR_TRANSPORT("Низкопольный транспорт"),
    @SerialName("bench") BENCH("Скамейки"),
    @SerialName("rest_area") REST_AREA("Зона отдыха"),
    @SerialName("simple_navigation") SIMPLE_NAVIGATION("Простая навигация"),
    @SerialName("braille") BRAILLE("Шрифт Брайля");

    companion object {
        fun fromString(s: String): AccessibilityTag? = entries.find { it.name.equals(s, true) || s == it.displayName }
        fun fromCsv(csv: String): List<AccessibilityTag> = csv.split(",").mapNotNull { fromString(it.trim()) }
    }
}

@Serializable
enum class PlaceCategory(val displayName: String) {
    @SerialName("cafe") CAFE("Кафе"),
    @SerialName("restaurant") RESTAURANT("Рестораны"),
    @SerialName("shop") SHOP("Магазины"),
    @SerialName("clinic") CLINIC("Поликлиники"),
    @SerialName("theater") THEATER("Театры"),
    @SerialName("cinema") CINEMA("Кинотеатры"),
    @SerialName("transport_stop") TRANSPORT_STOP("Остановки"),
    @SerialName("park") PARK("Парки"),
    @SerialName("museum") MUSEUM("Музеи"),
    @SerialName("pharmacy") PHARMACY("Аптеки"),
    @SerialName("bank") BANK("Банки"),
    @SerialName("government") GOVERNMENT("Госуслуги"),
    @SerialName("sport") SPORT("Спорт"),
    @SerialName("library") LIBRARY("Библиотеки");

    companion object {
        fun fromString(s: String): PlaceCategory? = entries.find { it.name.equals(s, true) }
    }
}

// === Places (replaces Hotels) ===

@Serializable
data class PlaceListResponse(
    @SerialName("places") val places: List<PlaceResponse>,
    @SerialName("total") val total: Int,
    @SerialName("page") val page: Int,
    @SerialName("size") val size: Int
)

@Serializable
data class PlaceResponse(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("description") val description: String? = null,
    @SerialName("category") val category: String,
    @SerialName("address") val address: String? = null,
    @SerialName("latitude") val latitude: Double,
    @SerialName("longitude") val longitude: Double,
    @SerialName("phone") val phone: String? = null,
    @SerialName("website") val website: String? = null,
    @SerialName("images") val images: String? = null,
    @SerialName("accessibility_tags") val accessibilityTags: String? = null,
    @SerialName("disability_types") val disabilityTypes: String? = null,
    @SerialName("rating") val rating: Float = 0f,
    @SerialName("reviews_count") val reviewsCount: Int = 0,
    @SerialName("status") val status: String? = "active",
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("reviews") val reviews: List<ReviewResponse>? = null
)

// === Markers ===

@Serializable
data class MarkerListResponse(
    @SerialName("markers") val markers: List<MarkerResponse>,
    @SerialName("total") val total: Int,
    @SerialName("page") val page: Int,
    @SerialName("size") val size: Int
)

@Serializable
data class MarkerResponse(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: Int,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String? = null,
    @SerialName("latitude") val latitude: Double,
    @SerialName("longitude") val longitude: Double,
    @SerialName("marker_type") val markerType: String? = "general",
    @SerialName("accessibility_tags") val accessibilityTags: String? = null,
    @SerialName("photos") val photos: String? = null,
    @SerialName("votes_up") val votesUp: Int = 0,
    @SerialName("votes_down") val votesDown: Int = 0,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("user_name") val userName: String? = null,
    @SerialName("user_login") val userLogin: String? = null,
    @SerialName("reviews") val reviews: List<ReviewResponse>? = null
)

@Serializable
data class MarkerCreateRequest(
    @SerialName("title") val title: String,
    @SerialName("description") val description: String? = null,
    @SerialName("latitude") val latitude: Double,
    @SerialName("longitude") val longitude: Double,
    @SerialName("marker_type") val markerType: String = "general",
    @SerialName("accessibility_tags") val accessibilityTags: String = "",
    @SerialName("photos") val photos: String = "[]"
)

// === Reviews ===

@Serializable
data class ReviewResponse(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: Int,
    @SerialName("place_id") val placeId: Int? = null,
    @SerialName("marker_id") val markerId: Int? = null,
    @SerialName("text") val text: String? = null,
    @SerialName("rating") val rating: Int = 0,
    @SerialName("photos") val photos: String? = null,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("user_name") val userName: String? = null,
    @SerialName("user_login") val userLogin: String? = null
)

@Serializable
data class ReviewCreateRequest(
    @SerialName("place_id") val placeId: Int? = null,
    @SerialName("marker_id") val markerId: Int? = null,
    @SerialName("text") val text: String,
    @SerialName("rating") val rating: Int,
    @SerialName("photos") val photos: String = "[]"
)

// === Auth ===

@Serializable
data class LoginRequest(
    @SerialName("login") val login: String,
    @SerialName("password") val password: String
)

@Serializable
data class LoginResponse(
    @SerialName("token") val token: String,
    @SerialName("user") val user: UserResponse? = null
)

@Serializable
data class TokenResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("token_type") val tokenType: String = "bearer"
)

@Serializable
data class RegisterRequest(
    @SerialName("login") val login: String,
    @SerialName("email") val email: String,
    @SerialName("password") val password: String,
    @SerialName("name") val name: String? = null,
    @SerialName("disability_types") val disabilityTypes: String? = null
)

// === Users ===

@Serializable
data class UserCreate(
    @SerialName("login") val login: String,
    @SerialName("role_id") val roleId: Int? = 0,
    @SerialName("name") val name: String? = null,
    @SerialName("email") val email: String? = null,
    @SerialName("password") val password: String = ""
)

@Serializable
data class UserResponse(
    @SerialName("id") val id: Int,
    @SerialName("login") val login: String,
    @SerialName("name") val name: String? = null,
    @SerialName("email") val email: String? = null,
    @SerialName("role") val role: String? = null,
    @SerialName("disability_types") val disabilityTypes: String? = null,
    @SerialName("created_at") val createdAt: String? = null
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

// === Routes (Accessible) ===

@Serializable
data class AccessibleRouteRequest(
    @SerialName("name") val name: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("start_lat") val startLat: Double,
    @SerialName("start_lon") val startLon: Double,
    @SerialName("end_lat") val endLat: Double? = null,
    @SerialName("end_lon") val endLon: Double? = null,
    @SerialName("disability_types") val disabilityTypes: String = "",
    @SerialName("avoid_stairs") val avoidStairs: Boolean = true,
    @SerialName("prefer_ramps") val preferRamps: Boolean = true,
    @SerialName("max_distance_km") val maxDistanceKm: Double = 10.0
)

@Serializable
data class RouteResponse(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("description") val description: String?,
    @SerialName("disability_types") val disabilityTypes: String? = null,
    @SerialName("total_duration_hours") val totalDurationHours: Float = 0f,
    @SerialName("total_distance_km") val totalDistanceKm: Float = 0f,
    @SerialName("points") val points: List<RoutePointResponse> = emptyList(),
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("user_id") val userId: Int? = null
)

@Serializable
data class RoutePointResponse(
    @SerialName("order") val order: Int,
    @SerialName("name") val name: String,
    @SerialName("latitude") val latitude: Double,
    @SerialName("longitude") val longitude: Double,
    @SerialName("accessibility_tags") val accessibilityTags: String? = null,
    @SerialName("category") val category: String? = null,
    @SerialName("address") val address: String? = null
)

// === Common ===

@Serializable
data class ErrorResponse(
    @SerialName("detail") val detail: List<ValidationError>
)

@Serializable
data class ValidationError(
    @SerialName("loc") val location: List<String>,
    @SerialName("msg") val message: String,
    @SerialName("type") val type: String
)

// === AI Stub ===

@Serializable
data class GenerateCommentRequest(
    @SerialName("text") val text: String
)

@Serializable
data class GenerateCommentResponse(
    @SerialName("comment") val comment: String,
    @SerialName("success") val success: Boolean,
)

@Serializable
data class AiRouteRequest(
    @SerialName("user_preferences") val userPreferences: String,
    @SerialName("trip_duration_hours") val tripDurationHours: Int,
    @SerialName("start_date") val startDate: String,
    @SerialName("end_date") val endDate: String
)

@Serializable
data class AiRouteResponse(
    @SerialName("success") val success: Boolean,
    @SerialName("route") val route: RouteResponse? = null,
    @SerialName("ai_recommendations") val aiRecommendations: String?,
    @SerialName("error_message") val errorMessage: String?
)

// === Reference ===

@Serializable
data class CategoryInfo(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("icon") val icon: String
)

@Serializable
data class VoteRequest(
    @SerialName("vote") val vote: String // "up" or "down"
)
