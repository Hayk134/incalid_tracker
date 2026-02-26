package app.what.investtravel.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import app.what.investtravel.data.local.entity.PlaceEntity
import app.what.investtravel.data.local.entity.MarkerEntity
import app.what.investtravel.data.local.entity.ReviewEntity
import app.what.investtravel.data.local.entity.RouteEntity
import app.what.investtravel.data.local.entity.RoutePointEntity

@Database(
    entities = [
        RouteEntity::class,
        RoutePointEntity::class,
        PlaceEntity::class,
        MarkerEntity::class,
        ReviewEntity::class
    ],
    version = 2
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun routesDao(): RoutesDAO
    abstract fun routePointsDao(): PointsDao
    abstract fun placesDao(): PlacesDao
    abstract fun markersDao(): MarkersDao
    abstract fun reviewsDao(): ReviewsDao
}
