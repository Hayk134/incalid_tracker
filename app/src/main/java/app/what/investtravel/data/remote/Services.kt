package app.what.investtravel.data.remote

import app.what.investtravel.data.local.settings.AppValues
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType


// === Places Service (replaces HotelsService) ===

class PlacesService(
    private val apiClient: ApiClient,
    private val appValues: AppValues
) {
    suspend fun searchPlaces(
        category: String? = null,
        disabilityTypes: String? = null,
        tags: String? = null,
        search: String? = null,
        page: Int = 1,
        size: Int = 20
    ): Result<PlaceListResponse> {
        return apiClient.safeRequest {
            get(ApiClient.BASE_URL + "/places") {
                header("Authorization", "Bearer ${appValues.authToken.get()}")
                category?.let { parameter("category", it) }
                disabilityTypes?.let { parameter("disability_types", it) }
                tags?.let { parameter("tags", it) }
                search?.let { parameter("search", it) }
                parameter("page", page)
                parameter("size", size)
            }.body()
        }
    }

    suspend fun getPlace(placeId: Int): Result<PlaceResponse> {
        return apiClient.safeRequest {
            get(ApiClient.BASE_URL + "/places/$placeId") {
                header("Authorization", "Bearer ${appValues.authToken.get()}")
            }.body()
        }
    }

    suspend fun getCategories(): Result<List<CategoryInfo>> {
        return apiClient.safeRequest {
            get(ApiClient.BASE_URL + "/places/categories").body()
        }
    }

    suspend fun createPlace(
        name: String,
        description: String,
        category: String,
        address: String,
        latitude: Double,
        longitude: Double,
        accessibilityTags: String,
        disabilityTypes: String
    ): Result<PlaceResponse> {
        return apiClient.safeRequest {
            post(ApiClient.BASE_URL + "/places") {
                header("Authorization", "Bearer ${appValues.authToken.get()}")
                contentType(ContentType.Application.Json)
                setBody(mapOf(
                    "name" to name,
                    "description" to description,
                    "category" to category,
                    "address" to address,
                    "latitude" to latitude.toString(),
                    "longitude" to longitude.toString(),
                    "accessibility_tags" to accessibilityTags,
                    "disability_types" to disabilityTypes
                ))
            }.body()
        }
    }
}

// === Markers Service ===

class MarkersService(
    private val apiClient: ApiClient,
    private val appValues: AppValues
) {
    suspend fun getMarkers(
        lat: Double? = null,
        lon: Double? = null,
        radius: Double? = null,
        page: Int = 1,
        size: Int = 50
    ): Result<MarkerListResponse> {
        return apiClient.safeRequest {
            get(ApiClient.BASE_URL + "/markers") {
                header("Authorization", "Bearer ${appValues.authToken.get()}")
                lat?.let { parameter("lat", it) }
                lon?.let { parameter("lon", it) }
                radius?.let { parameter("radius", it) }
                parameter("page", page)
                parameter("size", size)
            }.body()
        }
    }

    suspend fun getMarker(markerId: Int): Result<MarkerResponse> {
        return apiClient.safeRequest {
            get(ApiClient.BASE_URL + "/markers/$markerId") {
                header("Authorization", "Bearer ${appValues.authToken.get()}")
            }.body()
        }
    }

    suspend fun createMarker(request: MarkerCreateRequest): Result<MarkerResponse> {
        return apiClient.safeRequest {
            post(ApiClient.BASE_URL + "/markers") {
                header("Authorization", "Bearer ${appValues.authToken.get()}")
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body()
        }
    }

    suspend fun voteOnMarker(markerId: Int, vote: String): Result<MarkerResponse> {
        return apiClient.safeRequest {
            put(ApiClient.BASE_URL + "/markers/$markerId/vote") {
                header("Authorization", "Bearer ${appValues.authToken.get()}")
                contentType(ContentType.Application.Json)
                setBody(VoteRequest(vote))
            }.body()
        }
    }
}

// === Reviews Service ===

class ReviewsService(
    private val apiClient: ApiClient,
    private val appValues: AppValues
) {
    suspend fun getReviewsForPlace(placeId: Int): Result<List<ReviewResponse>> {
        return apiClient.safeRequest {
            get(ApiClient.BASE_URL + "/reviews/place/$placeId") {
                header("Authorization", "Bearer ${appValues.authToken.get()}")
            }.body()
        }
    }

    suspend fun getReviewsForMarker(markerId: Int): Result<List<ReviewResponse>> {
        return apiClient.safeRequest {
            get(ApiClient.BASE_URL + "/reviews/marker/$markerId") {
                header("Authorization", "Bearer ${appValues.authToken.get()}")
            }.body()
        }
    }

    suspend fun createReview(request: ReviewCreateRequest): Result<ReviewResponse> {
        return apiClient.safeRequest {
            post(ApiClient.BASE_URL + "/reviews") {
                header("Authorization", "Bearer ${appValues.authToken.get()}")
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body()
        }
    }
}

// === Auth Service ===

class AuthService(
    private val apiClient: ApiClient,
    private val appValues: AppValues
) {
    suspend fun login(loginRequest: LoginRequest): Result<LoginResponse> {
        return apiClient.safeRequest {
            post(ApiClient.BASE_URL + "/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(loginRequest)
            }.body()
        }
    }

    suspend fun register(registerRequest: RegisterRequest): Result<LoginResponse> {
        return apiClient.safeRequest {
            post(ApiClient.BASE_URL + "/auth/register") {
                contentType(ContentType.Application.Json)
                setBody(registerRequest)
            }.body()
        }
    }
}

// === Users Service ===

class UsersService(
    private val apiClient: ApiClient,
    private val appValues: AppValues
) {
    suspend fun createUser(userCreate: UserCreate): Result<UserCreate> {
        return apiClient.safeRequest {
            post(ApiClient.BASE_URL + "/users/") {
                header("Authorization", "Bearer ${appValues.authToken.get()}")
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
                header("Authorization", "Bearer ${appValues.authToken.get()}")
            }.body()
        }
    }

    suspend fun updateUser(userId: Int, userCreate: UserCreate): Result<UserCreate> {
        return apiClient.safeRequest {
            put(ApiClient.BASE_URL + "/users/$userId") {
                header("Authorization", "Bearer ${appValues.authToken.get()}")
                contentType(ContentType.Application.Json)
                setBody(userCreate)
            }.body()
        }
    }

    suspend fun deleteUser(userId: Int): Result<Unit> {
        return apiClient.safeRequest {
            delete(ApiClient.BASE_URL + "/users/$userId") {
                header("Authorization", "Bearer ${appValues.authToken.get()}")
            }
        }
    }

    suspend fun getCurrentUser(): Result<UserResponse> {
        return apiClient.safeRequest {
            get(ApiClient.BASE_URL + "/users/me") {
                header("Authorization", "Bearer ${appValues.authToken.get()}")
            }.body()
        }
    }

    suspend fun updateCurrentUser(
        name: String? = null,
        email: String? = null,
        disabilityTypes: String? = null
    ): Result<UserResponse> {
        return apiClient.safeRequest {
            put(ApiClient.BASE_URL + "/users/me") {
                header("Authorization", "Bearer ${appValues.authToken.get()}")
                contentType(ContentType.Application.Json)
                setBody(mapOf(
                    "name" to name,
                    "email" to email,
                    "disability_types" to disabilityTypes
                ))
            }.body()
        }
    }
}

// === Routes Service (Accessible) ===

class RoutesService(
    private val apiClient: ApiClient,
    private val appValues: AppValues
) {
    suspend fun generateRoute(request: AccessibleRouteRequest): Result<RouteResponse> {
        return apiClient.safeRequest {
            post(ApiClient.BASE_URL + "/routes/generate") {
                header("Authorization", "Bearer ${appValues.authToken.get()}")
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body()
        }
    }

    suspend fun getRoutes(): Result<List<RouteResponse>> {
        return apiClient.safeRequest {
            get(ApiClient.BASE_URL + "/routes") {
                header("Authorization", "Bearer ${appValues.authToken.get()}")
            }.body()
        }
    }

    suspend fun getRoute(routeId: Int): Result<RouteResponse> {
        return apiClient.safeRequest {
            get(ApiClient.BASE_URL + "/routes/$routeId") {
                header("Authorization", "Bearer ${appValues.authToken.get()}")
            }.body()
        }
    }

    suspend fun deleteRoute(routeId: Int): Result<Unit> {
        return apiClient.safeRequest {
            delete(ApiClient.BASE_URL + "/routes/$routeId") {
                header("Authorization", "Bearer ${appValues.authToken.get()}")
            }
        }
    }
}

// === AI Service (Stub) ===

class AiService(
    private val apiClient: ApiClient,
    private val appValues: AppValues
) {
    suspend fun generateComment(data: GenerateCommentRequest): Result<GenerateCommentResponse> {
        // Stub - return predefined accessibility tips
        val tips = listOf(
            "Рекомендуем проверить наличие пандуса перед визитом. Позвоните заранее, чтобы уточнить доступность.",
            "Для слабовидящих рекомендуем маршруты с тактильной навигацией по ул. Большая Садовая.",
            "Низкопольные автобусы ходят по маршрутам 3, 7 и 22. Уточняйте расписание на остановках.",
            "В парке Горького есть оборудованные туалеты и зоны отдыха со скамейками.",
            "МФЦ на Пушкинской предоставляет услуги сурдопереводчика по предварительной записи."
        )
        return Result.success(GenerateCommentResponse(
            comment = tips.random(),
            success = true
        ))
    }

    suspend fun generateAiRoute(data: AiRouteRequest): Result<AiRouteResponse> {
        // Stub response
        return Result.success(AiRouteResponse(
            success = false,
            route = null,
            aiRecommendations = "ИИ-генерация маршрутов временно недоступна. Используйте ручное построение маршрута с фильтрами доступности.",
            errorMessage = "Функция находится в разработке"
        ))
    }
}
