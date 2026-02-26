package app.what.investtravel.data.local.mappers

import app.what.investtravel.data.local.entity.RouteEntity
import app.what.investtravel.data.local.entity.RoutePointEntity
import app.what.investtravel.data.remote.RoutePointResponse
import app.what.investtravel.data.remote.RouteResponse

fun RouteResponse.toEntity(): RouteEntity {
    return RouteEntity(
        id = id,
        name = name,
        description = description,
        disabilityTypes = disabilityTypes,
        totalDurationHours = totalDurationHours,
        totalDistanceKm = totalDistanceKm,
        createdAt = createdAt
    )
}

fun List<RoutePointResponse>.toPointEntities(localRouteId: Int): List<RoutePointEntity> {
    return this.map {
        RoutePointEntity(
            routeId = localRouteId,
            order = it.order,
            name = it.name,
            category = it.category,
            latitude = it.latitude,
            longitude = it.longitude,
            address = it.address,
            accessibilityTags = it.accessibilityTags,
            checked = false
        )
    }
}
