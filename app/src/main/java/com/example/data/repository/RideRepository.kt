package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.models.DriverProfile
import com.example.data.models.NotificationItem
import com.example.data.models.RideBooking
import com.example.data.models.UserAccount
import kotlinx.coroutines.flow.Flow

class RideRepository(private val database: AppDatabase) {
    val allBookings: Flow<List<RideBooking>> = database.rideDao().getAllBookings()
    val activeBooking: Flow<RideBooking?> = database.rideDao().getActiveBooking()
    val completedBookings: Flow<List<RideBooking>> = database.rideDao().getCompletedBookings()
    val allNotifications: Flow<List<NotificationItem>> = database.notificationDao().getAllNotifications()
    val unreadNotificationsCount: Flow<Int> = database.notificationDao().getUnreadCount()
    val userProfile: Flow<UserAccount?> = database.userDao().getUserProfile()
    val allDrivers: Flow<List<DriverProfile>> = database.driverDao().getAllDrivers()
    val onlineDrivers: Flow<List<DriverProfile>> = database.driverDao().getOnlineDrivers()
    val totalRidesCount: Flow<Int> = database.rideDao().getTotalRidesCount()
    val totalRevenue: Flow<Double?> = database.rideDao().getTotalRevenue()

    suspend fun createBooking(booking: RideBooking): Long {
        return database.rideDao().insertBooking(booking)
    }

    suspend fun updateBooking(booking: RideBooking) {
        database.rideDao().updateBooking(booking)
    }

    suspend fun updateRideStatus(id: Long, status: String) {
        database.rideDao().updateStatus(id, status)
    }

    suspend fun updatePayment(id: Long, paymentStatus: String, txnId: String) {
        database.rideDao().updatePayment(id, paymentStatus, txnId)
    }

    suspend fun submitRating(id: Long, rating: Float, review: String, tip: Double, tags: String) {
        database.rideDao().updateRating(id, rating, review, tip, tags)
    }

    suspend fun addNotification(notification: NotificationItem) {
        database.notificationDao().insertNotification(notification)
    }

    suspend fun markNotificationAsRead(id: Long) {
        database.notificationDao().markAsRead(id)
    }

    suspend fun markAllNotificationsAsRead() {
        database.notificationDao().markAllAsRead()
    }

    suspend fun updateUser(user: UserAccount) {
        database.userDao().insertOrUpdateUser(user)
    }

    suspend fun updateLanguage(lang: String) {
        database.userDao().updateLanguage(lang)
    }

    suspend fun toggleDriverOnline(id: Long, isOnline: Boolean) {
        database.driverDao().toggleOnlineStatus(id, isOnline)
    }

    suspend fun toggleDriverVerified(id: Long, isVerified: Boolean) {
        database.driverDao().toggleVerification(id, isVerified)
    }
}
