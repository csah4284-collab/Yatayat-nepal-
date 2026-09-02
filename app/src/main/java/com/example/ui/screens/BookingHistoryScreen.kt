package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ElectricRickshaw
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.models.RideBooking
import com.example.ui.components.getVehicleIcon
import com.example.ui.theme.ESewaGreen
import com.example.ui.theme.KhaltiPurple
import com.example.ui.theme.NepalCrimsonPrimary
import com.example.ui.theme.NepalGoldSecondary
import com.example.ui.theme.StatusError
import com.example.ui.theme.StatusSuccess
import com.example.util.LanguageManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BookingHistoryScreen(
    bookings: List<RideBooking>,
    language: String,
    onRebookRide: (RideBooking) -> Unit
) {
    var selectedFilter by remember { mutableStateOf("ALL") }
    var selectedReceiptBooking by remember { mutableStateOf<RideBooking?>(null) }

    val filteredList = when (selectedFilter) {
        "COMPLETED" -> bookings.filter { it.status == "COMPLETED" }
        "ACTIVE" -> bookings.filter { it.status in listOf("SEARCHING", "DRIVER_ASSIGNED", "DRIVER_ARRIVED", "ON_TRIP") }
        "CANCELLED" -> bookings.filter { it.status == "CANCELLED" }
        else -> bookings
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp)
    ) {
        // Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (language == "ne") "यात्रा इतिहास तथा बिलहरू" else "Booking History & Receipts",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "Track all past rides across Nepal with digital tax invoices",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Filter Chips
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val filters = listOf("ALL", "COMPLETED", "ACTIVE", "CANCELLED")
                    items(filters) { filter ->
                        val isSelected = selectedFilter == filter
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .clickable { selectedFilter = filter },
                            color = if (isSelected) NepalCrimsonPrimary else Color(0xFFF1F5F9),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(
                                text = when (filter) {
                                    "ALL" -> if (language == "ne") "सबै (${bookings.size})" else "All (${bookings.size})"
                                    "COMPLETED" -> if (language == "ne") "सम्पन्न" else "Completed"
                                    "ACTIVE" -> if (language == "ne") "सक्रिय" else "Active"
                                    "CANCELLED" -> if (language == "ne") "रद्द" else "Cancelled"
                                    else -> filter
                                },
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else Color(0xFF475569)
                            )
                        }
                    }
                }
            }
        }

        // List
        if (filteredList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        Icons.Default.History,
                        contentDescription = null,
                        tint = Color(0xFFCBD5E1),
                        modifier = Modifier.size(64.dp)
                    )
                    Text("No bookings found in this category", color = Color(0xFF64748B), fontSize = 14.sp)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredList) { booking ->
                    BookingHistoryCard(
                        booking = booking,
                        language = language,
                        onViewReceipt = { selectedReceiptBooking = booking },
                        onRebook = { onRebookRide(booking) }
                    )
                }
            }
        }
    }

    // Tax Invoice / Digital Receipt Dialog
    if (selectedReceiptBooking != null) {
        val b = selectedReceiptBooking!!
        val dateStr = SimpleDateFormat("dd MMMM yyyy, hh:mm a", Locale.getDefault()).format(Date(b.createdAt))
        val subtotal = b.originalFareNpr
        val discount = b.promoDiscountNpr
        val tip = b.tipAmountNpr
        val vat = (b.fareNpr * 0.13) // Nepal VAT 13% demonstration breakdown
        val total = b.fareNpr + tip

        Dialog(onDismissRequest = { selectedReceiptBooking = null }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .clip(RoundedCornerShape(24.dp)),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Official Trip Receipt", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                            Text("PAN / VAT: 609821448", fontSize = 11.sp, color = Color(0xFF64748B))
                        }
                        IconButton(onClick = { selectedReceiptBooking = null }) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    }

                    Divider(color = Color(0xFFE2E8F0))

                    // Booking Info
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Invoice No:", fontSize = 12.sp, color = Color(0xFF64748B))
                        Text(b.bookingReference, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Date & Time:", fontSize = 12.sp, color = Color(0xFF64748B))
                        Text(dateStr, fontSize = 11.sp)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Vehicle & Driver:", fontSize = 12.sp, color = Color(0xFF64748B))
                        Text("${b.vehicleType} • ${b.driverName}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Surface(
                        color = Color(0xFFF8FAFC),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("From: ${b.pickupName}", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                            Text("To: ${b.dropName}", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                            Text("Distance: ${b.distanceKm} km (${b.durationMins} mins)", fontSize = 11.sp, color = Color(0xFF64748B))
                        }
                    }

                    Divider(color = Color(0xFFE2E8F0))

                    // Price Breakdown
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Base Ride Fare:", fontSize = 12.sp, color = Color(0xFF64748B))
                        Text("NPR $subtotal", fontSize = 12.sp)
                    }

                    if (discount > 0) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Promo Discount (${b.promoCode}):", fontSize = 12.sp, color = StatusSuccess)
                            Text("- NPR $discount", fontSize = 12.sp, color = StatusSuccess, fontWeight = FontWeight.Bold)
                        }
                    }

                    if (tip > 0) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Driver Tip:", fontSize = 12.sp, color = Color(0xFF64748B))
                            Text("NPR $tip", fontSize = 12.sp)
                        }
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Payment Method:", fontSize = 12.sp, color = Color(0xFF64748B))
                        Text(
                            "${b.paymentMethod} (${b.paymentStatus})",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (b.paymentMethod == "ESEWA") ESewaGreen else if (b.paymentMethod == "KHALTI") KhaltiPurple else Color(0xFF0F172A)
                        )
                    }

                    Divider(color = Color(0xFFE2E8F0))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Total Paid Amount:", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
                        Text("NPR $total", fontWeight = FontWeight.Black, fontSize = 18.sp, color = NepalCrimsonPrimary)
                    }

                    Button(
                        onClick = {
                            selectedReceiptBooking = null
                            onRebookRide(b)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = NepalCrimsonPrimary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Book this Ride Again", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun BookingHistoryCard(
    booking: RideBooking,
    language: String,
    onViewReceipt: () -> Unit,
    onRebook: () -> Unit
) {
    val dateStr = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(booking.createdAt))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Top Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFF1F5F9)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (booking.vehicleType) {
                                "BIKE" -> Icons.Default.TwoWheeler
                                "AUTO" -> Icons.Default.ElectricRickshaw
                                "CAR" -> Icons.Default.DirectionsCar
                                "BUS" -> Icons.Default.DirectionsBus
                                else -> Icons.Default.DirectionsCar
                            },
                            contentDescription = booking.vehicleType,
                            tint = NepalCrimsonPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "${booking.vehicleType} • ${booking.bookingReference}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(dateStr, fontSize = 11.sp, color = Color(0xFF94A3B8))
                    }
                }

                Surface(
                    color = when (booking.status) {
                        "COMPLETED" -> StatusSuccess.copy(alpha = 0.12f)
                        "CANCELLED" -> StatusError.copy(alpha = 0.12f)
                        else -> NepalGoldSecondary.copy(alpha = 0.15f)
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = booking.status,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (booking.status) {
                            "COMPLETED" -> StatusSuccess
                            "CANCELLED" -> StatusError
                            else -> Color(0xFFB45309)
                        }
                    )
                }
            }

            // Route
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(StatusSuccess))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(booking.pickupName, fontSize = 12.sp, fontWeight = FontWeight.Medium, maxLines = 1)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(NepalCrimsonPrimary))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(booking.dropName, fontSize = 12.sp, fontWeight = FontWeight.Medium, maxLines = 1)
                }
            }

            // Bottom Bar: Price, Payment method badge & Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = LanguageManager.formatCurrency(booking.fareNpr, language),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        color = when (booking.paymentMethod) {
                            "ESEWA" -> ESewaGreen.copy(alpha = 0.15f)
                            "KHALTI" -> KhaltiPurple.copy(alpha = 0.15f)
                            else -> Color(0xFFF1F5F9)
                        },
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = booking.paymentMethod,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = when (booking.paymentMethod) {
                                "ESEWA" -> ESewaGreen
                                "KHALTI" -> KhaltiPurple
                                else -> Color(0xFF475569)
                            }
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    OutlinedButton(
                        onClick = onViewReceipt,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(34.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp)
                    ) {
                        Icon(Icons.Default.Receipt, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Bill", fontSize = 11.sp)
                    }

                    Button(
                        onClick = onRebook,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NepalCrimsonPrimary),
                        modifier = Modifier.height(34.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp)
                    ) {
                        Text("Re-book", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
