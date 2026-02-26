package app.what.investtravel.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.what.investtravel.data.local.entity.PlaceEntity

@Dao
interface PlacesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(places: List<PlaceEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOne(place: PlaceEntity)

    @Query("SELECT * FROM places")
    suspend fun selectAll(): List<PlaceEntity>

    @Query("SELECT * FROM places WHERE id = :id")
    suspend fun selectById(id: Int): PlaceEntity?

    @Query("SELECT * FROM places WHERE category = :category")
    suspend fun selectByCategory(category: String): List<PlaceEntity>

    @Query("SELECT * FROM places WHERE disabilityTypes LIKE '%' || :type || '%'")
    suspend fun selectByDisabilityType(type: String): List<PlaceEntity>

    @Query("SELECT * FROM places WHERE name LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' OR address LIKE '%' || :query || '%'")
    suspend fun search(query: String): List<PlaceEntity>

    @Query("DELETE FROM places")
    suspend fun deleteAll()
}
