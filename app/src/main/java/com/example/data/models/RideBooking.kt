package com.example.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ride_bookings")
data class RideBooking(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val bookingReference: String,
    val pickupName: String,
    val pickupAddress: String,
    val pickupLat: Double,
    val pickupLng: Double,
    val dropName: String,
    val dropAddress: String,
    val dropLat: Double,
    val dropLng: Double,
    val vehicleType: String, // BIKE, AUTO, CAR, BUS
    val fareNpr: Double,
    val originalFareNpr: Double,
    val promoDiscountNpr: Double = 0.0,
    val promoCode: String = "",
    val distanceKm: Double,
    val durationMins: Int,
    val status: String, // RideStatus name
    val driverId: Long = 101,
    val driverName: String = "Bikash Shrestha",
    val driverPhone: String = "+977 9841234567",
    val driverRating: Double = 4.88,
    val driverVehicleNumber: String = "BA 24 PA 8821",
    val driverPhotoUrl: String = "",
    val otp: String = "4829",
    val paymentMethod: String, // ESEWA, KHALTI, FONEPAY, CASH, CARD
    val paymentStatus: String, // PENDING, PAID, REFUNDED
    val transactionId: String = "",
    val driverRatingGiven: Float = 0f,
    val driverReview: String = "",
    val tipAmountNpr: Double = 0.0,
    val complimentTags: String = "", // comma-separated
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null,
    val cancellationReason: String = "",
    val liveTrackingCode: String = "YATRI-${System.currentTimeMillis() % 100000}"
)
