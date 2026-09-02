package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.models.DriverProfile
import com.example.data.models.NotificationItem
import com.example.data.models.UserAccount
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<NotificationItem>>

    @Query("SELECT COUNT(*) FROM notifications WHERE isRead = 0")
    fun getUnreadCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationItem): Long

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: Long)

    @Query("UPDATE notifications SET isRead = 1")
    suspend fun markAllAsRead()

    @Query("DELETE FROM notifications")
    suspend fun clearAll()
}

@Dao
interface UserDao {
    @Query("SELECT * FROM user_accounts WHERE id = 1")
    fun getUserProfile(): Flow<UserAccount?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUser(user: UserAccount)

    @Query("UPDATE user_accounts SET selectedLanguage = :lang WHERE id = 1")
    suspend fun updateLanguage(lang: String)

    @Query("UPDATE user_accounts SET walletBalanceNpr = walletBalanceNpr + :amount WHERE id = 1")
    suspend fun addWalletBalance(amount: Double)

    @Query("UPDATE user_accounts SET walletBalanceNpr = walletBalanceNpr - :amount WHERE id = 1")
    suspend fun deductWalletBalance(amount: Double)
}

@Dao
interface DriverDao {
    @Query("SELECT * FROM drivers ORDER BY rating DESC")
    fun getAllDrivers(): Flow<List<DriverProfile>>

    @Query("SELECT * FROM drivers WHERE isOnline = 1")
    fun getOnlineDrivers(): Flow<List<DriverProfile>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrivers(drivers: List<DriverProfile>)

    @Update
    suspend fun updateDriver(driver: DriverProfile)

    @Query("UPDATE drivers SET isOnline = :isOnline WHERE id = :id")
    suspend fun toggleOnlineStatus(id: Long, isOnline: Boolean)

    @Query("UPDATE drivers SET isVerified = :isVerified WHERE id = :id")
    suspend fun toggleVerification(id: Long, isVerified: Boolean)
}
