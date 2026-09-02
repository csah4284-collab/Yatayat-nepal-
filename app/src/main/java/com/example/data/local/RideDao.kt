package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.models.RideBooking
import kotlinx.coroutines.flow.Flow

@Dao
interface RideDao {
    @Query("SELECT * FROM ride_bookings ORDER BY createdAt DESC")
    fun getAllBookings(): Flow<List<RideBooking>>

    @Query("SELECT * FROM ride_bookings WHERE status IN ('SEARCHING', 'DRIVER_ASSIGNED', 'DRIVER_ARRIVED', 'ON_TRIP') ORDER BY createdAt DESC LIMIT 1")
    fun getActiveBooking(): Flow<RideBooking?>

    @Query("SELECT * FROM ride_bookings WHERE id = :id")
    suspend fun getBookingById(id: Long): RideBooking?

    @Query("SELECT * FROM ride_bookings WHERE status = 'COMPLETED' ORDER BY completedAt DESC")
    fun getCompletedBookings(): Flow<List<RideBooking>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: RideBooking): Long

    @Update
    suspend fun updateBooking(booking: RideBooking)

    @Query("UPDATE ride_bookings SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Long, status: String)

    @Query("UPDATE ride_bookings SET paymentStatus = :paymentStatus, transactionId = :txnId WHERE id = :id")
    suspend fun updatePayment(id: Long, paymentStatus: String, txnId: String)

    @Query("UPDATE ride_bookings SET driverRatingGiven = :rating, driverReview = :review, tipAmountNpr = :tip, complimentTags = :tags WHERE id = :id")
    suspend fun updateRating(id: Long, rating: Float, review: String, tip: Double, tags: String)

    @Query("DELETE FROM ride_bookings WHERE id = :id")
    suspend fun deleteBooking(id: Long)

    @Query("SELECT COUNT(*) FROM ride_bookings")
    fun getTotalRidesCount(): Flow<Int>

    @Query("SELECT SUM(fareNpr) FROM ride_bookings WHERE paymentStatus = 'PAID'")
    fun getTotalRevenue(): Flow<Double?>
}
