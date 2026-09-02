package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.models.RideBooking
import com.example.data.models.UserAccount
import com.example.ui.theme.NepalCrimsonPrimary
import com.example.ui.theme.NepalGoldSecondary
import com.example.ui.theme.StatusError
import com.example.ui.theme.StatusSuccess
import com.example.util.LanguageManager

@Composable
fun LiveTrackingCard(
    booking: RideBooking,
    userAccount: UserAccount?,
    language: String,
    onCancelRide: () -> Unit,
    onTriggerNextStep: () -> Unit,
    onOpenPayment: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showShareModal by remember { mutableStateOf(false) }
    var showSosModal by remember { mutableStateOf(false) }
    var showCancelConfirm by remember { mutableStateOf(false) }
    var showChatModal by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(8.dp),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header: Status Badge + Live Tracking Code
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = when (booking.status) {
                        "SEARCHING" -> NepalGoldSecondary.copy(alpha = 0.2f)
                        "DRIVER_ASSIGNED", "DRIVER_ARRIVED" -> Color(0xFF0284C7).copy(alpha = 0.15f)
                        "ON_TRIP" -> StatusSuccess.copy(alpha = 0.15f)
                        else -> Color(0xFFF1F5F9)
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(
                                    when (booking.status) {
                                        "SEARCHING" -> NepalGoldSecondary
                                        "DRIVER_ASSIGNED", "DRIVER_ARRIVED" -> Color(0xFF0284C7)
                                        "ON_TRIP" -> StatusSuccess
                                        else -> Color.Gray
                                    }
                                )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = when (booking.status) {
                                "SEARCHING" -> LanguageManager.getString("searching_driver", language)
                                "DRIVER_ASSIGNED" -> "Driver Arriving in 3 mins"
                                "DRIVER_ARRIVED" -> "Driver Waiting at Pickup"
                                "ON_TRIP" -> "Trip in Progress"
                                else -> booking.status
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = when (booking.status) {
                                "SEARCHING" -> Color(0xFFB45309)
                                "DRIVER_ASSIGNED", "DRIVER_ARRIVED" -> Color(0xFF0284C7)
                                "ON_TRIP" -> StatusSuccess
                                else -> Color.DarkGray
                            }
                        )
                    }
                }

                // Start Ride OTP Badge
                Surface(
                    color = NepalCrimsonPrimary.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, NepalCrimsonPrimary.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "OTP: ",
                            fontSize = 11.sp,
                            color = NepalCrimsonPrimary,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = booking.otp,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = NepalCrimsonPrimary
                        )
                    }
                }
            }

            // Driver Information Row
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFF8FAFC),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE2E8F0)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Driver Avatar",
                                tint = Color(0xFF475569),
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = booking.driverName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = "Rating",
                                    tint = NepalGoldSecondary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "${booking.driverRating}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFB45309)
                                )
                            }
                            Text(
                                text = "${booking.vehicleType} • ${booking.driverVehicleNumber}",
                                fontSize = 12.sp,
                                color = Color(0xFF64748B),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Call & Message Buttons
                    Row {
                        IconButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                    data = Uri.parse("tel:${booking.driverPhone}")
                                }
                                try {
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Calling driver: ${booking.driverPhone}", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE2E8F0))
                        ) {
                            Icon(Icons.Default.Call, contentDescription = "Call Driver", tint = Color(0xFF0F172A), modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = { showChatModal = true },
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE2E8F0))
                        ) {
                            Icon(Icons.Default.Message, contentDescription = "Message Driver", tint = Color(0xFF0F172A), modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }

            // Route Points
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(StatusSuccess))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Pickup: ${booking.pickupName}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(NepalCrimsonPrimary))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Drop: ${booking.dropName}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Quick Safety and Sharing Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Live Location Sharing
                OutlinedButton(
                    onClick = { showShareModal = true },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF0284C7))
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(LanguageManager.getString("share_location", language), fontSize = 12.sp)
                }

                // SOS Emergency Button
                Button(
                    onClick = { showSosModal = true },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = StatusError)
                ) {
                    Icon(Icons.Default.Emergency, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(LanguageManager.getString("sos_emergency", language), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Action: Advance Simulation or Complete Payment
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { showCancelConfirm = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF64748B))
                ) {
                    Text("Cancel", fontSize = 13.sp)
                }

                Button(
                    onClick = {
                        if (booking.status == "ON_TRIP" || booking.status == "DRIVER_ARRIVED") {
                            onTriggerNextStep()
                        } else {
                            onTriggerNextStep()
                        }
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NepalCrimsonPrimary)
                ) {
                    Text(
                        text = when (booking.status) {
                            "SEARCHING" -> "Simulate: Driver Found"
                            "DRIVER_ASSIGNED" -> "Simulate: Driver Arrived"
                            "DRIVER_ARRIVED" -> "Start Trip (Match OTP)"
                            "ON_TRIP" -> "Complete Trip & Pay"
                            else -> "Proceed"
                        },
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }

    // Share Location Dialog
    if (showShareModal) {
        val shareUrl = "https://yatri.np/track/${booking.bookingReference}"
        val coordsText = "${booking.pickupLat}, ${booking.pickupLng}"
        val emergencyPhone = userAccount?.emergencyContactPhone ?: "+977 9841987654"

        AlertDialog(
            onDismissRequest = { showShareModal = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = Color(0xFF0284C7))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Share Live Trip Location")
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "Anyone with this secure link can track your live movement across Nepal in real time:",
                        fontSize = 13.sp
                    )
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFFF1F5F9),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(shareUrl, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0284C7))
                            IconButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    clipboard.setPrimaryClip(ClipData.newPlainText("Trip Link", shareUrl))
                                    Toast.makeText(context, "Tracking link copied to clipboard!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy Link", modifier = Modifier.size(16.dp))
                            }
                        }
                    }

                    Text("Emergency Contact: ${userAccount?.emergencyContactName} ($emergencyPhone)", fontSize = 12.sp, color = Color(0xFF64748B))

                    Button(
                        onClick = {
                            val smsIntent = Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse("sms:$emergencyPhone")
                                putExtra("sms_body", "Hi, I am traveling with Yatri Nepal in ${booking.vehicleType} (${booking.driverVehicleNumber}). Live track my trip here: $shareUrl")
                            }
                            try {
                                context.startActivity(smsIntent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Emergency SMS Prepared with Live GPS!", Toast.LENGTH_SHORT).show()
                            }
                            showShareModal = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Send SMS to Emergency Contact", fontWeight = FontWeight.Bold)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showShareModal = false }) {
                    Text("Done")
                }
            }
        )
    }

    // SOS Modal
    if (showSosModal) {
        val emergencyContact = userAccount?.emergencyContactPhone ?: "+977 9841987654"
        AlertDialog(
            onDismissRequest = { showSosModal = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = StatusError)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Emergency SOS Dispatch", color = StatusError, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Triggering SOS will instantly send your live GPS location to Nepal Police (100) and your emergency contact ($emergencyContact).",
                        fontSize = 13.sp
                    )
                    Text(
                        "Current Coordinates: ${booking.pickupLat}, ${booking.pickupLng}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF475569)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        Toast.makeText(context, LanguageManager.getString("emergency_alert_sent", language), Toast.LENGTH_LONG).show()
                        showSosModal = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StatusError)
                ) {
                    Text("Trigger SOS Alert Now", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSosModal = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Cancel Confirm
    if (showCancelConfirm) {
        AlertDialog(
            onDismissRequest = { showCancelConfirm = false },
            title = { Text("Cancel this Ride?") },
            text = { Text("Are you sure you want to cancel your booking? There is no cancellation fee within 5 minutes.") },
            confirmButton = {
                Button(
                    onClick = {
                        showCancelConfirm = false
                        onCancelRide()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StatusError)
                ) {
                    Text("Yes, Cancel Ride")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCancelConfirm = false }) {
                    Text("Keep Ride")
                }
            }
        )
    }

    // Driver Chat Modal
    if (showChatModal) {
        var chatInput by remember { mutableStateOf("") }
        var messages by remember {
            mutableStateOf(
                listOf(
                    "Driver: Namaste! I am on my way, reaching in 3 mins.",
                    "You: Sure, I am standing near the main gate."
                )
            )
        }

        Dialog(onDismissRequest = { showChatModal = false }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .clip(RoundedCornerShape(20.dp)),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Chat with ${booking.driverName}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        IconButton(onClick = { showChatModal = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .background(Color(0xFFF1F5F9), RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        messages.forEach { msg ->
                            val isUser = msg.startsWith("You:")
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isUser) NepalCrimsonPrimary else Color.White,
                                modifier = Modifier.align(if (isUser) Alignment.End else Alignment.Start)
                            ) {
                                Text(
                                    text = msg,
                                    color = if (isUser) Color.White else Color.Black,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        androidx.compose.material3.OutlinedTextField(
                            value = chatInput,
                            onValueChange = { chatInput = it },
                            placeholder = { Text("Type message...", fontSize = 12.sp) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (chatInput.isNotBlank()) {
                                    messages = messages + "You: $chatInput"
                                    chatInput = ""
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NepalCrimsonPrimary)
                        ) {
                            Text("Send")
                        }
                    }
                }
            }
        }
    }
}
