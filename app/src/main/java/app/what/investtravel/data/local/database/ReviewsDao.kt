package app.what.investtravel.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.what.investtravel.data.local.entity.ReviewEntity

@Dao
interface ReviewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reviews: List<ReviewEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOne(review: ReviewEntity)

    @Query("SELECT * FROM reviews WHERE placeId = :placeId")
    suspend fun selectByPlaceId(placeId: Int): List<ReviewEntity>

    @Query("SELECT * FROM reviews WHERE markerId = :markerId")
    suspend fun selectByMarkerId(markerId: Int): List<ReviewEntity>

    @Query("DELETE FROM reviews")
    suspend fun deleteAll()
}
