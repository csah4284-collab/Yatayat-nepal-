package com.example.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val message: String,
    val titleNe: String = "",
    val messageNe: String = "",
    val type: String, // RIDE, PAYMENT, PROMO, SAFETY, SYSTEM
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val actionData: String = ""
)

@Entity(tableName = "user_accounts")
data class UserAccount(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 1,
    val fullName: String = "Aarav Sharma",
    val phone: String = "+977 9801234567",
    val email: String = "aarav.nepal@gmail.com",
    val emergencyContactName: String = "Sunita Sharma (Family)",
    val emergencyContactPhone: String = "+977 9841987654",
    val role: String = "RIDER", // RIDER, DRIVER, ADMIN
    val eSewaLinkedPhone: String = "9801234567",
    val khaltiLinkedPhone: String = "9801234567",
    val walletBalanceNpr: Double = 1450.0,
    val selectedLanguage: String = "en", // en, ne, ne-rom
    val totalRidesCompleted: Int = 14,
    val totalSpentNpr: Double = 4320.0
)

@Entity(tableName = "drivers")
data class DriverProfile(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val phone: String,
    val vehicleType: String, // BIKE, AUTO, CAR, BUS
    val vehicleModel: String,
    val vehicleNumber: String,
    val rating: Double,
    val totalTrips: Int,
    val isVerified: Boolean = true,
    val isOnline: Boolean = true,
    val currentCity: String = "Kathmandu",
    val todayEarningsNpr: Double = 1850.0
)
