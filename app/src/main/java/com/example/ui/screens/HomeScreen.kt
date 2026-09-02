package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.models.NepalLocation
import com.example.data.models.NepalLocationsPreset
import com.example.data.models.PaymentMethod
import com.example.data.models.RideBooking
import com.example.data.models.VehicleType
import com.example.ui.components.NepalMapCanvas
import com.example.ui.components.PaymentMethodSelectorSheet
import com.example.ui.components.VehicleSelector
import com.example.ui.theme.ESewaGreen
import com.example.ui.theme.KhaltiPurple
import com.example.ui.theme.NepalCrimsonPrimary
import com.example.ui.theme.NepalGoldSecondary
import com.example.ui.theme.StatusSuccess
import com.example.util.LanguageManager

@Composable
fun HomeScreen(
    pickupLocation: NepalLocation,
    dropLocation: NepalLocation,
    selectedVehicle: VehicleType,
    selectedPaymentMethod: PaymentMethod,
    promoCode: String,
    discountAmountNpr: Double,
    promoMessage: String,
    activeBooking: RideBooking?,
    unreadCount: Int,
    language: String,
    onPickupChange: (NepalLocation) -> Unit,
    onDropChange: (NepalLocation) -> Unit,
    onSwapLocations: () -> Unit,
    onVehicleSelect: (VehicleType) -> Unit,
    onPaymentMethodSelect: (PaymentMethod) -> Unit,
    onApplyPromo: (String) -> Unit,
    onBookRide: () -> Unit,
    onNavigateToTracking: () -> Unit,
    onOpenNotifications: () -> Unit,
    onLanguageChange: (String) -> Unit
) {
    val scrollState = rememberScrollState()
    val distance = NepalLocationsPreset.calculateDistanceKm(pickupLocation, dropLocation)
    val rawFare = selectedVehicle.calculateFare(distance)
    val finalFare = (rawFare - discountAmountNpr).coerceAtLeast(selectedVehicle.baseFareNpr)

    var promoInput by remember { mutableStateOf(promoCode) }
    var showPickupDropdown by remember { mutableStateOf(false) }
    var showDropDropdown by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = 90.dp)
    ) {
        // Top Header with Branding, Language switch & Notifications
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(NepalCrimsonPrimary, Color(0xFF9E0C22))
                    )
                )
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = LanguageManager.getString("app_title", language),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(
                            text = LanguageManager.getString("tagline", language),
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Language Switcher Chips
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color.White.copy(alpha = 0.2f)
                        ) {
                            Row(modifier = Modifier.padding(2.dp)) {
                                LanguageChip("EN", language == "en") { onLanguageChange("en") }
                                LanguageChip("नेपाली", language == "ne") { onLanguageChange("ne") }
                                LanguageChip("Rom", language == "ne-rom") { onLanguageChange("ne-rom") }
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Notification Icon with Badge
                        IconButton(
                            onClick = onOpenNotifications,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f))
                                .testTag("notification_button")
                        ) {
                            BadgedBox(
                                badge = {
                                    if (unreadCount > 0) {
                                        Badge(containerColor = NepalGoldSecondary) {
                                            Text("$unreadCount", color = Color.Black, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            ) {
                                Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = Color.White)
                            }
                        }
                    }
                }
            }
        }

        // Active Ride Shortcut Banner (if in progress)
        if (activeBooking != null && activeBooking.status != "COMPLETED" && activeBooking.status != "CANCELLED") {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onNavigateToTracking() }
                    .shadow(4.dp, RoundedCornerShape(16.dp)),
                color = Color(0xFF0F172A)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(StatusSuccess),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.DirectionsCar, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Active Trip: ${activeBooking.vehicleType}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("Driver: ${activeBooking.driverName} • Tap to Track", color = Color(0xFF94A3B8), fontSize = 11.sp)
                        }
                    }
                    Icon(Icons.Default.ArrowForward, contentDescription = "Open", tint = NepalGoldSecondary)
                }
            }
        }

        // Hero Banner Graphic
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .height(130.dp)
                .clip(RoundedCornerShape(16.dp))
                .shadow(2.dp, RoundedCornerShape(16.dp))
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_hero_banner),
                contentDescription = "Nepal Transit Hero",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            // Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color.Black.copy(alpha = 0.65f), Color.Transparent)
                        )
                    )
                    .padding(14.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Column {
                    Text(
                        text = if (language == "ne") "काठमाडौँ देखि पोखरासम्म" else "City & Valley Rides",
                        color = NepalGoldSecondary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Text(
                        text = if (language == "ne") "सुलभ भाडा, भरपर्दो सेवा" else "Affordable, Verified Drivers",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp
                    )
                }
            }
        }

        // Main Booking Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(3.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = LanguageManager.getString("book_ride", language),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                // Location Picker Card
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFF8FAFC),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        // Pickup row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(StatusSuccess)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Box(modifier = Modifier.weight(1f)) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { showPickupDropdown = true }
                                ) {
                                    Text(
                                        text = LanguageManager.getString("pickup_location", language),
                                        fontSize = 11.sp,
                                        color = Color(0xFF64748B)
                                    )
                                    Text(
                                        text = pickupLocation.getDisplayName(language),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = pickupLocation.address,
                                        fontSize = 11.sp,
                                        color = Color(0xFF94A3B8),
                                        maxLines = 1
                                    )
                                }

                                DropdownMenu(
                                    expanded = showPickupDropdown,
                                    onDismissRequest = { showPickupDropdown = false }
                                ) {
                                    NepalLocationsPreset.presets.forEach { loc ->
                                        DropdownMenuItem(
                                            text = {
                                                Column {
                                                    Text(loc.getDisplayName(language), fontWeight = FontWeight.Bold)
                                                    Text(loc.address, fontSize = 11.sp, color = Color.Gray)
                                                }
                                            },
                                            onClick = {
                                                onPickupChange(loc)
                                                showPickupDropdown = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        // Swap Divider
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(24.dp)
                                    .background(Color(0xFFCBD5E1))
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(
                                onClick = onSwapLocations,
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    Icons.Default.SwapVert,
                                    contentDescription = "Swap Locations",
                                    tint = NepalCrimsonPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        // Drop row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(NepalCrimsonPrimary)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Box(modifier = Modifier.weight(1f)) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { showDropDropdown = true }
                                ) {
                                    Text(
                                        text = LanguageManager.getString("drop_location", language),
                                        fontSize = 11.sp,
                                        color = Color(0xFF64748B)
                                    )
                                    Text(
                                        text = dropLocation.getDisplayName(language),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = dropLocation.address,
                                        fontSize = 11.sp,
                                        color = Color(0xFF94A3B8),
                                        maxLines = 1
                                    )
                                }

                                DropdownMenu(
                                    expanded = showDropDropdown,
                                    onDismissRequest = { showDropDropdown = false }
                                ) {
                                    NepalLocationsPreset.presets.forEach { loc ->
                                        DropdownMenuItem(
                                            text = {
                                                Column {
                                                    Text(loc.getDisplayName(language), fontWeight = FontWeight.Bold)
                                                    Text(loc.address, fontSize = 11.sp, color = Color.Gray)
                                                }
                                            },
                                            onClick = {
                                                onDropChange(loc)
                                                showDropDropdown = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Map Preview
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    NepalMapCanvas(
                        pickupLocation = pickupLocation,
                        dropLocation = dropLocation,
                        tripProgress = 0.0f,
                        vehicleType = selectedVehicle.name,
                        isInteractive = false
                    )
                }

                // Vehicle Selector for Bike, Auto, Car, Bus
                VehicleSelector(
                    selectedType = selectedVehicle,
                    onVehicleSelect = onVehicleSelect,
                    distanceKm = distance,
                    language = language,
                    discountAmountNpr = discountAmountNpr
                )

                // Promo Code Bar
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = promoInput,
                            onValueChange = { promoInput = it },
                            placeholder = { Text("Enter Promo: YATRI50 / ESEWAPAY", fontSize = 12.sp) },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .testTag("promo_input"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            leadingIcon = { Icon(Icons.Default.LocalOffer, contentDescription = null, tint = NepalGoldSecondary) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { onApplyPromo(promoInput) },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NepalCrimsonPrimary),
                            modifier = Modifier.testTag("apply_promo_button")
                        ) {
                            Text(LanguageManager.getString("apply", language), fontWeight = FontWeight.Bold)
                        }
                    }

                    if (promoMessage.isNotEmpty()) {
                        Text(
                            text = promoMessage,
                            fontSize = 11.sp,
                            color = if (discountAmountNpr > 0) StatusSuccess else MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Payment Method Selector
                PaymentMethodSelectorSheet(
                    selectedMethod = selectedPaymentMethod,
                    onMethodSelect = onPaymentMethodSelect,
                    language = language
                )

                // Confirm and Book Ride Button
                Button(
                    onClick = onBookRide,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("confirm_booking_button"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NepalCrimsonPrimary),
                    elevation = ButtonDefaults.buttonElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = LanguageManager.getString("confirm_booking", language),
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 15.sp
                            )
                            Text(
                                text = "${selectedVehicle.titleEn} • ${selectedPaymentMethod.brandName}",
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                        Text(
                            text = LanguageManager.formatCurrency(finalFare, language),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LanguageChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        color = if (isSelected) Color.White else Color.Transparent,
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) NepalCrimsonPrimary else Color.White
        )
    }
}
