package app.what.investtravel.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "places")
data class PlaceEntity(
    @PrimaryKey val id: Int = 0,
    val name: String? = null,
    val description: String? = null,
    val category: String? = null,
    val address: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val phone: String? = null,
    val website: String? = null,
    val images: String? = null,
    val accessibilityTags: String? = null,
    val disabilityTypes: String? = null,
    val rating: Float? = null,
    val reviewsCount: Int? = null,
    val status: String? = null,
    val createdAt: String? = null
)

@Entity(tableName = "markers")
data class MarkerEntity(
    @PrimaryKey val id: Int = 0,
    val userId: Int = 0,
    val title: String? = null,
    val description: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val markerType: String? = null,
    val accessibilityTags: String? = null,
    val photos: String? = null,
    val votesUp: Int = 0,
    val votesDown: Int = 0,
    val createdAt: String? = null
)

@Entity(tableName = "reviews")
data class ReviewEntity(
    @PrimaryKey val id: Int = 0,
    val userId: Int = 0,
    val placeId: Int? = null,
    val markerId: Int? = null,
    val text: String? = null,
    val rating: Int = 0,
    val photos: String? = null,
    val createdAt: String? = null,
    val userName: String? = null
)
