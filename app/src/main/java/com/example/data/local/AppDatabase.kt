package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.models.DriverProfile
import com.example.data.models.NotificationItem
import com.example.data.models.RideBooking
import com.example.data.models.UserAccount
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        RideBooking::class,
        NotificationItem::class,
        UserAccount::class,
        DriverProfile::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun rideDao(): RideDao
    abstract fun notificationDao(): NotificationDao
    abstract fun userDao(): UserDao
    abstract fun driverDao(): DriverDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "yatri_nepal_db"
                ).addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            seedInitialData(getInstance(context))
                        }
                    }
                }).build()
                INSTANCE = instance
                instance
            }
        }

        private suspend fun seedInitialData(database: AppDatabase) {
            // Seed user
            database.userDao().insertOrUpdateUser(
                UserAccount(
                    id = 1,
                    fullName = "Aarav Sharma",
                    phone = "+977 9801234567",
                    email = "aarav.nepal@gmail.com",
                    emergencyContactName = "Sunita Sharma (Family)",
                    emergencyContactPhone = "+977 9841987654",
                    role = "RIDER",
                    eSewaLinkedPhone = "9801234567",
                    khaltiLinkedPhone = "9801234567",
                    walletBalanceNpr = 2850.0,
                    selectedLanguage = "en",
                    totalRidesCompleted = 6,
                    totalSpentNpr = 1420.0
                )
            )

            // Seed Drivers
            database.driverDao().insertDrivers(
                listOf(
                    DriverProfile(
                        id = 101,
                        name = "Bikash Shrestha",
                        phone = "+977 9841234567",
                        vehicleType = "BIKE",
                        vehicleModel = "Pulsar 150 (Red)",
                        vehicleNumber = "BA 24 PA 8821",
                        rating = 4.92,
                        totalTrips = 842,
                        isVerified = true,
                        isOnline = true,
                        currentCity = "Kathmandu",
                        todayEarningsNpr = 1450.0
                    ),
                    DriverProfile(
                        id = 102,
                        name = "Ramesh Chaudhary",
                        phone = "+977 9812345678",
                        vehicleType = "AUTO",
                        vehicleModel = "Bajaj RE CNG Auto",
                        vehicleNumber = "PRA 3-02-001 HA 4492",
                        rating = 4.85,
                        totalTrips = 1290,
                        isVerified = true,
                        isOnline = true,
                        currentCity = "Kathmandu",
                        todayEarningsNpr = 2200.0
                    ),
                    DriverProfile(
                        id = 103,
                        name = "Deepak Gurung",
                        phone = "+977 9851122334",
                        vehicleType = "CAR",
                        vehicleModel = "Suzuki Swift Dzire AC",
                        vehicleNumber = "BA 1 JHA 6512",
                        rating = 4.96,
                        totalTrips = 2140,
                        isVerified = true,
                        isOnline = true,
                        currentCity = "Kathmandu",
                        todayEarningsNpr = 4100.0
                    ),
                    DriverProfile(
                        id = 104,
                        name = "Manish Thapa",
                        phone = "+977 9860112233",
                        vehicleType = "BUS",
                        vehicleModel = "Toyota HiAce Tourist Express",
                        vehicleNumber = "BA 2 KHA 9120",
                        rating = 4.88,
                        totalTrips = 410,
                        isVerified = true,
                        isOnline = true,
                        currentCity = "Kathmandu - Pokhara Route",
                        todayEarningsNpr = 8900.0
                    ),
                    DriverProfile(
                        id = 105,
                        name = "Sujan Tamang",
                        phone = "+977 9843998877",
                        vehicleType = "BIKE",
                        vehicleModel = "Yamaha FZ Version 3",
                        vehicleNumber = "BA 89 PA 1245",
                        rating = 4.79,
                        totalTrips = 530,
                        isVerified = true,
                        isOnline = true,
                        currentCity = "Lalitpur",
                        todayEarningsNpr = 980.0
                    )
                )
            )

            // Seed Past Completed Bookings with real Nepal routes & eSewa/Khalti payments
            val currentTime = System.currentTimeMillis()
            database.rideDao().insertBooking(
                RideBooking(
                    bookingReference = "YN-2026-8812",
                    pickupName = "Thamel",
                    pickupAddress = "Thamel Marg, Ward 26, Kathmandu",
                    pickupLat = 27.7154,
                    pickupLng = 85.3123,
                    dropName = "Tribhuvan Int'l Airport (TIA)",
                    dropAddress = "Ring Road, Airport Gate, Kathmandu",
                    dropLat = 27.6966,
                    dropLng = 85.3591,
                    vehicleType = "CAR",
                    fareNpr = 450.0,
                    originalFareNpr = 500.0,
                    promoDiscountNpr = 50.0,
                    promoCode = "YATRI50",
                    distanceKm = 6.2,
                    durationMins = 22,
                    status = "COMPLETED",
                    driverId = 103,
                    driverName = "Deepak Gurung",
                    driverPhone = "+977 9851122334",
                    driverRating = 4.96,
                    driverVehicleNumber = "BA 1 JHA 6512",
                    otp = "7182",
                    paymentMethod = "ESEWA",
                    paymentStatus = "PAID",
                    transactionId = "ESW-99214482-NP",
                    driverRatingGiven = 5.0f,
                    driverReview = "Very polite driver, AC was nice and reached airport on time!",
                    tipAmountNpr = 50.0,
                    complimentTags = "Safe Driving, On Time, Clean Vehicle",
                    createdAt = currentTime - 86400000 * 2,
                    completedAt = currentTime - 86400000 * 2 + 1800000
                )
            )

            database.rideDao().insertBooking(
                RideBooking(
                    bookingReference = "YN-2026-8741",
                    pickupName = "Patan Durbar Square",
                    pickupAddress = "Mangal Bazar, Lalitpur",
                    pickupLat = 27.6744,
                    pickupLng = 85.3260,
                    dropName = "Durbar Marg / Kingsway",
                    dropAddress = "Durbar Marg, Kathmandu",
                    dropLat = 27.7107,
                    dropLng = 85.3175,
                    vehicleType = "BIKE",
                    fareNpr = 140.0,
                    originalFareNpr = 140.0,
                    promoDiscountNpr = 0.0,
                    distanceKm = 4.8,
                    durationMins = 14,
                    status = "COMPLETED",
                    driverId = 101,
                    driverName = "Bikash Shrestha",
                    driverPhone = "+977 9841234567",
                    driverRating = 4.92,
                    driverVehicleNumber = "BA 24 PA 8821",
                    otp = "3391",
                    paymentMethod = "KHALTI",
                    paymentStatus = "PAID",
                    transactionId = "KHL-44810291-NP",
                    driverRatingGiven = 5.0f,
                    driverReview = "Bikash gave a clean helmet and beat the traffic smoothly.",
                    tipAmountNpr = 20.0,
                    complimentTags = "Fast Route, Polite",
                    createdAt = currentTime - 86400000 * 4,
                    completedAt = currentTime - 86400000 * 4 + 1200000
                )
            )

            database.rideDao().insertBooking(
                RideBooking(
                    bookingReference = "YN-2026-8610",
                    pickupName = "Kalanki Chowk",
                    pickupAddress = "Prithvi Highway Junction, Kathmandu",
                    pickupLat = 27.6934,
                    pickupLng = 85.2818,
                    dropName = "Bouddhanath Stupa",
                    dropAddress = "Bouddha, Kathmandu",
                    dropLat = 27.7215,
                    dropLng = 85.3620,
                    vehicleType = "AUTO",
                    fareNpr = 380.0,
                    originalFareNpr = 380.0,
                    promoDiscountNpr = 0.0,
                    distanceKm = 9.8,
                    durationMins = 35,
                    status = "COMPLETED",
                    driverId = 102,
                    driverName = "Ramesh Chaudhary",
                    driverPhone = "+977 9812345678",
                    driverRating = 4.85,
                    driverVehicleNumber = "PRA 3-02-001 HA 4492",
                    otp = "9201",
                    paymentMethod = "CASH",
                    paymentStatus = "PAID",
                    transactionId = "CSH-RECEIPT-8610",
                    driverRatingGiven = 4.0f,
                    driverReview = "Good tempo ride across the ring road.",
                    tipAmountNpr = 0.0,
                    complimentTags = "Affordable",
                    createdAt = currentTime - 86400000 * 7,
                    completedAt = currentTime - 86400000 * 7 + 2400000
                )
            )

            // Seed Notifications
            database.notificationDao().insertNotification(
                NotificationItem(
                    title = "Welcome to Yatri Nepal!",
                    titleNe = "यात्री नेपालमा स्वागत छ!",
                    message = "Book Bike, Auto, Taxi & Bus across Nepal with eSewa & Khalti support.",
                    messageNe = "नेपालभर बाइक, अटो, ट्याक्सी र बस बुक गर्नुहोस्। ई-सेवा र खल्ती भुक्तानी उपलब्ध!",
                    type = "SYSTEM",
                    timestamp = currentTime - 86400000 * 7,
                    isRead = true
                )
            )
            database.notificationDao().insertNotification(
                NotificationItem(
                    title = "Payment Successful: NPR 450.0 via eSewa",
                    titleNe = "भुक्तानी सम्पन्न: ई-सेवा मार्फत रु ४५०.०",
                    message = "Your payment for ride YN-2026-8812 was completed successfully. Txn: ESW-99214482-NP",
                    messageNe = "तपाईंको यात्रा YN-2026-8812 को भुक्तानी सफलतापूर्वक सम्पन्न भयो।",
                    type = "PAYMENT",
                    timestamp = currentTime - 86400000 * 2,
                    isRead = true
                )
            )
            database.notificationDao().insertNotification(
                NotificationItem(
                    title = "Festive Offer: 20% OFF on all Cabs!",
                    titleNe = "चाडपर्व अफर: सबै क्याबमा २०% छुट!",
                    message = "Use promo code 'NEPALRIDE' to get up to NPR 100 discount on your next ride.",
                    messageNe = "प्रोमो कोड 'NEPALRIDE' प्रयोग गरी अर्को यात्रामा रु १०० सम्म छुट पाउनुहोस्।",
                    type = "PROMO",
                    timestamp = currentTime - 3600000 * 4,
                    isRead = false
                )
            )
        }
    }
}
