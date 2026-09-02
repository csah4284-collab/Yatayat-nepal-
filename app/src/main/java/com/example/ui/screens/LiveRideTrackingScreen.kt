package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.NepalLocation
import com.example.data.models.NepalLocationsPreset
import com.example.data.models.RideBooking
import com.example.data.models.UserAccount
import com.example.ui.components.LiveTrackingCard
import com.example.ui.components.NepalMapCanvas
import com.example.ui.theme.NepalCrimsonPrimary
import com.example.ui.theme.NepalGoldSecondary
import com.example.ui.theme.StatusSuccess
import com.example.util.LanguageManager

@Composable
fun LiveRideTrackingScreen(
    activeBooking: RideBooking?,
    userAccount: UserAccount?,
    tripProgress: Float,
    language: String,
    onBack: () -> Unit,
    onCancelRide: () -> Unit,
    onTriggerNextStep: () -> Unit,
    onOpenPayment: () -> Unit,
    onBookNewRide: () -> Unit
) {
    if (activeBooking == null || activeBooking.status == "COMPLETED" || activeBooking.status == "CANCELLED") {
        // Empty / No Active Ride state
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(NepalCrimsonPrimary.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.DirectionsCar,
                            contentDescription = null,
                            tint = NepalCrimsonPrimary,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Text(
                        text = if (language == "ne") "हाल कुनै सक्रिय यात्रा छैन" else "No Active Trip in Progress",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Text(
                        text = if (language == "ne")
                            "बाइक, अटो, क्याब वा बस बुक गर्न गृहपृष्ठमा जानुहोस्।"
                        else
                            "Book a Bike, Auto, Cab or Bus from the Home tab to track your live journey.",
                        color = Color(0xFF64748B),
                        fontSize = 13.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    Button(
                        onClick = onBookNewRide,
                        colors = ButtonDefaults.buttonColors(containerColor = NepalCrimsonPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(LanguageManager.getString("book_ride", language), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    } else {
        val pickupPreset = NepalLocationsPreset.presets.find { it.nameEn == activeBooking.pickupName }
            ?: NepalLocation("p1", activeBooking.pickupName, activeBooking.pickupName, activeBooking.pickupName, activeBooking.pickupAddress, "Kathmandu", activeBooking.pickupLat, activeBooking.pickupLng)

        val dropPreset = NepalLocationsPreset.presets.find { it.nameEn == activeBooking.dropName }
            ?: NepalLocation("d1", activeBooking.dropName, activeBooking.dropName, activeBooking.dropName, activeBooking.dropAddress, "Kathmandu", activeBooking.dropLat, activeBooking.dropLng)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
        ) {
            // Top Bar
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = LanguageManager.getString("tracking_title", language),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Ref: ${activeBooking.bookingReference} • ${activeBooking.vehicleType}",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                    }

                    Surface(
                        color = StatusSuccess.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "LIVE GPS",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = StatusSuccess
                        )
                    }
                }
            }

            // Map and Tracking Card layout
            Box(modifier = Modifier.fillMaxSize()) {
                // Interactive Map Canvas taking full space
                NepalMapCanvas(
                    pickupLocation = pickupPreset,
                    dropLocation = dropPreset,
                    tripProgress = tripProgress,
                    vehicleType = activeBooking.vehicleType,
                    modifier = Modifier.fillMaxSize(),
                    isInteractive = true
                )

                // Bottom Floating Live Tracking Details Card
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    LiveTrackingCard(
                        booking = activeBooking,
                        userAccount = userAccount,
                        language = language,
                        onCancelRide = onCancelRide,
                        onTriggerNextStep = onTriggerNextStep,
                        onOpenPayment = onOpenPayment
                    )
                }
            }
        }
    }
}
