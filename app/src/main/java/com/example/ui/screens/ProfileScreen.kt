package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.UserAccount
import com.example.ui.theme.ESewaGreen
import com.example.ui.theme.KhaltiPurple
import com.example.ui.theme.NepalCrimsonPrimary
import com.example.ui.theme.NepalGoldSecondary
import com.example.ui.theme.StatusSuccess
import com.example.util.LanguageManager

@Composable
fun ProfileScreen(
    userAccount: UserAccount?,
    language: String,
    onLanguageChange: (String) -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var emergencyName by remember { mutableStateOf(userAccount?.emergencyContactName ?: "Sunita Sharma (Sister)") }
    var emergencyPhone by remember { mutableStateOf(userAccount?.emergencyContactPhone ?: "+977 9841987654") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = 90.dp)
    ) {
        // Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = NepalCrimsonPrimary
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Avatar",
                        tint = NepalCrimsonPrimary,
                        modifier = Modifier.size(48.dp)
                    )
                }

                Text(
                    text = userAccount?.fullName ?: "Aarav Sharma",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = Color.White
                )

                Text(
                    text = "${userAccount?.phone ?: "+977 9841123456"} • Kathmandu, Nepal",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Language Preference
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Language, contentDescription = null, tint = NepalCrimsonPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = LanguageManager.getString("language", language),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        LanguageButton("English", language == "en") { onLanguageChange("en") }
                        LanguageButton("नेपाली", language == "ne") { onLanguageChange("ne") }
                        LanguageButton("Romanized", language == "ne-rom") { onLanguageChange("ne-rom") }
                    }
                }
            }

            // Linked Payment Wallets
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = ESewaGreen)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Nepal Digital Wallets", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }

                    WalletStatusRow(name = "eSewa ID: 9841123456", brand = "eSewa Verified", brandColor = ESewaGreen)
                    WalletStatusRow(name = "Khalti ID: 9841123456", brand = "Khalti Linked", brandColor = KhaltiPurple)
                    WalletStatusRow(name = "Fonepay Interoperable QR", brand = "Active", brandColor = Color(0xFF0284C7))
                }
            }

            // Emergency Contacts & SOS Setup
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Emergency, contentDescription = null, tint = NepalCrimsonPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Emergency SOS Contact", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }

                    Text(
                        text = "Used for 1-tap live GPS tracking link sharing and automated SMS emergency dispatch.",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )

                    OutlinedTextField(
                        value = emergencyName,
                        onValueChange = { emergencyName = it },
                        label = { Text("Contact Name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = emergencyPhone,
                        onValueChange = { emergencyPhone = it },
                        label = { Text("Contact Phone (Nepal Mobile)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )

                    Button(
                        onClick = {
                            Toast.makeText(context, "Emergency contact updated!", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F172A)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save Emergency Settings")
                    }
                }
            }

            // Support & Helpline
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.SupportAgent, contentDescription = null, tint = NepalCrimsonPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("24/7 Nepal Rider Support", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                    Text("Toll Free: 1660-01-92874 • Kathmandu Control: +977-1-4412345", fontSize = 12.sp, color = Color(0xFF475569))
                    Text("Emergency Police: 100 • Traffic Police: 103", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = NepalCrimsonPrimary)
                }
            }
        }
    }
}

@Composable
private fun LanguageButton(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() },
        color = if (isSelected) NepalCrimsonPrimary else Color(0xFFF1F5F9),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            fontSize = 12.sp,
            color = if (isSelected) Color.White else Color(0xFF334155)
        )
    }
}

@Composable
private fun WalletStatusRow(name: String, brand: String, brandColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(name, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        Surface(
            color = brandColor.copy(alpha = 0.15f),
            shape = RoundedCornerShape(6.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = brandColor, modifier = Modifier.size(12.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(brand, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = brandColor)
            }
        }
    }
}
