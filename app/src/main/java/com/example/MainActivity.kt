package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.models.PaymentMethod
import com.example.ui.components.DriverRatingDialog
import com.example.ui.components.ESewaPaymentGatewayDialog
import com.example.ui.components.FonepayQrDialog
import com.example.ui.components.KhaltiPaymentGatewayDialog
import com.example.ui.components.NotificationSheet
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.BookingHistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LiveRideTrackingScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.theme.NepalCrimsonPrimary
import com.example.ui.theme.NepalGoldSecondary
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.YatriNepalTheme
import com.example.ui.viewmodel.RideViewModel
import com.example.util.LanguageManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YatriNepalTheme {
                YatriNepalApp()
            }
        }
    }
}

sealed class NavigationTab(
    val route: String,
    val titleKey: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object Home : NavigationTab("home", "nav_home", Icons.Filled.Home, Icons.Outlined.Home)
    object Tracking : NavigationTab("tracking", "nav_tracking", Icons.Filled.DirectionsCar, Icons.Outlined.DirectionsCar)
    object History : NavigationTab("history", "nav_history", Icons.Filled.History, Icons.Outlined.History)
    object Admin : NavigationTab("admin", "nav_admin", Icons.Filled.Assessment, Icons.Outlined.Assessment)
    object Profile : NavigationTab("profile", "nav_profile", Icons.Filled.Person, Icons.Outlined.Person)
}

@Composable
fun YatriNepalApp(viewModel: RideViewModel = viewModel()) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val currentLanguage by viewModel.currentLanguage.collectAsState()
    val activeBooking by viewModel.activeBooking.collectAsState()
    val allBookings by viewModel.allBookings.collectAsState()
    val allDrivers by viewModel.allDrivers.collectAsState()
    val allNotifications by viewModel.allNotifications.collectAsState()
    val unreadNotificationsCount by viewModel.unreadNotificationsCount.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()

    val pickupLocation by viewModel.pickupLocation.collectAsState()
    val dropLocation by viewModel.dropLocation.collectAsState()
    val selectedVehicle by viewModel.selectedVehicle.collectAsState()
    val selectedPaymentMethod by viewModel.selectedPaymentMethod.collectAsState()
    val promoCode by viewModel.promoCode.collectAsState()
    val discountAmountNpr by viewModel.discountAmountNpr.collectAsState()
    val promoMessage by viewModel.promoMessage.collectAsState()
    val tripProgress by viewModel.tripProgress.collectAsState()

    val showPaymentGateway by viewModel.showPaymentGateway.collectAsState()
    val showRatingDialog by viewModel.showRatingDialog.collectAsState()
    val showNotificationSheet by viewModel.showNotificationSheet.collectAsState()

    val navTabs = listOf(
        NavigationTab.Home,
        NavigationTab.Tracking,
        NavigationTab.History,
        NavigationTab.Admin,
        NavigationTab.Profile
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                navTabs.forEach { tab ->
                    val isSelected = currentScreen == tab.route
                    val hasActiveRide = tab.route == "tracking" && activeBooking != null &&
                            activeBooking!!.status != "COMPLETED" && activeBooking!!.status != "CANCELLED"

                    NavigationBarItem(
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (hasActiveRide) {
                                        Badge(containerColor = StatusSuccess) {
                                            Text("●", fontSize = 8.sp, color = Color.White)
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                    contentDescription = tab.titleKey
                                )
                            }
                        },
                        label = {
                            Text(
                                text = LanguageManager.getString(tab.titleKey, currentLanguage),
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        selected = isSelected,
                        onClick = { viewModel.currentScreen.value = tab.route },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NepalCrimsonPrimary,
                            selectedTextColor = NepalCrimsonPrimary,
                            indicatorColor = NepalCrimsonPrimary.copy(alpha = 0.12f),
                            unselectedIconColor = Color(0xFF64748B),
                            unselectedTextColor = Color(0xFF64748B)
                        ),
                        modifier = Modifier.testTag("nav_tab_${tab.route}")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "screen_transition"
            ) { screen ->
                when (screen) {
                    "home" -> HomeScreen(
                        pickupLocation = pickupLocation,
                        dropLocation = dropLocation,
                        selectedVehicle = selectedVehicle,
                        selectedPaymentMethod = selectedPaymentMethod,
                        promoCode = promoCode,
                        discountAmountNpr = discountAmountNpr,
                        promoMessage = promoMessage,
                        activeBooking = activeBooking,
                        unreadCount = unreadNotificationsCount,
                        language = currentLanguage,
                        onPickupChange = { viewModel.pickupLocation.value = it },
                        onDropChange = { viewModel.dropLocation.value = it },
                        onSwapLocations = {
                            val temp = viewModel.pickupLocation.value
                            viewModel.pickupLocation.value = viewModel.dropLocation.value
                            viewModel.dropLocation.value = temp
                        },
                        onVehicleSelect = { viewModel.selectedVehicle.value = it },
                        onPaymentMethodSelect = { viewModel.selectedPaymentMethod.value = it },
                        onApplyPromo = { viewModel.applyPromoCode(it) },
                        onBookRide = { viewModel.bookRide { } },
                        onNavigateToTracking = { viewModel.currentScreen.value = "tracking" },
                        onOpenNotifications = { viewModel.showNotificationSheet.value = true },
                        onLanguageChange = { viewModel.setLanguage(it) }
                    )

                    "tracking" -> LiveRideTrackingScreen(
                        activeBooking = activeBooking,
                        userAccount = userProfile,
                        tripProgress = tripProgress,
                        language = currentLanguage,
                        onBack = { viewModel.currentScreen.value = "home" },
                        onCancelRide = {
                            activeBooking?.let { viewModel.cancelActiveRide(it.id) }
                        },
                        onTriggerNextStep = {
                            activeBooking?.let { viewModel.stepAdvanceRide(it) }
                        },
                        onOpenPayment = {
                            activeBooking?.let {
                                if (it.paymentMethod in listOf("ESEWA", "KHALTI", "FONEPAY")) {
                                    viewModel.showPaymentGateway.value = PaymentMethod.valueOf(it.paymentMethod)
                                }
                            }
                        },
                        onBookNewRide = { viewModel.currentScreen.value = "home" }
                    )

                    "history" -> BookingHistoryScreen(
                        bookings = allBookings,
                        language = currentLanguage,
                        onRebookRide = { b ->
                            val p = com.example.data.models.NepalLocationsPreset.presets.find { it.nameEn == b.pickupName }
                                ?: com.example.data.models.NepalLocationsPreset.presets[0]
                            val d = com.example.data.models.NepalLocationsPreset.presets.find { it.nameEn == b.dropName }
                                ?: com.example.data.models.NepalLocationsPreset.presets[1]
                            viewModel.pickupLocation.value = p
                            viewModel.dropLocation.value = d
                            try {
                                viewModel.selectedVehicle.value = com.example.data.models.VehicleType.valueOf(b.vehicleType)
                            } catch (e: Exception) {}
                            viewModel.currentScreen.value = "home"
                        }
                    )

                    "admin" -> AdminDashboardScreen(
                        bookings = allBookings,
                        drivers = allDrivers,
                        userAccount = userProfile,
                        language = currentLanguage,
                        onToggleDriverOnline = { id, online -> viewModel.toggleDriverOnline(id, online) },
                        onToggleDriverVerification = { id, verified -> viewModel.toggleDriverVerification(id, verified) }
                    )

                    "profile" -> ProfileScreen(
                        userAccount = userProfile,
                        language = currentLanguage,
                        onLanguageChange = { viewModel.setLanguage(it) }
                    )
                }
            }
        }
    }

    // Payment Gateway Dialogs
    showPaymentGateway?.let { method ->
        val currentRide = activeBooking
        val amount = currentRide?.fareNpr ?: 250.0
        val phone = userProfile?.phone ?: "9841123456"
        val rideId = currentRide?.id ?: 0L

        when (method) {
            PaymentMethod.ESEWA -> {
                ESewaPaymentGatewayDialog(
                    amountNpr = amount,
                    userPhone = phone,
                    onSuccess = { txnId ->
                        viewModel.completePaymentAndTrip(rideId, txnId)
                    },
                    onDismiss = { viewModel.showPaymentGateway.value = null }
                )
            }
            PaymentMethod.KHALTI -> {
                KhaltiPaymentGatewayDialog(
                    amountNpr = amount,
                    userPhone = phone,
                    onSuccess = { txnId ->
                        viewModel.completePaymentAndTrip(rideId, txnId)
                    },
                    onDismiss = { viewModel.showPaymentGateway.value = null }
                )
            }
            PaymentMethod.FONEPAY -> {
                FonepayQrDialog(
                    amountNpr = amount,
                    onSuccess = { txnId ->
                        viewModel.completePaymentAndTrip(rideId, txnId)
                    },
                    onDismiss = { viewModel.showPaymentGateway.value = null }
                )
            }
            else -> {}
        }
    }

    // Driver Rating Dialog
    showRatingDialog?.let { booking ->
        DriverRatingDialog(
            booking = booking,
            language = currentLanguage,
            onSubmitRating = { rating, review, tip, tags ->
                viewModel.submitRating(booking.id, rating, review, tip, tags)
            },
            onDismiss = { viewModel.showRatingDialog.value = null }
        )
    }

    // Notification Center Sheet
    if (showNotificationSheet) {
        NotificationSheet(
            notifications = allNotifications,
            language = currentLanguage,
            onMarkAsRead = { viewModel.markNotificationAsRead(it) },
            onMarkAllRead = { viewModel.markAllNotificationsAsRead() },
            onDismiss = { viewModel.showNotificationSheet.value = false }
        )
    }
}
