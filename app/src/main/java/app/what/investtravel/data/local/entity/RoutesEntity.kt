package app.what.investtravel.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "routes")
data class RouteEntity(
    @PrimaryKey(autoGenerate = true) val localId: Int = 0,
    val id: Int,
    val name: String? = null,
    val description: String? = null,
    val disabilityTypes: String? = null,
    val totalDurationHours: Float = 0f,
    val totalDistanceKm: Float = 0f,
    val createdAt: String? = null
)

@Entity(tableName = "route_points")
data class RoutePointEntity(
    @PrimaryKey(autoGenerate = true) val localId: Int = 0,
    val routeId: Int,
    val order: Int = 0,
    val name: String? = null,
    val category: String? = null,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val address: String? = null,
    val accessibilityTags: String? = null,
    val checked: Boolean = false
)

data class RouteWithPoints(
    @Embedded val route: RouteEntity,
    @Relation(
        parentColumn = "localId",
        entityColumn = "routeId"
    )
    val points: List<RoutePointEntity>
)
