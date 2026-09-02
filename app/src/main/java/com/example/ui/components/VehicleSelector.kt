package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.AirportShuttle
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ElectricRickshaw
import androidx.compose.material.icons.filled.LocalTaxi
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.VehicleType
import com.example.ui.theme.NepalCrimsonPrimary
import com.example.ui.theme.NepalGoldSecondary
import com.example.util.LanguageManager

@Composable
fun VehicleSelector(
    selectedType: VehicleType,
    onVehicleSelect: (VehicleType) -> Unit,
    distanceKm: Double,
    language: String,
    discountAmountNpr: Double = 0.0,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = LanguageManager.getString("select_vehicle", language),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Surface(
                color = NepalGoldSecondary.copy(alpha = 0.15f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Est. ${distanceKm} km",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFB45309)
                )
            }
        }

        VehicleType.values().forEach { vehicle ->
            val isSelected = vehicle == selectedType
            val rawFare = vehicle.calculateFare(distanceKm)
            val finalFare = (rawFare - discountAmountNpr).coerceAtLeast(vehicle.baseFareNpr)

            val animatedScale by animateFloatAsState(
                targetValue = if (isSelected) 1.02f else 1.0f,
                animationSpec = tween(150),
                label = "card_scale"
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(animatedScale)
                    .clickable { onVehicleSelect(vehicle) },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) {
                        NepalCrimsonPrimary.copy(alpha = 0.06f)
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
                ),
                border = BorderStroke(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = if (isSelected) NepalCrimsonPrimary else Color(0xFFE2E8F0)
                ),
                elevation = CardDefaults.cardElevation(if (isSelected) 4.dp else 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Left: Icon + Name + Description
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isSelected) {
                                        Brush.linearGradient(
                                            listOf(NepalCrimsonPrimary, Color(0xFFE11D48))
                                        )
                                    } else {
                                        Brush.linearGradient(
                                            listOf(Color(0xFFF1F5F9), Color(0xFFE2E8F0))
                                        )
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = getVehicleIcon(vehicle),
                                contentDescription = vehicle.titleEn,
                                tint = if (isSelected) Color.White else Color(0xFF1E293B),
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = when (language) {
                                        "ne" -> vehicle.titleNe
                                        "ne-rom" -> vehicle.titleRom
                                        else -> vehicle.titleEn
                                    },
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    color = Color(0xFFF1F5F9),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Default.Person,
                                            contentDescription = "Capacity",
                                            modifier = Modifier.size(12.dp),
                                            tint = Color(0xFF64748B)
                                        )
                                        Text(
                                            text = "${vehicle.capacity}",
                                            fontSize = 10.sp,
                                            color = Color(0xFF64748B)
                                        )
                                    }
                                }
                            }

                            Text(
                                text = when (language) {
                                    "ne" -> vehicle.subtitleNe
                                    "ne-rom" -> vehicle.subtitleRom
                                    else -> vehicle.subtitleEn
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Text(
                                text = "⏱ ~${vehicle.etaMins} mins away",
                                fontSize = 11.sp,
                                color = NepalGoldSecondary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    // Right: Price in NPR
                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = LanguageManager.formatCurrency(finalFare, language),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = NepalCrimsonPrimary
                        )

                        if (discountAmountNpr > 0) {
                            Text(
                                text = LanguageManager.formatCurrency(rawFare, language),
                                fontSize = 11.sp,
                                color = Color(0xFF94A3B8),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

fun getVehicleIcon(vehicleType: VehicleType): ImageVector {
    return when (vehicleType) {
        VehicleType.BIKE -> Icons.Default.TwoWheeler
        VehicleType.AUTO -> Icons.Default.ElectricRickshaw
        VehicleType.CAR -> Icons.Default.DirectionsCar
        VehicleType.BUS -> Icons.Default.DirectionsBus
    }
}
