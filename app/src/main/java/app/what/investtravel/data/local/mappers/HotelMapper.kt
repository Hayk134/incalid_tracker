package app.what.investtravel.data.local.mappers

import app.what.investtravel.data.local.entity.PlaceEntity
import app.what.investtravel.data.local.entity.MarkerEntity
import app.what.investtravel.data.local.entity.ReviewEntity
import app.what.investtravel.data.remote.PlaceResponse
import app.what.investtravel.data.remote.MarkerResponse
import app.what.investtravel.data.remote.ReviewResponse

fun PlaceResponse.toEntity(): PlaceEntity {
    return PlaceEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        category = this.category,
        address = this.address,
        latitude = this.latitude,
        longitude = this.longitude,
        phone = this.phone,
        website = this.website,
        images = this.images,
        accessibilityTags = this.accessibilityTags,
        disabilityTypes = this.disabilityTypes,
        rating = this.rating,
        reviewsCount = this.reviewsCount,
        status = this.status,
        createdAt = this.createdAt
    )
}

fun List<PlaceResponse>.toPlaceEntities(): List<PlaceEntity> {
    return this.map { it.toEntity() }
}

fun MarkerResponse.toEntity(): MarkerEntity {
    return MarkerEntity(
        id = this.id,
        userId = this.userId,
        title = this.title,
        description = this.description,
        latitude = this.latitude,
        longitude = this.longitude,
        markerType = this.markerType,
        accessibilityTags = this.accessibilityTags,
        photos = this.photos,
        votesUp = this.votesUp,
        votesDown = this.votesDown,
        createdAt = this.createdAt
    )
}

fun List<MarkerResponse>.toMarkerEntities(): List<MarkerEntity> {
    return this.map { it.toEntity() }
}

fun ReviewResponse.toEntity(): ReviewEntity {
    return ReviewEntity(
        id = this.id,
        userId = this.userId,
        placeId = this.placeId,
        markerId = this.markerId,
        text = this.text,
        rating = this.rating,
        photos = this.photos,
        createdAt = this.createdAt,
        userName = this.userName
    )
}

fun List<ReviewResponse>.toReviewEntities(): List<ReviewEntity> {
    return this.map { it.toEntity() }
}
