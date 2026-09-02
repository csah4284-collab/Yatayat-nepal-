package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.models.DriverProfile
import com.example.data.models.NepalLocation
import com.example.data.models.NepalLocationsPreset
import com.example.data.models.NotificationItem
import com.example.data.models.PaymentMethod
import com.example.data.models.RideBooking
import com.example.data.models.UserAccount
import com.example.data.models.VehicleType
import com.example.data.repository.RideRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RideViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: RideRepository

    init {
        val db = AppDatabase.getInstance(application)
        repository = RideRepository(db)
    }

    val allBookings = repository.allBookings.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val activeBooking = repository.activeBooking.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    val allNotifications = repository.allNotifications.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val unreadNotificationsCount = repository.unreadNotificationsCount.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        0
    )

    val userProfile = repository.userProfile.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    val allDrivers = repository.allDrivers.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    // Current Booking State
    var pickupLocation = MutableStateFlow<NepalLocation>(NepalLocationsPreset.presets[0]) // Thamel
    var dropLocation = MutableStateFlow<NepalLocation>(NepalLocationsPreset.presets[1])   // Airport
    var selectedVehicle = MutableStateFlow<VehicleType>(VehicleType.CAR)
    var selectedPaymentMethod = MutableStateFlow<PaymentMethod>(PaymentMethod.ESEWA)
    var promoCode = MutableStateFlow<String>("")
    var discountAmountNpr = MutableStateFlow<Double>(0.0)
    var promoMessage = MutableStateFlow<String>("")

    // Map & Simulation Progress
    var tripProgress = MutableStateFlow<Float>(0.0f)
    private var simulationJob: Job? = null

    // App Navigation & Language
    var currentLanguage = MutableStateFlow<String>("en") // en, ne, ne-rom
    var currentScreen = MutableStateFlow<String>("home") // home, tracking, history, admin, profile

    // Active Dialogs
    var showPaymentGateway = MutableStateFlow<PaymentMethod?>(null)
    var showRatingDialog = MutableStateFlow<RideBooking?>(null)
    var showNotificationSheet = MutableStateFlow<Boolean>(false)

    fun setLanguage(lang: String) {
        currentLanguage.value = lang
        viewModelScope.launch {
            repository.updateLanguage(lang)
        }
    }

    fun applyPromoCode(code: String) {
        val trimmed = code.trim().uppercase()
        promoCode.value = trimmed
        when (trimmed) {
            "YATRI50", "NEPAL50" -> {
                discountAmountNpr.value = 50.0
                promoMessage.value = "NPR 50 discount applied!"
            }
            "ESEWAPAY" -> {
                discountAmountNpr.value = 75.0
                promoMessage.value = "eSewa special: NPR 75 discount!"
            }
            "KHALTIRIDE" -> {
                discountAmountNpr.value = 80.0
                promoMessage.value = "Khalti special: NPR 80 discount!"
            }
            "FREEFIRST" -> {
                discountAmountNpr.value = 100.0
                promoMessage.value = "Welcome gift: NPR 100 discount!"
            }
            else -> {
                discountAmountNpr.value = 0.0
                promoMessage.value = "Invalid promo code"
            }
        }
    }

    fun bookRide(onSuccess: (Long) -> Unit) {
        val pickup = pickupLocation.value
        val drop = dropLocation.value
        val vehicle = selectedVehicle.value
        val paymentMethod = selectedPaymentMethod.value
        val distance = NepalLocationsPreset.calculateDistanceKm(pickup, drop)
        val rawFare = vehicle.calculateFare(distance)
        val finalFare = (rawFare - discountAmountNpr.value).coerceAtLeast(vehicle.baseFareNpr)

        val reference = "YN-2026-${(1000..9999).random()}"
        val otpCode = (1000..9999).random().toString()

        val assignedDriver = when (vehicle) {
            VehicleType.BIKE -> DriverProfile(101, "Bikash Shrestha", "+977 9841234567", "BIKE", "Pulsar 150 (Red)", "BA 24 PA 8821", 4.92, 842)
            VehicleType.AUTO -> DriverProfile(102, "Ramesh Chaudhary", "+977 9812345678", "AUTO", "Bajaj RE Auto", "PRA 3-02-001 HA 4492", 4.85, 1290)
            VehicleType.CAR -> DriverProfile(103, "Deepak Gurung", "+977 9851122334", "CAR", "Suzuki Swift Dzire", "BA 1 JHA 6512", 4.96, 2140)
            VehicleType.BUS -> DriverProfile(104, "Manish Thapa", "+977 9860112233", "BUS", "Toyota HiAce Express", "BA 2 KHA 9120", 4.88, 410)
        }

        val booking = RideBooking(
            bookingReference = reference,
            pickupName = pickup.nameEn,
            pickupAddress = pickup.address,
            pickupLat = pickup.lat,
            pickupLng = pickup.lng,
            dropName = drop.nameEn,
            dropAddress = drop.address,
            dropLat = drop.lat,
            dropLng = drop.lng,
            vehicleType = vehicle.name,
            fareNpr = finalFare,
            originalFareNpr = rawFare,
            promoDiscountNpr = discountAmountNpr.value,
            promoCode = promoCode.value,
            distanceKm = distance,
            durationMins = (distance * 3.5).toInt() + 5,
            status = "DRIVER_ASSIGNED",
            driverId = assignedDriver.id,
            driverName = assignedDriver.name,
            driverPhone = assignedDriver.phone,
            driverRating = assignedDriver.rating,
            driverVehicleNumber = assignedDriver.vehicleNumber,
            otp = otpCode,
            paymentMethod = paymentMethod.name,
            paymentStatus = "PENDING",
            transactionId = ""
        )

        viewModelScope.launch {
            val id = repository.createBooking(booking)
            repository.addNotification(
                NotificationItem(
                    title = "Ride Confirmed: ${assignedDriver.name} is on the way!",
                    titleNe = "यात्रा पुष्टि भयो: चालक ${assignedDriver.name} आउँदै हुनुहुन्छ!",
                    message = "Your ${vehicle.titleEn} (${assignedDriver.vehicleNumber}) is arriving in 3 mins. Start OTP: $otpCode",
                    messageNe = "तपाईंको ${vehicle.titleNe} ३ मिनेटमा आइपुग्नेछ। ओटिपी: $otpCode",
                    type = "RIDE"
                )
            )
            tripProgress.value = 0.1f
            currentScreen.value = "tracking"
            startSimulation(id)
            onSuccess(id)
        }
    }

    private fun startSimulation(bookingId: Long) {
        simulationJob?.cancel()
        simulationJob = viewModelScope.launch {
            // Driver Assigned
            delay(4000)
            repository.updateRideStatus(bookingId, "DRIVER_ARRIVED")
            tripProgress.value = 0.25f
            repository.addNotification(
                NotificationItem(
                    title = "Driver has arrived at pickup point!",
                    titleNe = "चालक पिकअप स्थानमा आइपुग्नुभयो!",
                    message = "Please share your 4-digit OTP to start your journey.",
                    messageNe = "यात्रा सुरु गर्न आफ्नो ४-अङ्कको ओटिपी चालकलाई दिनुहोस्।",
                    type = "RIDE"
                )
            )

            // Auto progress to On Trip
            delay(5000)
            repository.updateRideStatus(bookingId, "ON_TRIP")
            repository.addNotification(
                NotificationItem(
                    title = "Trip Started! Safe travels across Nepal.",
                    titleNe = "यात्रा सुरु भयो! शुभ यात्रा।",
                    message = "You can share live tracking with your family and use SOS if needed.",
                    messageNe = "तपाईं आफ्नो प्रत्यक्ष स्थान परिवारसँग सेयर गर्न सक्नुहुन्छ।",
                    type = "SAFETY"
                )
            )

            // Smooth route progression
            for (i in 30..95 step 5) {
                delay(1200)
                tripProgress.value = i / 100.0f
            }
        }
    }

    fun stepAdvanceRide(booking: RideBooking) {
        viewModelScope.launch {
            when (booking.status) {
                "SEARCHING" -> {
                    repository.updateRideStatus(booking.id, "DRIVER_ASSIGNED")
                    tripProgress.value = 0.2f
                }
                "DRIVER_ASSIGNED" -> {
                    repository.updateRideStatus(booking.id, "DRIVER_ARRIVED")
                    tripProgress.value = 0.35f
                }
                "DRIVER_ARRIVED" -> {
                    repository.updateRideStatus(booking.id, "ON_TRIP")
                    tripProgress.value = 0.5f
                }
                "ON_TRIP" -> {
                    tripProgress.value = 1.0f
                    // If payment is digital, show gateway dialog, else complete directly
                    if (booking.paymentMethod == "ESEWA" || booking.paymentMethod == "KHALTI" || booking.paymentMethod == "FONEPAY") {
                        val method = PaymentMethod.valueOf(booking.paymentMethod)
                        showPaymentGateway.value = method
                    } else {
                        completeTrip(booking.id, "CSH-${(100000..999999).random()}")
                    }
                }
            }
        }
    }

    fun completePaymentAndTrip(bookingId: Long, txnId: String) {
        viewModelScope.launch {
            showPaymentGateway.value = null
            repository.updatePayment(bookingId, "PAID", txnId)
            repository.updateRideStatus(bookingId, "COMPLETED")
            tripProgress.value = 1.0f

            val booking = allBookings.value.find { it.id == bookingId }
            repository.addNotification(
                NotificationItem(
                    title = "Payment Successful: NPR ${booking?.fareNpr ?: 0.0}",
                    titleNe = "भुक्तानी सम्पन्न: रु ${booking?.fareNpr ?: 0.0}",
                    message = "Transaction ID: $txnId. Thank you for traveling with Yatri Nepal!",
                    messageNe = "कारोबार आइडी: $txnId। यात्री नेपाल रोज्नुभएकोमा धन्यवाद!",
                    type = "PAYMENT"
                )
            )

            if (booking != null) {
                showRatingDialog.value = booking
            }
        }
    }

    private fun completeTrip(bookingId: Long, txnId: String) {
        viewModelScope.launch {
            repository.updatePayment(bookingId, "PAID", txnId)
            repository.updateRideStatus(bookingId, "COMPLETED")
            val booking = allBookings.value.find { it.id == bookingId }
            if (booking != null) {
                showRatingDialog.value = booking
            }
        }
    }

    fun cancelActiveRide(bookingId: Long) {
        simulationJob?.cancel()
        viewModelScope.launch {
            repository.updateRideStatus(bookingId, "CANCELLED")
            tripProgress.value = 0f
            currentScreen.value = "home"
            repository.addNotification(
                NotificationItem(
                    title = "Ride Cancelled",
                    titleNe = "यात्रा रद्द गरियो",
                    message = "Your booking was successfully cancelled.",
                    messageNe = "तपाईंको यात्रा सफलतापूर्वक रद्द गरियो।",
                    type = "RIDE"
                )
            )
        }
    }

    fun submitRating(bookingId: Long, rating: Float, review: String, tip: Double, tags: String) {
        viewModelScope.launch {
            repository.submitRating(bookingId, rating, review, tip, tags)
            showRatingDialog.value = null
            currentScreen.value = "history"
        }
    }

    fun markNotificationAsRead(id: Long) {
        viewModelScope.launch {
            repository.markNotificationAsRead(id)
        }
    }

    fun markAllNotificationsAsRead() {
        viewModelScope.launch {
            repository.markAllNotificationsAsRead()
        }
    }

    fun toggleDriverOnline(id: Long, isOnline: Boolean) {
        viewModelScope.launch {
            repository.toggleDriverOnline(id, isOnline)
        }
    }

    fun toggleDriverVerification(id: Long, isVerified: Boolean) {
        viewModelScope.launch {
            repository.toggleDriverVerified(id, isVerified)
        }
    }
}
