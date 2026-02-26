package app.what.investtravel.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.what.investtravel.data.local.entity.MarkerEntity

@Dao
interface MarkersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(markers: List<MarkerEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOne(marker: MarkerEntity)

    @Query("SELECT * FROM markers")
    suspend fun selectAll(): List<MarkerEntity>

    @Query("SELECT * FROM markers WHERE id = :id")
    suspend fun selectById(id: Int): MarkerEntity?

    @Query("DELETE FROM markers")
    suspend fun deleteAll()
}
