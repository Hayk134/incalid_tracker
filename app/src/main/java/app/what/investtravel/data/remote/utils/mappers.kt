package app.what.investtravel.data.remote.utils

import app.what.investtravel.data.remote.AccessibleRouteRequest
import app.what.investtravel.features.travel.presentation.pages.UserPreferences

fun UserPreferences.toRoute(): AccessibleRouteRequest = AccessibleRouteRequest(
    name = this.name,
    description = this.description,
    startLat = this.startLatitude,
    startLon = this.startLongitude,
    endLat = this.endLatitude,
    endLon = this.endLongitude,
    disabilityTypes = this.disabilityTypes,
    avoidStairs = this.avoidStairs,
    preferRamps = this.preferRamps,
    maxDistanceKm = this.maxDistanceKm
)
